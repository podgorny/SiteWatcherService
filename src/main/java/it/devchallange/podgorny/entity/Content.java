package it.devchallange.podgorny.entity;

import it.devchallange.podgorny.utils.Decoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.nio.charset.Charset;
import java.util.List;

@Component
@XmlRootElement(name = "content")
@XmlAccessorType(XmlAccessType.NONE)
public class Content implements Entity {
    private static final Logger logger = Logger.getLogger(Content.class);

    @XmlElement(name = "id")
    private long id;
    @XmlElement(name = "text")
    private String data;
    private String sha256Hex, md5Hex;

    public Content(){}

    public Content(long id, String data, String sha256Hex, String md5Hex) {
        this.id = id;
        this.data = data;
        this.sha256Hex = sha256Hex;
        this.md5Hex = md5Hex;
    }

    public Content(String data, Decoder decoder) {
        this.data = data;
        this.sha256Hex = decoder.getSHA256Hex(data);
        this.md5Hex = decoder.getMD5Hex(data);
    }

    public Content(String data, String sha256Hex, String md5Hex) {
        this.data = data;
        this.sha256Hex = sha256Hex;
        this.md5Hex = md5Hex;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getSha256Hex() {
        return sha256Hex;
    }

    public String getMd5Hex() {
        return md5Hex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Content content = (Content) o;

        if (!sha256Hex.equals(content.sha256Hex)) return false;
        return md5Hex.equals(content.md5Hex);
    }

    @Override
    public int hashCode() {
        int result = sha256Hex.hashCode();
        result = 31 * result + md5Hex.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Content{" +
                (data.length() < 20 ? "data='" + data + '\'' : "md5HEX: " + md5Hex + " : sha256Hex: " + sha256Hex) +
                '}';
    }
}
