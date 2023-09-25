package jettyServer;

import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

//class HotelServer
public class HotelServer {

    public static final int PORT = 8081;

    private Object data;

    /**
     * Class HotelServer
     *
     * @param data
     */
    public HotelServer(Object data) {
        this.data = data;
    }

    /**
     * Function that starts the server
     *
     * @throws Exception throws exception if access failed
     */
    public void start() throws Exception {
        Server server = new Server(PORT);

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        servletContextHandler.setAttribute("data", data);
        servletContextHandler.addServlet(LoginServlet.class, "/login");
//        servletContextHandler.addServlet(LoginServlet.class, "/");
        servletContextHandler.addServlet(RegistrationServlet.class, "/register");
        servletContextHandler.addServlet(HomeServlet.class, "/home");
        servletContextHandler.addServlet(HotelSearchServlet.class, "/hotelSearch");
        servletContextHandler.addServlet(HotelInfoServlet.class, "/hotelInfo");
        servletContextHandler.addServlet(AddReviewServlet.class, "/addReview");
        servletContextHandler.addServlet(EditReviewServlet.class, "/editReview");
        servletContextHandler.addServlet(DeleteServlet.class, "/delete");
        servletContextHandler.addServlet(LogoutServlet.class, "/logout");
        servletContextHandler.addServlet(ReviewJsonServlet.class, "/jsonReview");
        servletContextHandler.addServlet(WeatherJsonServlet.class, "/jsonWeather");
        servletContextHandler.addServlet(InsertExpediaLinksServlet.class, "/insertLinks");
        servletContextHandler.addServlet(ExpediaLinkHistoryServlet.class, "/showHistory");
        servletContextHandler.addServlet(ClearHistoryServlet.class, "/clearHistory");
        servletContextHandler.addServlet(InsertFavoritesServlet.class, "/insertFavorites");
        servletContextHandler.addServlet(ShowFavoritesServlet.class, "/showFavorites");
        servletContextHandler.addServlet(ClearFavoritesServlet.class, "/clearFavorites");

        VelocityEngine velocity = new VelocityEngine();
        velocity.init();

        servletContextHandler.setAttribute("VelocityTemplateEngine", velocity);

        ResourceHandler resource_handler = new ResourceHandler(); // a handler for serving static pages
        resource_handler.setDirectoriesListed(true);

        resource_handler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, servletContextHandler });

        server.setHandler(handlers);

        server.start();
        server.join();
    }

/*	public static void main(String[] args) throws Exception {
		Server server = new Server(PORT);
		// FILL IN CODE, and add more classes as needed

		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.addServlet(LoginServlet.class, "/login");
		servletContextHandler.addServlet(RegistrationServlet.class, "/register");
		servletContextHandler.addServlet(HomeServlet.class, "/home");
		VelocityEngine velocity = new VelocityEngine();
		velocity.init();

		servletContextHandler.setAttribute("VelocityTemplateEngine", velocity);
		server.setHandler(servletContextHandler);

		server.start();
		server.join();
//		servletContextHandler.addServlet(RegistrationServlet.class, "/registration");
	}*/
}