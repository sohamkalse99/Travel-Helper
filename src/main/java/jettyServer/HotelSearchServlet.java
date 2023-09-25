package jettyServer;

import hotelapp.DatabaseHandler;
import hotelapp.Hotel;
import hotelapp.ThreadSafeHotelReviewData;
import org.apache.commons.text.StringEscapeUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//class HotelSearchServlet
public class HotelSearchServlet extends HttpServlet {

    /**
     * A method that gets executed when the get request is sent to the
     * HotelSearchServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String hotelId = request.getParameter("hotelId");
        String hotelName = request.getParameter("hotelName");
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        hotelName = StringEscapeUtils.escapeHtml4(hotelName);

        String username = (String) session.getAttribute("username");

        /*DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
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
        }*/

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        List<String> hotelObjectList = (List<String>) session.getAttribute("hotelObjectList");
        PrintWriter out = response.getWriter();
        VelocityEngine velocity = (VelocityEngine) getServletContext().getAttribute("VelocityTemplateEngine");

        VelocityContext context = new VelocityContext();
        context.put("servletPath", request.getServletPath());
        context.put("hotelObjectList", hotelObjectList);

        StringWriter writer = new StringWriter();
        Template template = velocity.getTemplate("templates/hotelSearch.html");
        template.merge(context, writer);

        Object obj = session.getAttribute("username");

        if (obj != null)
            out.println(writer);
        else
            response.sendRedirect("/login");

    }

    /**
     * A method that handles POST request when request is sent to
     * HotelSearchServlet
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("hotelname");
        keyword = StringEscapeUtils.escapeHtml4(keyword);

        ThreadSafeHotelReviewData threadSafeHotelReviewData = (ThreadSafeHotelReviewData) getServletContext().getAttribute("data");

        Set<String> hotelIdAsSet = threadSafeHotelReviewData.getHotelIdAsSet();

//        List<Hotel> hotelObjectList = threadSafeHotelReviewData.findHotelObjectAsList(hotelIdAsSet, keyword);
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        Set<Hotel> hotelSet = dbHandler.getHotelObjectSet();
        List<Hotel> hotelObjectList = getHotelObjectList(hotelSet, keyword);

        request.getSession().setAttribute("hotelObjectList", hotelObjectList);
        response.sendRedirect("/hotelSearch");

    }

    public List<Hotel> getHotelObjectList(Set<Hotel> hotelSet, String keyword){
        List<Hotel> hotelObjectList = new ArrayList<>();

        for(Hotel h: hotelSet){
            if (keyword.equals("")) {
                hotelObjectList.add(h);
            } else if (h.getName().toLowerCase().replaceAll("[^a-zA-Z ]", "").contains(keyword.toLowerCase())) {
                hotelObjectList.add(h);
            }
        }
        return hotelObjectList;
    }

}
