package hotelapp;

import com.google.gson.*;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

//class HotelReviewData
public class HotelReviewData {

    private Map<String, Set<Review>> reviewsMap = new HashMap<>();
    private Map<String, Hotel> hotelMap = new TreeMap<>();

    private Set<String> reviewIdHashSet = new HashSet<>();
    private Map<String, Set<ReviewWithFrequency>> frequencyWordMap = new HashMap<>();

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private Random random = new Random();

    public HotelReviewData() {

    }

    /**
     * buildHotelMap Method
     *
     * @param hotelArray is an array of type Hotel
     *                   Method is used to create map of key hotel id and value of type Hotel
     */
    public void buildHotelMap(Hotel[] hotelArray) {

        try {
            for (Hotel h : hotelArray) {
                hotelMap.put(h.getHotelID(), h);
            }
        } catch (Exception e) {
            System.out.println("Cannot build hotel map");
        }


    }

    /**
     * buildReviewsMap Method
     *
     * @param reviewsList is a list of type Review
     *                    Method is used to create map of key hotel id and value of Set of type Reviews
     */
    public void buildReviewsMap(List<Review> reviewsList) {

        for (Review review : reviewsList) {
            if (reviewsMap.containsKey(review.getHotelId())) {
                Set<Review> set = reviewsMap.get(review.getHotelId());
                set.add(review);
                reviewIdHashSet.add(review.getReviewId());
            } else {
                Comparator<Review> comp = new ComparatorByDate();
                Set<Review> reviewsSet = new TreeSet<>(comp);
                reviewsSet.add(review);
                reviewIdHashSet.add(review.getReviewId());
                reviewsMap.put(review.getHotelId(), reviewsSet);
            }

        }

    }

    /**
     * computeFrequency Method
     *
     * @param word    is a word in reviewText
     * @param wordset is an array of type String
     *                Method is used to calculate frequency of particular word in a reviewText
     */
    private int computeFrequency(String word, String[] wordset) {
        int frequency = 0;
        word = word.toLowerCase();

        for (String w : wordset) {
            w = w.toLowerCase().replaceAll("[^a-zA-Z ]", "");
            if (w.equals(word))
                frequency++;
        }

        return frequency;
    }

    /**
     * buildWordMap Method
     *
     * @param reviewsList is a list of type Review
     *                    Method is used to create map which has a key word and value is Set of type ReviewWithFrequency
     */
    public void buildWordMap(List<Review> reviewsList) {

        for (Review review : reviewsList) {

            String[] reviewTextWords = review.getReviewText().split(" ");

            for (String text : reviewTextWords) {
                text = text.toLowerCase().replaceAll("[^a-zA-Z ]", "");

                if (!StopWords.contains(text)) {

                    int frequency = computeFrequency(text, reviewTextWords);
                    ReviewWithFrequency reviewWithFrequency = new ReviewWithFrequency(review, frequency, text);

                    if (frequencyWordMap.containsKey(text)) {
                        frequencyWordMap.get(text).add(reviewWithFrequency);
                    } else {
                        Comparator<ReviewWithFrequency> comp = new ComparatorByFrequency();
                        Set<ReviewWithFrequency> frequencySet = new TreeSet<>(comp);
                        frequencySet.add(reviewWithFrequency);
                        frequencyWordMap.put(text, frequencySet);
                    }
                }
            }
        }
    }

    /**
     * printHotelDetails Method
     *
     * @param hotelDetails is a variable of type String
     *                     Method is used to print hotel details
     */
    public void printHotelDetails(String hotelDetails) {
        System.out.println(System.lineSeparator() + hotelDetails + System.lineSeparator());
    }

    /**
     * findHotelAsJson Method
     *
     * @param hotelId is a variable of type String
     *                Method is used to return Hotel details in json format
     */
    public String findHotelAsJson(String hotelId) {
        String jsonHotelDetails;
        JsonObject jsonObject = new JsonObject();

        if (hotelMap.containsKey(hotelId)) {
            jsonObject.addProperty("success", true);
            jsonObject.addProperty("hotelId", hotelMap.get(hotelId).getHotelID());
            jsonObject.addProperty("name", hotelMap.get(hotelId).getName());
            jsonObject.addProperty("addr", hotelMap.get(hotelId).getStreet());
            jsonObject.addProperty("city", hotelMap.get(hotelId).getCity());
            jsonObject.addProperty("state", hotelMap.get(hotelId).getState());
            jsonObject.addProperty("lat", hotelMap.get(hotelId).getCoordinates().getLatitude());
            jsonObject.addProperty("lng", hotelMap.get(hotelId).getCoordinates().getLongitude());

            return jsonObject.toString();
        } else {
            jsonObject.addProperty("success", false);
            jsonObject.addProperty("hotelId", "invalid");
            return jsonObject.toString();
        }

    }

    /**
     * findHotel Method
     *
     * @param hotelID is a variable of type String
     *                Method takes hotelID and returns string of type hoteldetails
     */
    public String findHotel(String hotelID) {
        String jsonHotelDetails;
        Gson gsonForWriting = new GsonBuilder().setPrettyPrinting().create();

        if (hotelMap.containsKey(hotelID)) {
            return hotelMap.get(hotelID).toString();

        } else
            return "Hotel not found";
    }

    /**
     * printReviews Method
     *
     * @param reviewString is a variable of type String
     *                     Method is used to print all the reviews for a particular hotel
     */
    public void printReviews(String reviewString) {
        System.out.println(reviewString);
    }

    /**
     * findReviews Method
     *
     * @param hotelId is a variable of type String
     *                Method takes hotelID and returns string which has all the reviews for that hotelID
     */
    public String findReviews(String hotelId) {
        String reviewString = "";
        String jsonInString = "";
        JsonObject jsonResponseObject = new JsonObject();
        Gson gsonForWriting = new GsonBuilder().setPrettyPrinting().create();

        if (reviewsMap.containsKey(hotelId)) {
            jsonResponseObject.addProperty(hotelId, reviewsMap.get(hotelId).toString());
            jsonResponseObject.getAsJsonArray(jsonResponseObject.toString());
            for (Review review : reviewsMap.get(hotelId)) {
                reviewString = reviewString + review.toString() + System.lineSeparator();
                jsonInString += gsonForWriting.toJson(review) + "," + System.lineSeparator();
//                jsonResponseObject.ad
            }
        } else
            reviewString = "No Reviews for this hotel";

        System.out.println(jsonInString);
        return reviewString;

    }

    /**
     * findReviewsAsJson Method
     *
     * @param hotelId is a variable of type String
     * @param num     is a variable of type String
     *                Method is used to return review details in json format
     */
    public String findReviewsAsJson(String hotelId, String num) {
        String reviewAsString = "";
        String jsonInString = "";
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        int count = 0;
        Gson gsonForWriting = new GsonBuilder().setPrettyPrinting().create();

        if (reviewsMap.containsKey(hotelId)) {

            jsonObject.addProperty("success", true);
            jsonObject.addProperty("hotelId", hotelId);

            for (Review review : reviewsMap.get(hotelId)) {
                JsonObject reviewObject = new JsonObject();
                reviewObject.addProperty("reviewId", review.getReviewId());
                reviewObject.addProperty("title", review.getTitle());
                reviewObject.addProperty("user", review.getUserNickname());
                reviewObject.addProperty("reviewText", review.getReviewText());
                reviewObject.addProperty("date", review.getReviewSubmissionDate().getMonthValue() + ":" + review.getReviewSubmissionDate().getDayOfMonth() + ":" + review.getReviewSubmissionDate().getYear());

                if (count < Integer.parseInt(num)) {
                    jsonArray.add(reviewObject);
                    count++;

                }
            }

            jsonObject.add("reviews", jsonArray);


        } else {
            jsonObject.addProperty("success", false);
            jsonObject.addProperty("hotelId", "invalid");
            return jsonObject.toString();
        }


        return jsonObject.toString();

    }

    /**
     * findHotelAndReviewsForWriting Method
     *
     * @param hotelId is a variable of type String
     *                Method takes hotelID and returns string which has all the reviews for that hotelID
     */
    public String findHotelAndReviewsForWriting(String hotelId) {
        String outputString = " " + System.lineSeparator() + " ";

        if (hotelMap.containsKey(hotelId)) {
            Hotel hotel = hotelMap.get(hotelId);
            outputString = "********************" + System.lineSeparator() + hotel.getName() + ": " + hotel.getHotelID() + System.lineSeparator() + hotel.getStreet() + System.lineSeparator() + hotel.getCity() + ", " + hotel.getState() + System.lineSeparator();
            if (reviewsMap.containsKey(hotelId)) {
                for (Review review : reviewsMap.get(hotelId)) {
                    outputString = outputString + "--------------------" + System.lineSeparator() + "Review by " + review.getUserNickname() + " on " + review.getReviewSubmissionDate() + System.lineSeparator() + "Rating: " + (int) review.getRatingOverall() + System.lineSeparator() + "ReviewId: " + review.getReviewId() + System.lineSeparator() + review.getTitle() + System.lineSeparator() + review.getReviewText() + System.lineSeparator();
                }
            }
        }
        return outputString;
    }

    /**
     * printWords Method
     *
     * @param reviewWithFrequency is a variable of type String
     *                            Method is used to print all the reviews and frequency for a particular word
     */
    public void printWords(String reviewWithFrequency) {
        System.out.println(reviewWithFrequency);
    }

    /**
     * findWord Method
     *
     * @param word is a variable of type String
     *             Method takes word and returns string which has all the reviews, count for the word which user has entered
     */
    public String findWord(String word) {
        String reviewWithFrequency = "";
        if (frequencyWordMap.containsKey(word)) {

            Set<ReviewWithFrequency> reviewWithFrequencies = frequencyWordMap.get(word);
            for (ReviewWithFrequency r : reviewWithFrequencies) {
                reviewWithFrequency = reviewWithFrequency + r.toString() + System.lineSeparator();

            }

        } else {
            reviewWithFrequency = "Word not found";
        }
        return reviewWithFrequency;
    }

    /**
     * findWordAsJson Method
     *
     * @param word is a variable of type String
     * @param num  is a variable of type String
     *             Method is used to return word details in json format
     */
    public String findWordAsJson(String word, String num) {

        String reviewWithFrequency = "";

        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        int count = 0;

        if (word == null || num == null || word.isEmpty() || word.equals("invalid") || num.equals("invalid")) {
            jsonObject.addProperty("success", false);
            jsonObject.addProperty("word", "invalid");

            return jsonObject.toString();
        }

        if (frequencyWordMap.containsKey(word)) {

            jsonObject.addProperty("success", true);
            jsonObject.addProperty("word", word);

            Set<ReviewWithFrequency> reviewWithFrequencies = frequencyWordMap.get(word);
            for (ReviewWithFrequency r : reviewWithFrequencies) {
                JsonObject wordObject = new JsonObject();
                wordObject.addProperty("reviewId", r.review.getReviewId());
                wordObject.addProperty("title", r.review.getTitle());
                wordObject.addProperty("user", r.review.getUserNickname());
                wordObject.addProperty("reviewText", r.review.getReviewText());
                wordObject.addProperty("date", r.review.getReviewSubmissionDate().getDayOfWeek() + " " + r.review.getReviewSubmissionDate().getMonth() + " " + r.review.getReviewSubmissionDate().getDayOfMonth() + " " + r.review.getReviewSubmissionDate().getYear());

                if (count < Integer.parseInt(num)) {
                    jsonArray.add(wordObject);
                    count++;
                }
            }

            jsonObject.add("reviews", jsonArray);
        } else {

            jsonObject.addProperty("success", false);
            jsonObject.addProperty("hotelId", "invalid");
            return jsonObject.toString();

        }

        return jsonObject.toString();

    }

    /**
     * A method that creates a GET request for the given host and resource
     *
     * @param host
     * @param pathResourceQuery
     * @return HTTP GET request returned as a string
     */
    private static String getRequest(String host, String pathResourceQuery) {
        String request = "GET " + pathResourceQuery + " HTTP/1.1" + System.lineSeparator() // GET
                // request
                + "Host: " + host + System.lineSeparator() // Host header required for HTTP/1.1
                + "Connection: close" + System.lineSeparator() // make sure the server closes the
                + System.lineSeparator();
        return request;
    }

    /**
     * callAPI Method
     *
     * @param latitude  is a variable of type String
     * @param longitude is a variable of type String
     *                  Method is used to call the open mateo api and extract json data
     */
    public static String callAPI(String latitude, String longitude) {
        String s = "";
        String stringWithoutHeader = "";

        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current_weather=true";


        PrintWriter out = null;
        BufferedReader in = null;
        SSLSocket socket = null;
        try {
            URL url = new URL(urlString);

            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

            socket = (SSLSocket) factory.createSocket(url.getHost(), 443);

            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            String request = getRequest(url.getHost(), url.getPath() + "?" + url.getQuery());

            out.println(request); // send the request to the server
            out.flush();

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                if (line.equals("0"))
                    continue;
                sb.append(line);
            }
            s = sb.toString();
            stringWithoutHeader = s.substring(s.indexOf("{"));


        } catch (IOException e) {
            System.out.println(
                    "An IOException occured while writing to the socket stream or reading from the stream: " + e);
        } finally {
            try {
                // close the streams and the socket
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("An exception occured while trying to close the streams or the socket: " + e);
            }
        }

        return stringWithoutHeader;

    }

    /**
     * parseAPI Method
     *
     * @param jsonData is a variable of type String
     * @param hotelId  is a variable of type String
     *                 Method is used to parse json data and extract temperature and windspeed
     */
    public String parseAPI(String jsonData, String hotelId) {

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(jsonData);
        JsonObject obj = jsonObject.getAsJsonObject("current_weather");

        JsonElement temperatureElement = obj.get("temperature");
        JsonElement windSpeedElement = obj.get("windspeed");

        JsonObject weatherObject = new JsonObject();

        if (hotelMap.containsKey(hotelId)) {
            weatherObject.addProperty("hotelId", hotelMap.get(hotelId).getHotelID());
            weatherObject.addProperty("name", hotelMap.get(hotelId).getName());
            weatherObject.add("temperature", temperatureElement);
            weatherObject.add("windspeed", windSpeedElement);
        }

        return weatherObject.toString();

    }

    /**
     * findWeatherData Method
     *
     * @param hotelId is a variable of type String
     *                Method is used to return Weather Data in json format
     */
    public String findWeatherData(String hotelId) {

        if (hotelMap.containsKey(hotelId)) {
            Hotel hotel = hotelMap.get(hotelId);

            double latitude = hotel.getCoordinates().getLatitude();
            double longitude = hotel.getCoordinates().getLongitude();

            String jsonResult = callAPI(Double.toString(latitude), Double.toString(longitude));

            String jsonObject = parseAPI(jsonResult, hotelId);

            return jsonObject;
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("success", false);
            jsonObject.addProperty("hotelId", "invalid");
            return jsonObject.toString();

        }
    }

    /**
     * entrySetHotel Method
     * Method returns set of String which contains keys of HotelMap
     */
    public Set<String> getHotelIdAsSet() {

        Set<String> hotelIdSet = hotelMap.keySet();

        return hotelIdSet;
    }

    /**
     * findHotelObjectAsList Method
     *
     * @param hotelIdSet is a Set of type String
     * @param keyWord    is a variable of type String
     *                   Method is used to return list of type Hotel which matches with the keyword entered by the user
     */
    public List<Hotel> findHotelObjectAsList(Set<String> hotelIdSet, String keyWord) {
        List<Hotel> hotelObjects = new ArrayList<>();
        for (String hotelId : hotelIdSet) {
            Hotel hotel = hotelMap.get(hotelId);
            if (keyWord.equals("")) {
                hotelObjects.add(hotel);
            } else if (hotel.getName().toLowerCase().replaceAll("[^a-zA-Z ]", "").contains(keyWord.toLowerCase())) {
                hotelObjects.add(hotel);
            }
        }

        return hotelObjects;
    }

    /**
     * getHotelObject Method
     *
     * @param hotelId is a variable of type String
     *                Method returns the Hotel Object for a particular hotelId from the hotelMap
     */
    public Hotel getHotelObject(String hotelId) {
        return hotelMap.get(hotelId);
    }

    /**
     * getReviewSet Method
     *
     * @param hotelId is a variable of type String
     *                Method returns the Set of Reviews for a particular hotelId from the reviewsMap
     */
    public Set<Review> getReviewSet(String hotelId) {
        return reviewsMap.get(hotelId);
    }

    /**
     * getAverageRating Method
     *
     * @param hotelId is a variable of type String
     *                Method returns the average rating of all the reviews in the reviewSet
     */
    public double getAverageRating(String hotelId) {
        Set<Review> reviewSet = reviewsMap.get(hotelId);
        double avgRating = 0.0;
        if (reviewSet != null) {
            for (Review review : reviewSet) {
                avgRating += review.getRatingOverall();
            }

            avgRating = avgRating / reviewSet.size();
        }

        return Double.parseDouble(df.format(avgRating));
    }

    /**
     * generateLink Method
     *
     * @param hotelId is a variable of type String
     *                Method is used to create a link for expedia and return the link
     */
    public String generateLink(String hotelId) {
        String link = "https://www.expedia.com/";
        String hotelName = "";
        if (!hotelId.equals("null"))
            hotelName = hotelMap.get(hotelId).getName();
        link = link + hotelName + ".h" + hotelId + ".Hotel-Information";

        return link;
    }

    /**
     * generateDate Method
     * Method is used to return current date
     */
    public LocalDate generateDate() {
        return LocalDate.now();
    }

    /**
     * generateTime Method
     * Method is used to return current time
     */
    public LocalTime generateTime() {
        return LocalTime.now();
    }

    /**
     * Returns the hex encoding of a byte array.
     *
     * @param bytes  - byte array to encode
     * @param length - desired length of encoding
     * @return hex encoded byte array
     */
    public static String encodeHex(byte[] bytes, int length) {
        BigInteger bigint = new BigInteger(1, bytes);
        String hex = String.format("%0" + length + "X", bigint);

        assert hex.length() == length;
        return hex;
    }

    /**
     * generateReviewId Method
     * Method is used to create new reviewId using Random class
     */
    public String generateReviewId() {

        byte[] bytes = new byte[8];
        random.nextBytes(bytes);

        String number = encodeHex(bytes, 18);
        String reviewId = "review" + number;

        return reviewId;
    }

    /**
     * storeReview Method
     *
     * @param hotelId
     * @param ratingOverall
     * @param title
     * @param reviewText
     * @param username      Method is used to store a new review which has been given by the user
     */
    public void storeReview(String hotelId, String ratingOverall, String title, String reviewText, String username) {
        LocalDate date = generateDate();
        LocalTime time = generateTime();
        String submissionDateTime = date + "T" + time + "Z";
        String reviewId = generateReviewId();

        while (reviewIdHashSet.contains(reviewId)) {
            reviewId = generateReviewId();
        }

        Review review = new Review(hotelId, reviewId, Double.parseDouble(ratingOverall), title, reviewText, username, submissionDateTime);

        if (reviewsMap.containsKey(hotelId)) {
            reviewsMap.get(hotelId).add(review);
        } else {
            ComparatorByDate comparatorByDate = new ComparatorByDate();
            Set<Review> reviewSet = new TreeSet<>(comparatorByDate);
            reviewSet.add(review);
            reviewsMap.put(hotelId, reviewSet);
        }
    }

    /**
     * checkReviewByUsername Method
     *
     * @param reviewSet
     * @param user      Method is used to create new reviewId using Random class
     */
    public List<String> checkReviewByUsername(Set<Review> reviewSet, String user) {
        List<String> userList = new ArrayList<>();
        for (Review review : reviewSet) {
            if (review.getUserNickname().equals(user))
                userList.add(review.getUserNickname());
        }

        return userList;
    }


/*
    public void deleteReview(String reviewId, Set<Review> reviewSet){
        for(Review review: reviewSet){
            if(review.getReviewId().equals(reviewId))
                reviewSet.remove(review);
        }
    }
*/

    /**
     * deleteReviewFromMap Method
     *
     * @param hotelId
     * @param reviewId Method is used to delete a review from Map
     */
    public void deleteReviewFromMap(String hotelId, String reviewId) {
        if (reviewsMap.containsKey(hotelId)) {
            Set<Review> reviewSet = reviewsMap.get(hotelId);
            Review tempReview = null;
            for (Review review : reviewSet) {
                if (review.getReviewId().equals(reviewId))
                    tempReview = review;
            }
            reviewSet.remove(tempReview);
        }
    }
}
