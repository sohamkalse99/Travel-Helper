package jettyServer;

import hotelapp.*;
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
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

//class HotelInfoServlet
public class HotelInfoServlet extends HttpServlet {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * A method that gets executed when the get request is sent to the
     * HotelInfoServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        String hotelId = request.getParameter("hotelId");
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
/*        String link = request.getParameter("link");
        link = StringEscapeUtils.escapeHtml4(link);
        System.out.println(link);*/
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("username");
        String user = (String) session.getAttribute("username");

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        Hotel hotel = databaseHandler.getHotelObject(hotelId);
        ThreadSafeHotelReviewData threadSafeHotelReviewData = (ThreadSafeHotelReviewData) getServletContext().getAttribute("data");
//        Hotel hotel = threadSafeHotelReviewData.getHotelObject(hotelId);
//        Set<Review> reviewSet = threadSafeHotelReviewData.getReviewSet(hotelId);
//        Set<Review> reviewSet = databaseHandler.getReviewObjectSet(hotelId);
//        double avgRating = threadSafeHotelReviewData.getAverageRating(hotelId);
        double avgRating = getAverageRating(hotelId);

//        String expediaLink = threadSafeHotelReviewData.generateLink(hotelId);
        String expediaLink = generateLink(hotelId);
//        List<String> userList = threadSafeHotelReviewData.checkReviewByUsername(reviewSet, user);

/*        for(String u: userList){
            System.out.println(u);
        }*/



        VelocityEngine velocity = (VelocityEngine) getServletContext().getAttribute("VelocityTemplateEngine");

        Template template = velocity.getTemplate("templates/hotelInfo.html");

        VelocityContext context = new VelocityContext();

/*
        JsonBuilder jsonBuilder = new JsonBuilder();
        String jsonData = jsonBuilder.buildJsonData(hotelId);
*/

        context.put("hotel", hotel);
//        context.put("servletPath", request.getServletPath());
//        context.put("reviewSet", reviewSet);
        context.put("avgRating", avgRating);
        context.put("expediaLink", expediaLink);
        context.put("user", user);

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        if (obj != null)
            out.println(writer);
        else
            response.sendRedirect("/login");

    }

    public double getAverageRating(String hotelId){
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        Set<Review> reviewSet = databaseHandler.getReviewObjectSet(hotelId);
        double avgRating = 0.0;
        if (reviewSet != null) {
            for (Review review : reviewSet) {
                avgRating += review.getRatingOverall();
            }

            avgRating = avgRating / reviewSet.size();
        }

        return Double.parseDouble(df.format(avgRating));

    }

    public String generateLink(String hotelId) {
        String link = "https://www.expedia.com/";
        String hotelName = "";
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        Hotel hotel = databaseHandler.getHotelObject(hotelId);
        if (!hotelId.equals("null"))
            hotelName = hotel.getName();

        link = link + hotelName + ".h" + hotelId + ".Hotel-Information";
        return link;
    }
    }
