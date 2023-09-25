package jettyServer;

import hotelapp.DatabaseHandler;
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

//class ShowFavoritesServlet
public class ShowFavoritesServlet extends HttpServlet {

    /**
     * A method that gets executed when the get request is sent to the
     * ShowFavoritesServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String username = (String)session.getAttribute("username");

/*        if(hotelName!=null){

            List<String> tableList = databaseHandler.getTableData();
            boolean tableExists = false;
            for(String table:tableList){
                if(table.equals("favoriteHotel"))
                    tableExists =true;
            }

            if(!tableExists)
                databaseHandler.createFavoritesTable();

            isDuplicate = databaseHandler.storeFavorites(username, hotelId, hotelName);
        }*/
        VelocityEngine velocity = (VelocityEngine) getServletContext().getAttribute("VelocityTemplateEngine");
        VelocityContext context = new VelocityContext();

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

        List<String> favoriteHotels = databaseHandler.getFavoriteHotels(username);
        if(!favoriteHotels.isEmpty())
            context.put("favoriteHotels", favoriteHotels);

//        context.put("isDuplicate", isDuplicate);
        Template template = velocity.getTemplate("templates/showFavoriteHotels.html");

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        if(username!=null)
            out.println(writer);
        else
            response.sendRedirect("/login");
    }
}
