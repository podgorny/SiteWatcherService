package it.devchallange.podgorny.http;

import it.devchallange.podgorny.DataGrabber;
import it.devchallange.podgorny.entity.Entity;
import it.devchallange.podgorny.entity.Site;

import java.util.List;

public interface SimpleSiteDataGrabber extends DataGrabber<Site> {
    List<? extends Entity> grab(Site simpleSite, long delayBetweenNext);
    Site nextPage(Site site);
}
