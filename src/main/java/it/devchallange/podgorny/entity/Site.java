package it.devchallange.podgorny.entity;

import it.devchallange.podgorny.http.SimpleSite;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Component
@XmlRootElement(name = "site")
@XmlAccessorType(XmlAccessType.NONE)
public class Site implements Entity, SimpleSite {
    private static final Logger logger = Logger.getLogger(Site.class);

    @XmlElement(name = "id")
    private long id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "url")
    private URL url;
    private Map<String, String> replacements;


    public Site() {
    }

    public Site(long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = convertToURL(url);
    }

    public Site(String name, String url) {
        this.name = name;
        this.url = convertToURL(url);
    }

    public Site(String name, String url, Map<String, String> replacements) {
        this.name = name;
        this.url = convertToURL(url);
        this.replacements = replacements;
    }

    public Site(String name, URL url, Map<String, String> replacements) {
        this.name = name;
        this.url = url;
        this.replacements = replacements;
    }

    public String getURLString(){
        String sURL = url.getProtocol() + "://" + url.getHost() + url.getPath();
        logger.info("String URL:" + sURL + ", replacement:" + replacements);
        if (replacements != null) {
            for (String s : replacements.keySet()) {
                sURL = sURL.replace(s, replacements.get(s));
            }
        }
        return sURL;
    }

    @Override
    public Map<String, String> getReplacementMap() {
        return replacements;
    }

    public URL getUrl(){
        return url;
    }
    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Site site = (Site) o;

        return url != null ? url.equals(site.url) : site.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Site{" +
                "name='" + name + '\'' +
                ", url=" + url +
                '}';
    }

    private URL convertToURL(String src){
        try {
            return new URL(src);
        } catch (MalformedURLException e) {
            logger.error(e);
        }
        return null;
    }
}
