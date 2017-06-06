package it.devchallange.podgorny.resource;

import it.devchallange.podgorny.dao.DAOService;
import it.devchallange.podgorny.entity.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class SiteResource {

    @Autowired
    @Qualifier("siteDAOJDBC")
    DAOService siteDAO;

    @RequestMapping(value = "/site", method = RequestMethod.GET)
    @ResponseBody
    public Set<Site> getAll() {
        return siteDAO.getAll();
    }

    @RequestMapping(value = "/site/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Site getAct(@PathVariable long id) {
        return (Site) siteDAO.getEntityById(id);
    }
}
