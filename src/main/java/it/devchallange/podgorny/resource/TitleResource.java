package it.devchallange.podgorny.resource;

import it.devchallange.podgorny.dao.DAOService;
import it.devchallange.podgorny.entity.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class TitleResource {

    @Autowired
    @Qualifier("titleDAOJDBC")
    DAOService titleDAO;

    @RequestMapping(value = "/title", method = RequestMethod.GET)
    @ResponseBody
    public Set<Title> getAll() {
        return titleDAO.getAll();
    }

    @RequestMapping(value = "/title/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Title getAct(@PathVariable long id) {
        return (Title) titleDAO.getEntityById(id);
    }
}
