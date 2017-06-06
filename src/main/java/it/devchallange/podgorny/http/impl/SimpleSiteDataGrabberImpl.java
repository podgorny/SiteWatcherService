package it.devchallange.podgorny.http.impl;

import it.devchallange.podgorny.entity.*;
import it.devchallange.podgorny.http.Selector;
import it.devchallange.podgorny.http.SimpleSiteDataGrabber;
import it.devchallange.podgorny.utils.Decoder;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

public class SimpleSiteDataGrabberImpl implements SimpleSiteDataGrabber {
    private static final Logger logger = Logger.getLogger(SimpleSiteDataGrabberImpl.class);
    private Selector categorySelector, titleSelector, contentSelector, nextPageSelector;

    private Map<Site, Document> siteDocumentMap;
    private static Document doc;
    private String rootURL;
    private long delayBetweenNext;

    @Autowired
    @Qualifier("decoder")
    Decoder decoder;

    public SimpleSiteDataGrabberImpl(Selector categorySelector, Selector titleSelector, Selector contentSelector, Selector nextPageSelector) {
        siteDocumentMap = new HashMap<>();
        this.categorySelector = categorySelector;
        this.titleSelector = titleSelector;
        this.contentSelector = contentSelector;
        this.nextPageSelector = nextPageSelector;
    }

    @Override
    public String getContent(Site site){
        doc = getDocument(site);

        return doc != null ? doc.toString() : null;
    }

    @Override
    public List<Article> grab(Site site, long delayBetweenNext) {
        this.delayBetweenNext = delayBetweenNext;
        rootURL = site.getURLString();
        logger.info("Start grabbing site : " + site);
        List<Article> articleList = getArticleListWithoutContent(site);
        logger.info("Processing page: " + site.getURLString() + "; total articles on page " + articleList.size());
        try {
            articleList = processFetchedPageData(site, articleList, false);
        } catch (MalformedURLException e) {
            logger.error(e);
        }
        return articleList;
    }

    public Site nextPage(Site site) {
        doc = getDocument(site);

        Elements pageElements;
        Element pageElement;

        pageElements = doc.select(nextPageSelector.getDOMName() + nextPageSelector.getCSSSelector());
        pageElement = pageElements.first();

        if (logger.isDebugEnabled()){
            logger.debug("page selector is {" + nextPageSelector.getDOMName() + nextPageSelector.getCSSSelector() + "}");
            logger.debug("next page: " + rootURL + pageElement.attr("href"));
        }
//            return new SimpleSiteImpl(new URL(mainURL + pageElement.attr("href")), site.getReplacementMap());
            return new Site(site.getName(), rootURL + pageElement.attr("href"));
    }
    private void waitForNextIteration() {
        try {
            logger.info("waiting for next iteration (ms) : " + delayBetweenNext);
            Thread.sleep(delayBetweenNext);
        } catch (InterruptedException e) {
            logger.error(e);
        }
    }

    private List<Article> processFetchedPageData(Site site, List<Article> articleListWithoutContent, boolean fetchImages) throws MalformedURLException {
        List<Article> articleList = new ArrayList<>();
        for (Article article : articleListWithoutContent){
            Content content = getContentByArticle(new Site(site.getName(), article.getURL()), article, false);
            if (content != null){
                article.setContent(content);
                articleList.add(article);
            }
            waitForNextIteration();
        }
        return articleList;
    }

    private Content getContentByArticle(Site site, Article article, boolean fetchImages) throws MalformedURLException {
        Document doc = getDocument(site);

        Elements contentElemtnt;
        Content content = null;

        //FETCH CONTENT
        contentElemtnt = doc.select(contentSelector.getDOMName() + contentSelector.getCSSSelector());

        if (!fetchImages) {
            String s = contentElemtnt.first().text();
            content = new Content(s, decoder);
        } else{
            // TODO
        }
        if (logger.isDebugEnabled()){
            logger.debug("content selector is {" + contentSelector.getDOMName() + contentSelector.getCSSSelector() + "}");
            logger.debug("CONTENT : " + content);
        }

        return content;
    }

    private List<Article> getArticleListWithoutContent(Site site){
        doc = getDocument(site);

        Elements categoryElements, titleElements;
        List<Article> articleList = new ArrayList<>();

        categoryElements = doc.select(categorySelector.getDOMName() + categorySelector.getCSSSelector());
        titleElements = doc.select(titleSelector.getDOMName() + titleSelector.getCSSSelector());

        for(int i = 0; i < categoryElements.size(); i++){
            Category category = new Category(categoryElements.get(i).text());
            Title title = new Title(titleElements.get(i).text());
            String articleURL = titleElements.get(i).attr("href");

            articleList.add(new Article((Site) site, articleURL, category, title, null));

            if (logger.isDebugEnabled()){
                logger.debug("category selector is {" + categorySelector.getDOMName() + categorySelector.getCSSSelector() + "}");
                logger.debug("title selector is {" + titleSelector.getDOMName() + titleSelector.getCSSSelector() + "}");
                logger.debug("CATEGORY text: " + category.getName());
                logger.debug("TITLE text: " + title.getName());
                logger.debug("TITLE url: " + articleURL);
            }
        };

        return articleList;
    }

    private Document getDocument(Site site)  {
        if (siteDocumentMap.containsKey(site)){
            return siteDocumentMap.get(site);
        }
        try {
            logger.info("Initialize doc for : " + site.getUrl());
            doc = Jsoup.connect(site.getURLString()).get();
            siteDocumentMap.put((Site) site, doc);
        } catch (IOException e) {
            logger.error(e);
        }
        if (doc == null){
            logger.error("Document initialization failde");
            return null;
        }
        if (logger.isTraceEnabled()){
            logger.trace(doc.toString());
        }
        return doc;
    }

}
