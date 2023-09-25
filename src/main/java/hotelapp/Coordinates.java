package hotelapp;

import com.google.gson.annotations.SerializedName;

//Class Coordinates
public class Coordinates {

    @SerializedName(value = "lat")
    final private double latitude;

    @SerializedName(value = "lng")
    final private double longitude;

    /**
     * Class Coordinates
     *
     * @param latitude  of the Hotel
     * @param longitude of the Hotel
     **/
    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Getters
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     * toString
     *
     * @return string representation of the Coordinates
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
