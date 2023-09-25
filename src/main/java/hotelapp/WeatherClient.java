package hotelapp;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//class WeatherClient
public class WeatherClient {

    /**
     *
     * Removes header and returns jsonObject
     *
     * @param jsonData
     * @param hotel
     *
     * @return JsonObject which will contain temperature and windspeed
     */
    public String parseAPI(String jsonData, Hotel hotel) {

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(jsonData);
        JsonObject obj = jsonObject.getAsJsonObject("current_weather");

        JsonElement temperatureElement = obj.get("temperature");
        JsonElement windSpeedElement = obj.get("windspeed");

        JsonObject weatherObject = new JsonObject();

/*        if (hotelMap.containsKey(hotelId)) {
            weatherObject.addProperty("hotelId", hotelMap.get(hotelId).getHotelID());
            weatherObject.addProperty("name", hotelMap.get(hotelId).getName());
            weatherObject.add("temperature", temperatureElement);
            weatherObject.add("Wind Speed", windSpeedElement);
        }*/

        weatherObject.addProperty("hotelId", hotel.getHotelID());
        weatherObject.addProperty("name", hotel.getName());
        weatherObject.add("temperature", temperatureElement);
        weatherObject.add("windspeed", windSpeedElement);

        return weatherObject.toString();

    }

    /**
     *
     * Creates url string, creates json object and returns it in string format
     *
     * @param latitude
     * @param longitude
     * @param hotel
     *
     * @return JsonObject
     */
    public String getWeatherData(String latitude, String longitude, Hotel hotel){
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current_weather=true";
        HttpFetcher httpFetcher = new HttpFetcher();
        String jsonResultWithHeader = httpFetcher.getData(urlString);
        String jsonResult = jsonResultWithHeader.substring(jsonResultWithHeader.indexOf("{"));

        String jsonObject = parseAPI(jsonResult, hotel);

        return jsonObject;
    }
}
