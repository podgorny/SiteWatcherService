package it.devchallange.podgorny.resource;

import it.devchallange.podgorny.dao.DAOService;
import it.devchallange.podgorny.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class CategoryResource {

    @Autowired
    @Qualifier("categoryDAOJDBC")
    DAOService categoryDAO;

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    @ResponseBody
    public Set<Category> getAll() {
        return categoryDAO.getAll();
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Category getAct(@PathVariable long id) {
        return (Category) categoryDAO.getEntityById(id);
    }
}
