package it.devchallange.podgorny.http.impl;

import it.devchallange.podgorny.dao.DAOService;
import it.devchallange.podgorny.entity.Article;
import it.devchallange.podgorny.entity.Site;
import it.devchallange.podgorny.http.SimpleSite;
import it.devchallange.podgorny.http.SimpleSiteProcessor;
import it.devchallange.podgorny.http.SimpleSiteDataGrabber;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleSiteProcessorImpl implements SimpleSiteProcessor {
    private static Logger logger = Logger.getLogger(SimpleSiteProcessorImpl.class);

    Map<Site, SimpleSiteDataGrabber> grabberMap;

    private long delayBetweenGrabNext;
    private readerStatus currentStatus;
    private boolean paused, stopped;

    @Autowired
    @Qualifier("articleDAOJDBC")
    private DAOService<Article> articleDAOService;

    enum readerStatus {
        RUNNING,
        STOPPED,
        PAUSED,
        RESUMED,
        RENEW_DATA
    }

    @PostConstruct
    private void startReading()
    {
        read();
    }

    public SimpleSiteProcessorImpl(Map<Site, SimpleSiteDataGrabber> grabberMap, long delayBetweenGrabNext) {
        this.grabberMap = grabberMap;
        this.delayBetweenGrabNext = delayBetweenGrabNext;
        logger.info("TOTAL SITES FOR GRAB: " + Arrays.toString(grabberMap.keySet().toArray()));
    }

    @Override
    public void read() {
        logger.info("Starting...");
        if (currentStatus == null) resume(); // to set init status
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(10);
        for(Map.Entry<Site, SimpleSiteDataGrabber> entry : grabberMap.entrySet()){
            threadPoolExecutor.submit(() -> {
                Site site = entry.getKey();
                SimpleSiteDataGrabber grabber = entry.getValue();
                read(site, site, grabber, delayBetweenGrabNext);
            });
        }
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void stop() {
        stopped = true;
    }

    @Override
    public void resume() {
        paused = false;
        stopped = false;
    }

    @Override
    public String getStatus() {
        return currentStatus.toString();
    }

    @Override
    public void renewALL() {
        pause();
        logger.info("Renewing ALL");
        currentStatus = readerStatus.RENEW_DATA;
        Set<Article> articles = articleDAOService.getAll();
        for(Article article : articles){
            logger.info("Renewing " + article.getURL());
            renewArticle(article.getSite(), new Site(article.getSite().getName(), article.getURL()), grabberMap.get(article.getSite()), delayBetweenGrabNext);
        }
        resume();
    }

    @Override
    public void renewByID(long id) {
        pause();
        Article article = articleDAOService.getEntityById(id);
        logger.info("Renewing " + article.getURL());
        renewArticle(article.getSite(), new Site(article.getSite().getName(), article.getURL()), grabberMap.get(article.getSite()), delayBetweenGrabNext);
        resume();
    }
    private void renewArticle(final Site root, Site site, SimpleSiteDataGrabber grabber, long delayBetweenGrabNext){
        List<Article> articleList = (List<Article>) grabber.grab(site, delayBetweenGrabNext);
        insertToDB(articleList, root);
        read(root, grabber.nextPage(site), grabber, delayBetweenGrabNext);
    }

    private void read(final Site root, Site site, SimpleSiteDataGrabber grabber, long delayBetweenGrabNext){
        logger.info("Start read " + site.getURLString() + ", status=" + currentStatus);
        while (currentStatus != readerStatus.RUNNING){
            try {
                Thread.sleep(5000);
                currentStatus = checkStatus();
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
        List<Article> articleList = (List<Article>) grabber.grab(site, delayBetweenGrabNext);
        insertToDB(articleList, root);
        read(root, grabber.nextPage(site), grabber, delayBetweenGrabNext);
    }

    private readerStatus checkStatus() {
        if (stopped) return readerStatus.STOPPED;
        if (paused) return readerStatus.PAUSED;
        if (currentStatus == readerStatus.PAUSED && !paused) return readerStatus.RESUMED;
        return readerStatus.RUNNING;
    }

    private synchronized void insertToDB(List<Article> articleList, Site root){
        logger.info("Insertint to DB list of Article");
        logger.info(articleList.get(0).getCategory().getName());
        articleList.forEach((i) -> i.setSite(root));
        articleDAOService.insert(new HashSet<>(articleList));
    }
}
