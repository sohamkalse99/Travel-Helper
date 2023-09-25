package jettyServer;

import hotelapp.DatabaseHandler;
import hotelapp.Hotel;
import hotelapp.ThreadSafeHotelReviewData;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.server.session.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

//class AddReviewServlet
public class AddReviewServlet extends HttpServlet {

    /**
     * A method that gets executed when the get request is sent to the
     * AddReviewServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        String hotelId = request.getParameter("hotelId");

        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        VelocityEngine velocity = (VelocityEngine) getServletContext().getAttribute("VelocityTemplateEngine");
        VelocityContext context = new VelocityContext();
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("username");

        context.put("addReviewServlet", request.getServletPath());
        context.put("hotelId", hotelId);
        Template template = velocity.getTemplate("templates/addReview.html");
        StringWriter writer = new StringWriter();

        template.merge(context, writer);
        if (obj != null)
            out.println(writer);
        else
            response.sendRedirect("/login");

    }

    /**
     * A method that handles POST request when request is sent to
     * AddReviewServlet
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String hotelId = request.getParameter("hotelId");
        String title = request.getParameter("title");
        String reviewText = request.getParameter("reviewText");
        String ratingOverall = request.getParameter("ratingOverall");
        HttpSession session = request.getSession();
        ThreadSafeHotelReviewData threadSafeHotelReviewData = (ThreadSafeHotelReviewData) getServletContext().getAttribute("data");

        String username = (String) session.getAttribute("username");

        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        title = StringEscapeUtils.escapeHtml4(title);
        reviewText = StringEscapeUtils.escapeHtml4(reviewText);
        ratingOverall = StringEscapeUtils.escapeHtml4(ratingOverall);

//        Hotel hotel = threadSafeHotelReviewData.getHotelObject(hotelId);
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        Hotel hotel =databaseHandler.getHotelObject(hotelId);

        if (hotel != null) {
//            threadSafeHotelReviewData.storeReview(hotelId, ratingOverall, title, reviewText, username);
            databaseHandler.insertInReviewsTable(hotelId, ratingOverall, title, reviewText, username);
            response.sendRedirect("/hotelInfo?hotelId=" + hotelId);
        } else {
            response.sendRedirect("addReview?hotelId=incorrect");
        }
    }
}
