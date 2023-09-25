package hotelapp;

import com.google.gson.annotations.SerializedName;

//Class Reviews
public class Hotel {

    @SerializedName(value = "f")
    final private String name;

    @SerializedName(value = "id")
    final private String hotelID;

    @SerializedName(value = "ll")
    final private Coordinates coordinates;

    @SerializedName(value = "ad")
    final private String street;

    @SerializedName(value = "ci")
    final private String city;

    @SerializedName(value = "pr")
    final private String state;

    @SerializedName(value = "c")
    final private String country;

    /**
     * Class Hotel
     *
     * @param name        name of the hotel
     * @param coordinates Coordinates of the Hotel
     * @param street      Name of the street where the Hotel is located
     * @param city        Name of the city where the Hotel is located
     * @param state       Name of the state where the Hotel is located
     * @param country     Name of the country where the Hotel is located
     */
    public Hotel(String name, String hotelID, Coordinates coordinates, String street, String city, String state, String country) {
        this.name = name;
        this.hotelID = hotelID;
        this.coordinates = coordinates;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    //Getters
    public String getName() {
        return name;
    }

    public String getHotelID() {
        return hotelID;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }


    /**
     * toString
     *
     * @return string representation of the Hotel
     */
/*    @Override
    public String toString() {
        return "{" + System.lineSeparator() +
                "\"success\": true," + System.lineSeparator()+
                "\"hotelId\": " + "\"" + hotelID + "\"" + "," + System.lineSeparator() +
                "\"name\": " + "\"" + name + "\"" + ","+ System.lineSeparator() +
                "\"addr\": " + "\"" + street + "\"" + ","+System.lineSeparator() +
                "\"city\": " + "\"" + city + "\"" + ","+System.lineSeparator() +
                "\"state\": " + "\"" + state + "\"" + ","+System.lineSeparator() +
                "\"lat\": " + "\"" + coordinates.getLatitude() + "\""+ ","+ System.lineSeparator() +
                "\"lng\": " + "\"" + coordinates.getLongitude() + "\"" +","+ System.lineSeparator() +
                "}";
    }*/
    @Override
    public String toString() {
        return "Hotelname->" + name + System.lineSeparator() +
                "HotelID->" + hotelID + System.lineSeparator() +
                "Latitude->" + coordinates.getLatitude() + System.lineSeparator() +
                "Longitude->" + coordinates.getLongitude() + System.lineSeparator() +
                "Address->" + street + ", " + city + ", " + state + ", " + country;
    }


}


