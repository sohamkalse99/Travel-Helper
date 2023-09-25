package jettyServer;

import hotelapp.DatabaseHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//class ClearHistoryServlet
public class ClearHistoryServlet extends HttpServlet {

    /**
     * A method that gets executed when the get request is sent to the
     * ClearHistoryServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        databaseHandler.clearExpediaLinkHistory(username);

        response.sendRedirect("/showHistory");
    }
}
