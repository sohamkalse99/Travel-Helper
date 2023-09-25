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
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

//class HomeServlet
public class HomeServlet extends HttpServlet {

    /**
     * A method that gets executed when the get request is sent to the
     * HomeServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        ThreadSafeHotelReviewData threadSafeHotelReviewData = (ThreadSafeHotelReviewData) getServletContext().getAttribute("data");

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        List<String> tableList = databaseHandler.getTableData();
        boolean tableExists = false;
        for(String table:tableList){
            if(table.equals("lastlogin"))
                tableExists =true;
        }

        if(!tableExists)
            databaseHandler.createLastLoginTable();

        String username = (String) session.getAttribute("username");
        String dateAndTime = databaseHandler.fetchLastLoginFromTable(username);
        String date="";
        String time="";
        if(!dateAndTime.isEmpty()){
            date = dateAndTime.substring(0, dateAndTime.indexOf("T"));
            time = dateAndTime.substring(dateAndTime.indexOf("T")+1, dateAndTime.indexOf("Z"));
        }

        VelocityEngine velocity = (VelocityEngine) getServletContext().getAttribute("VelocityTemplateEngine");

        VelocityContext context = new VelocityContext();
        context.put("servletPath", request.getServletPath());
        context.put("date", date);
        context.put("time", time);
        context.put("username", username);
        Template template = velocity.getTemplate("templates/home.html");
        StringWriter writer = new StringWriter();

        template.merge(context, writer);

        Object obj = session.getAttribute("username");

        if (obj != null)
            out.println(writer);
        else
            response.sendRedirect("/login");

    }

/*    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        ThreadSafeHotelReviewData threadSafeHotelReviewData = (ThreadSafeHotelReviewData) getServletContext().getAttribute("data");

        String keyword = request.getParameter("hotelname");
        keyword = StringEscapeUtils.escapeHtml4(keyword);
        Set<String> hotelIdAsSet = threadSafeHotelReviewData.getHotelIdAsSet();

        List<Hotel> hotelObjectList = threadSafeHotelReviewData.findHotelObjectAsList(hotelIdAsSet, keyword);
        request.getSession().setAttribute("hotelObjectList", hotelObjectList);
        response.sendRedirect("/hotelSearch");
    }*/
}
