package jettyServer;

import hotelapp.Coordinates;
import hotelapp.DatabaseHandler;
import hotelapp.Hotel;
import hotelapp.WeatherClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//class ShowFavoritesServlet
public class WeatherJsonServlet extends HttpServlet {

    /**
     * A method that gets executed when the get request is sent to the
     * WeatherJsonServlet
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        String hotelId = request.getParameter("hotelId");

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        Hotel hotel = databaseHandler.getHotelObject(hotelId);
        Coordinates coordinates = databaseHandler.getCoordinates(hotelId);
        WeatherClient weatherClient = new WeatherClient();
        String jsonObject = weatherClient.getWeatherData(Double.toString(coordinates.getLatitude()), Double.toString(coordinates.getLongitude()), hotel);

        out.println(jsonObject);
    }
}
