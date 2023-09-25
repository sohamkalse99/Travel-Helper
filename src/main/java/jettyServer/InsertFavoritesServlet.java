package jettyServer;

import hotelapp.DatabaseHandler;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

//class InsertFavoritesServlet
public class InsertFavoritesServlet extends HttpServlet {

    /**
     * A method that gets executed when the get request is sent to the
     * InsertFavoritesServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String username = (String)session.getAttribute("username");
        String hotelId = request.getParameter("hotelId");
        String hotelName = request.getParameter("hotelName");
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);

        hotelName = StringEscapeUtils.escapeHtml4(hotelName);

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        boolean isDuplicate = false;
        if(hotelName!=null){

            List<String> tableList = databaseHandler.getTableData();
            boolean tableExists = false;
            for(String table:tableList){
                if(table.equals("favoriteHotel"))
                    tableExists =true;
            }

            if(!tableExists)
                databaseHandler.createFavoritesTable();

            isDuplicate = databaseHandler.storeFavorites(username, hotelId, hotelName);
        }

        out.println(isDuplicate);
    }
}
