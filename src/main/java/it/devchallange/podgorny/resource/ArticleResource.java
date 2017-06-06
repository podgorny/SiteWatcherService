package it.devchallange.podgorny.resource;

import it.devchallange.podgorny.dao.DAOService;
import it.devchallange.podgorny.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class ArticleResource {

    @Autowired
    @Qualifier("articleDAOJDBC")
    DAOService articleDAO;

    @RequestMapping(value = "/article", method = RequestMethod.GET)
    @ResponseBody
    public Set<Article> getAll() {
        return articleDAO.getAll();
    }

    @RequestMapping(value = "/article/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Article getAct(@PathVariable long id) {
        return (Article) articleDAO.getEntityById(id);
    }


    @RequestMapping(value = "/article/{id}/revision", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Set<Article> getArtRevision(@PathVariable long id) {
        return (Set<Article>) articleDAO.getAllEntityRevision(articleDAO.getEntityById(id));
    }

    @RequestMapping(value = "/article/{id}/diffwithRevision/{idRevision}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDiff(@PathVariable long id, @PathVariable long idRevision) {
        Article src = (Article) articleDAO.getEntityById(id),
                dst = (Article) articleDAO.getEntityById(id);

        return "Oops";
    }
}
