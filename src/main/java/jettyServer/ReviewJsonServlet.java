package jettyServer;

import hotelapp.JsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

//class ReviewJsonServlet
public class ReviewJsonServlet extends HttpServlet {

    public static final int LIMIT = 10;

    /**
     * A method that gets executed when the get request is sent to the
     * ReviewJsonServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        String hotelId = request.getParameter("hotelId");
        String isNext = request.getParameter("isNext");
        String isOnClick = request.getParameter("isOnClick");
        int offset;

        HttpSession session = request.getSession();

        Object result = session.getAttribute("offset");
        if (result == null) {
            offset = 0;
        } else if ((int) result == 0 && isOnClick.equals("false")) {
                offset = 10;
        }
        else {
            offset = (int) result;
        }
        PrintWriter out = response.getWriter();

        JsonBuilder jsonBuilder = new JsonBuilder();
        String jsonData = jsonBuilder.buildJsonData(hotelId, LIMIT, offset, session, isOnClick, isNext);

        out.println(jsonData);
    }
}
