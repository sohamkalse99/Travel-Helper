package jettyServer;

import hotelapp.DatabaseHandler;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

//class InsertExpediaLinksServlet
public class InsertExpediaLinksServlet extends HttpServlet {

    /**
     * A method that gets executed when the get request is sent to the
     * InsertExpediaLinksServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String)session.getAttribute("username");
        String link = request.getParameter("link");
        link = StringEscapeUtils.escapeHtml4(link);

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
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
        }


    }
}
