package jettyServer;

import hotelapp.DatabaseHandler;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//class RegistrationServlet
public class RegistrationServlet extends HttpServlet {

    /**
     * A method that gets executed when the get request is sent to the
     * RegistrationServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();

        VelocityEngine velocity = (VelocityEngine) request.getServletContext().getAttribute("VelocityTemplateEngine");
        VelocityContext context = new VelocityContext();

        context.put("servletPath", request.getServletPath());
        Template template = velocity.getTemplate("templates/registrationPage.html");
        StringWriter writer = new StringWriter();
        String userExist = request.getParameter("userExists");
        userExist = StringEscapeUtils.escapeHtml4(userExist);
        if (userExist != null && !userExist.isEmpty()) {
            if (userExist.equals("true")) {
                context.put("userExist", true);
            }
        }
        template.merge(context, writer);
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("username");

        if (obj != null)
            response.sendRedirect("/home");
        else
            out.println(writer);
    }

    /**
     * Returns true if user enters correct values for username
     *
     * @param username
     * @return true if the pattern is matched
     */
    public boolean checkUsername(String username) {
        String regex = "[A-Za-z][A-Za-z \\._\\d]{4,17}";

        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(username);

        if (m.matches()) {
            return true;
        } else
            return false;

    }

    /**
     * Returns true if user enters correct values for password
     *
     * @param password
     * @return true if the pattern is matched
     */
    public boolean checkPassword(String password) {
//        String regex = "(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%])(?=.{8,})"; //add underscore
        String regex = "^(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%_]).{8,}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);

        if (m.find()) {
            return true;
        } else
            return false;
    }

    /**
     * A method that handles POST request when request is sent to
     * RegistrationServlet
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        name = StringEscapeUtils.escapeHtml4(name);
        password = StringEscapeUtils.escapeHtml4(password);

        PrintWriter out = response.getWriter();
        name = StringEscapeUtils.escapeHtml4(name);
        boolean isUserNameValid = checkUsername(name);

        password = StringEscapeUtils.escapeHtml4(password);
        boolean isPasswordValid = checkPassword(password);

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        List<String> usersList = databaseHandler.getUsersList();

        if (!usersList.contains(name)) {
            if (isUserNameValid && isPasswordValid) {
                databaseHandler.registerUser(name, password);
                response.sendRedirect("/login?registrationSuccess=true");
            } else {
                response.sendRedirect("/registration?success=false");
            }

        } else {
            response.sendRedirect("/register?userExists=true");
        }

    }
}
