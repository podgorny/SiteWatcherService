package it.devchallange.podgorny.resource;

import it.devchallange.podgorny.http.SimpleSiteProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
public class SiteReaderResource {

    @Autowired
    @Qualifier("siteReader")
    SimpleSiteProcessor siteProcessor;

    @RequestMapping(value = "/siteReader", method = RequestMethod.GET)
    @ResponseBody
    public String getStatus() {
        return  "STATUS: " + siteProcessor.getStatus()
                + "<br>NOTE: ALL STATUS CHANGEING APPLIED AFTER grab all articles from page. Also related to delay between grab page"
                + "<br>call /rest/v1/siteReader/pause to pause"
                + "<br>call /rest/v1/siteReader/stop to stop"
                + "<br>call /rest/v1/siteReader/resume to resume"
                + "<br>call /rest/v1/siteReader/renew to renew ALL available articles in db (NOTE: all processing will stop untill renew process finished)"
                + "<br>call /rest/v1/siteReader/renew/{id} to renew needed article";
    }

    @RequestMapping(value = "/siteReader/pause", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String pause() {
        siteProcessor.pause();
        return "PAUSED";
    }

    @RequestMapping(value = "/siteReader/stop", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String stop() {
        siteProcessor.stop();
        return "STOPPED";

    }

    @RequestMapping(value = "/siteReader/resume", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String resume() {
        siteProcessor.resume();
        return "RESUMED";
    }

    @RequestMapping(value = "/siteReader/renew", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String renew() {
        siteProcessor.renewALL();
        return "REFRESHING ALL DATA";
    }

    @RequestMapping(value = "/siteReader/renew/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String renewID(@PathVariable long id) {
        siteProcessor.renewByID(id);
        return "REFRESHING ALL DATA";
    }
}
