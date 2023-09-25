package jettyServer;

import hotelapp.DatabaseHandler;
import hotelapp.ThreadSafeHotelReviewData;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.jetty.server.session.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//class DeleteServlet
public class DeleteServlet extends HttpServlet {

    /**
     * A method that gets executed when the get request is sent to the
     * DeleteServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        String hotelId = request.getParameter("hotelId");
        String reviewId = request.getParameter("reviewId");
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        reviewId = StringEscapeUtils.escapeHtml4(reviewId);

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        databaseHandler.deleteReview(hotelId, reviewId);

/*        ThreadSafeHotelReviewData threadSafeHotelReviewData = (ThreadSafeHotelReviewData) getServletContext().getAttribute("data");
        threadSafeHotelReviewData.deleteReviewFromMap(hotelId, reviewId);*/

        HttpSession session = request.getSession();
        Object obj = session.getAttribute("username");

        if (obj != null)
            response.sendRedirect("/hotelInfo?hotelId=" + hotelId);

        else
            response.sendRedirect("/login");
    }
}
