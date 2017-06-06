package it.devchallange.podgorny.http;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Hithere extends HttpServlet {
    private static Logger logger = Logger.getLogger(Hithere.class);
    private StringBuilder builder;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/html");

        // Actual logic goes here.
        PrintWriter out = response.getWriter();

        builder = new StringBuilder("<html><title>");
        builder.append("Semifinal");
        builder.append("</title><body>");

        builder.append("<h1>Available services: </h1>");
        builder.append("<h1>Data store to DB after grab all articles from page <br>(between next page with articles reading)</h1>");

//TODO: replace with reading from PROPERTIES

        builder.append("<a href=http://127.0.0.1:8888/SiteWatcher/rest/v1/siteReader>");
        builder.append("http://127.0.0.1:8888/SiteWatcher/rest/v1/siteReader");
        builder.append("</a><br>");

        builder.append("<a href=http://127.0.0.1:8888/SiteWatcher/rest/v1/title/{id}>");
        builder.append("http://127.0.0.1:8888/SiteWatcher/rest/v1/title/{id}");
        builder.append("</a><br>");

        builder.append("<a href=http://127.0.0.1:8888/SiteWatcher/rest/v1/site>");
        builder.append("http://127.0.0.1:8888/SiteWatcher/rest/v1/site");
        builder.append("</a><br>");

        builder.append("<a href=http://127.0.0.1:8888/SiteWatcher/rest/v1/site/{id}>");
        builder.append("http://127.0.0.1:8888/SiteWatcher/rest/v1/site/{id}");
        builder.append("</a><br>");

        builder.append("<a href=http://127.0.0.1:8888/SiteWatcher/rest/v1/category>");
        builder.append("http://127.0.0.1:8888/SiteWatcher/rest/v1/category");
        builder.append("</a><br>");

        builder.append("<a href=http://127.0.0.1:8888/SiteWatcher/rest/v1/category/{id}>");
        builder.append("http://127.0.0.1:8888/SiteWatcher/rest/v1/category/{id}");
        builder.append("</a><br>");

        builder.append("<a href=http://127.0.0.1:8888/SiteWatcher/rest/v1/article>");
        builder.append("http://127.0.0.1:8888/SiteWatcher/rest/v1/article");
        builder.append("</a><br>");

        builder.append("<a href=http://127.0.0.1:8888/SiteWatcher/rest/v1/article/{id}>");
        builder.append("http://127.0.0.1:8888/SiteWatcher/rest/v1/article/{id}");
        builder.append("</a><br>");

        builder.append("<a href=http://127.0.0.1:8888/SiteWatcher/rest/v1/article/{id}/revision>");
        builder.append("http://127.0.0.1:8888/SiteWatcher/rest/v1/article/{id}/revision");
        builder.append("</a><br>");
        builder.append("<a href=http://127.0.0.1:8888/SiteWatcher/rest/v1/article/{id}/diffwithRevision/{idRevision}");
        builder.append("http://127.0.0.1:8888/SiteWatcher/rest/v1/article/{id}/diffwithRevision/{idRevision}");
        builder.append("</a><br>");

        builder.append("<a href=http://127.0.0.1:8888/SiteWatcher/rest/v1/title>");
        builder.append("http://127.0.0.1:8888/SiteWatcher/rest/v1/title");
        builder.append("</a><br>");

        builder.append("<a href=http://127.0.0.1:8888/SiteWatcher/rest/v1/title/{id}>");
        builder.append("http://127.0.0.1:8888/SiteWatcher/rest/v1/title/{id}");
        builder.append("</a><br>");

        builder.append("</body></html>");

        out.println(builder);
    }


}
