package jettyServer;

import hotelapp.DatabaseHandler;
import hotelapp.Review;
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
import java.util.Set;

//class EditReviewServlet
public class EditReviewServlet extends HttpServlet {

    /**
     * A method that gets executed when the get request is sent to the
     * EditReviewServlet
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        String hotelId = request.getParameter("hotelId");
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        String title = "";
        String text = "";
        String reviewId = request.getParameter("reviewId");
        reviewId = StringEscapeUtils.escapeHtml4(reviewId);

        HttpSession session = request.getSession();
        Object obj = session.getAttribute("username");
        if (obj == null) {
            response.sendRedirect("/login");
        } else {
            DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
            ThreadSafeHotelReviewData threadSafeHotelReviewData = (ThreadSafeHotelReviewData) getServletContext().getAttribute("data");
            Set<Review> reviewSet = null;
            if (hotelId != null) {
                reviewSet = databaseHandler.getReviewObjectSet(hotelId);
//                reviewSet = threadSafeHotelReviewData.getReviewSet(hotelId);
            }
            Review review = null;
            if (reviewSet != null) {
                for (Review rev : reviewSet) {
                    if (rev.getReviewId().equals(reviewId)) {
                        review = rev;
                    }
                }
            }

            PrintWriter out = response.getWriter();

            VelocityEngine velocity = (VelocityEngine) getServletContext().getAttribute("VelocityTemplateEngine");

            VelocityContext context = new VelocityContext();
/*        context.put("hotelId", hotelId);
        context.put("title", title);
        context.put("text", text);
        context.put("reviewId", reviewId);*/
            context.put("review", review);
            context.put("servletPath", request.getServletPath());

            Template template = velocity.getTemplate("templates/editReview.html");
            StringWriter writer = new StringWriter();
            template.merge(context, writer);

            out.println(writer);
        }

    }

    /**
     * A method that handles POST request when request is sent to
     * EditReviewServlet
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String reviewText = request.getParameter("reviewText");
        String reviewId = request.getParameter("reviewId");
        String hotelId = request.getParameter("hotelId");
        String rating = request.getParameter("rating");

        title = StringEscapeUtils.escapeHtml4(title);
        reviewText = StringEscapeUtils.escapeHtml4(reviewText);
        reviewId = StringEscapeUtils.escapeHtml4(reviewId);
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        rating = StringEscapeUtils.escapeHtml4(rating);

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        databaseHandler.editReview(hotelId, reviewId, title, reviewText, rating);
        ThreadSafeHotelReviewData threadSafeHotelReviewData = (ThreadSafeHotelReviewData) getServletContext().getAttribute("data");
        Set<Review> reviewSet = threadSafeHotelReviewData.getReviewSet(hotelId);
        /*double ratingOverall = 0.0;
        for (Review review : reviewSet) {
            if (review.getReviewId().equals(reviewId))
                ratingOverall = review.getRatingOverall();
        }


        //threadSafeHotelReviewData.deleteReview(reviewId, reviewSet);
        threadSafeHotelReviewData.deleteReviewFromMap(hotelId, reviewId);
        threadSafeHotelReviewData.storeReview(hotelId, String.valueOf(ratingOverall), title, reviewText, username);*/

        response.sendRedirect("/hotelInfo?hotelId=" + hotelId);
    }
}
