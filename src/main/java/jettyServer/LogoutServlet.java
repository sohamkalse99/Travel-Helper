package jettyServer;

import hotelapp.DatabaseHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//class LogoutServlet
public class LogoutServlet extends HttpServlet {

    /**
     * A method that gets executed when the get request is sent to the
     * LogoutServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String)session.getAttribute("username");
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        boolean flag = databaseHandler.checkLastLoginTable(username);
        if(!flag){
            databaseHandler.insertIntoLastLoginTable(username);
        }

        databaseHandler.updateLastLoginTable(username);
        request.getSession().invalidate();
        response.sendRedirect("/login");
    }
}
