package jettyServer;

import hotelapp.DatabaseHandler;
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

//class LoginServlet
public class LoginServlet extends HttpServlet {

    /**
     * A method that gets executed when the get request is sent to the
     * LoginServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session = request.getSession();


        PrintWriter out = response.getWriter();
        VelocityEngine velocity = (VelocityEngine) request.getServletContext().getAttribute("VelocityTemplateEngine");
        VelocityContext context = new VelocityContext();

        String usernamePasswordValidation = request.getParameter("usernameOrPasswordInvalid");
        usernamePasswordValidation = StringEscapeUtils.escapeHtml4(usernamePasswordValidation);

        String registrationSuccess = request.getParameter("registrationSuccess");
        registrationSuccess = StringEscapeUtils.escapeHtml4(registrationSuccess);

        if (registrationSuccess != null && !registrationSuccess.isEmpty())
            context.put("registrationSuccess", true);
        if (usernamePasswordValidation != null && !usernamePasswordValidation.isEmpty())
            context.put("usernamePasswordValidation", true);

        context.put("loginServlet", request.getServletPath());

//        System.out.println(request.getServletPath());


        Template template = velocity.getTemplate("templates/loginPage.html");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        Object obj = session.getAttribute("username");

        if (obj != null)
            response.sendRedirect("/home");
        else
            out.println(writer.toString());
//        session.setMaxInactiveInterval(60);//60secs
    }

    /**
     * A method that handles POST request when request is sent to
     * LoginServlet
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("name");
        String password = request.getParameter("password");

        username = StringEscapeUtils.escapeHtml4(username);
        password = StringEscapeUtils.escapeHtml4(password);

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        boolean isValid = databaseHandler.authenticateUser(username, password);

        if (isValid) {
//            out.println("Authentication successful");
            session.setAttribute("username", username);
            response.sendRedirect("/home");
        } else {
//            response.sendRedirect("/login?success="+ isValid);
            response.sendRedirect("/login?usernameOrPasswordInvalid=true");
        }
    }
}
