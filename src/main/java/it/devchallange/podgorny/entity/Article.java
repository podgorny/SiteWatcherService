package it.devchallange.podgorny.entity;

import it.devchallange.podgorny.dao.jdbc.impl.ArticleDAOJDBCImpl;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Component
@XmlRootElement(name = "title")
@XmlAccessorType(XmlAccessType.NONE)
public class Article implements Entity {

    @XmlElement(name = "id")
    private long id;
    @XmlElement(name = "url")
    private String url;
    @XmlElement(name = "site")
    private Site site;
    @XmlElement(name = "category")
    private Category category;
    @XmlElement(name = "title")
    private Title title;
    @XmlElement(name = "content")
    private Content content;
    @XmlElement(name = "date")
    private Date date;
    @XmlElement(name = "revisiton")
    private long revision;

    public Article() {
    }

    public Article(long id, Site site, String url, Category category, Title title, Content content, Long revision, Date date) {
        this.id = id;
        this.site = site;
        this.url = url;
        this.category = category;
        this.title = title;
        this.content = content;
        this.revision = revision;
        this.date = date;
    }

    public Article(Article article) {
        this.id = article.id;
        this.site = article.site;
        this.url = article.url;
        this.category = article.category;
        this.title = article.title;
        this.content = article.content;
        this.revision = article.revision;
        this.date = article.date;
    }

    public Article(Site site, String url, Category category, Title title, Content content) {
        this.site = site;
        this.url = url;
        this.category = category;
        this.title = title;
        this.content = content;
    }




    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public Title getTitle() {
        return title;
    }

    public Content getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (!site.equals(article.site)) return false;
        if (!url.equals(article.url)) return false;
        if (!category.equals(article.category)) return false;
        if (!title.equals(article.title)) return false;
        return content.equals(article.content);
    }

    @Override
    public int hashCode() {
        int result = category.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + site.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Article{" +
                "site=" + site +
                "category=" + category +
                ", title=" + title +
                ", content HASH=" + content.getMd5Hex() +
                '}';
    }

    public String getURL() {
        return url;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public long getCurrentRevision() {
        return revision;
    }

    public Site getSite() {
        return site;
    }
    public void setSite(Site site) {
        this.site = site;
    }

}
