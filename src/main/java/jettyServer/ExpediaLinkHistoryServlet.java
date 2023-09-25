package jettyServer;

import hotelapp.DatabaseHandler;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

//class ExpediaLinkHistoryServlet
public class ExpediaLinkHistoryServlet extends HttpServlet {

    /**
     * A method that gets executed when the get request is sent to the
     * ExpediaLinkHistoryServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String username = (String)session.getAttribute("username");
        String link = request.getParameter("link");
        link = StringEscapeUtils.escapeHtml4(link);

        /*DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        if(link!=null){

            List<String> tableList = databaseHandler.getTableData();
            boolean tableExists = false;
            for(String table:tableList){
                if(table.equals("expediaHistory"))
                    tableExists =true;
            }

            if(!tableExists)
                databaseHandler.createExpediaHistory();

            databaseHandler.storeExpediaLink(username, link);
        }*/

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        VelocityEngine velocity = (VelocityEngine) getServletContext().getAttribute("VelocityTemplateEngine");
        VelocityContext context = new VelocityContext();

        List<String> links = databaseHandler.getExpediaLinkHistory(username);
        if(!links.isEmpty())
            context.put("links", links);

        Template template = velocity.getTemplate("templates/showHistory.html");

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        if(username!=null)
            out.println(writer);
        else
            response.sendRedirect("/login");
    }
}
