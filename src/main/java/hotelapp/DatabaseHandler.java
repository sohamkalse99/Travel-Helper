package hotelapp;

import java.io.FileReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

//class DatabaseHandler
public class DatabaseHandler {

    private static DatabaseHandler databaseHandler = new DatabaseHandler("database.properties");

    private Properties config;

    private String uri = null;

    private Random random = new Random();

    /**
     * DataBaseHandler is a singleton
     */
    public DatabaseHandler(String propertiesFile) {
        this.config = loadConfigFile(propertiesFile);
        this.uri = "jdbc:mysql://" + config.getProperty("hostname") + "/" + config.getProperty("database") + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    }

    /**
     * Returns the instance of the database handler.
     *
     * @return instance of the database handler
     */
    public static DatabaseHandler getInstance() {
        return databaseHandler;
    }

    // Load info from config file database.properties
    public Properties loadConfigFile(String propertiesFile) {
        Properties config = new Properties();

        try (FileReader reader = new FileReader(propertiesFile)) {
            config.load(reader);
        } catch (Exception e) {
            System.out.println("Cannot read from file" + propertiesFile);
        }

        return config;
    }

    /**
     * Returns the hex encoding of a byte array.
     *
     * @param saltbytes - byte array to encode
     * @param length    - desired length of encoding
     * @return hex encoded byte array
     */
    public static String encodeHex(byte[] saltbytes, int length) {
        BigInteger bigint = new BigInteger(1, saltbytes);
        String hex = String.format("%0" + length + "X", bigint);

        assert hex.length() == length;
        return hex;
    }

    /**
     * Calculates the hash of a password and salt using SHA-256.
     *
     * @param password - password to hash
     * @param salt     - salt associated with user
     * @return hashed password
     */
    public static String getHash(String password, String salt) {
        String salted = salt + password;
        String hashed = salted;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salted.getBytes());
            hashed = encodeHex(md.digest(), 64);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return hashed;
    }

    /**
     * Gets the salt for a specific user.
     *
     * @param connection - active database connection
     * @param username   - which user to retrieve salt for
     * @return salt for the specified user or null if user does not exist
     * @throws SQLException if any issues with database connection
     */
    private String getSalt(Connection connection, String username) {
        String salt = null;

        try (PreparedStatement statement = connection.prepareStatement(hotelapp.PreparedStatement.SALT_SQL)) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                salt = result.getString("usersalt");
                return salt;
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return salt;
    }

    /**
     * Registers a new user, placing the username, password hash, and
     * salt into the database.
     *
     * @param newUser     - username of new user
     * @param newPassword - password of new user
     */
    public void registerUser(String newUser, String newPassword) {

        byte[] saltbytes = new byte[16];//Used to generate salt
        random.nextBytes(saltbytes);

        String userSalt = encodeHex(saltbytes, 32);
        String passwordhash = getHash(newPassword, userSalt); // hashed password

        PreparedStatement statement;

        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("Database connection successful");
            try {
                statement = connection.prepareStatement(hotelapp.PreparedStatement.REGISTER_SQL);
                statement.setString(1, newUser);
                statement.setString(2, passwordhash);
                statement.setString(3, userSalt);
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (SQLException exception) {
            System.out.println(exception);
        }
    }

    /**
     * Checks whether username and password are correct
     *
     * @param username - username of existing user
     * @param password - password of existing user
     */
    public boolean authenticateUser(String username, String password) {
        PreparedStatement statement;
        boolean isValid = false;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("Connection established");
            try {
                String usersalt = getSalt(connection, username);
                String passwordSalt = getHash(password, usersalt);
                statement = connection.prepareStatement(hotelapp.PreparedStatement.AUTHENTICATE_SQL);
                statement.setString(1, username);
                statement.setString(2, passwordSalt);
                ResultSet result = statement.executeQuery();

                isValid = result.next();
                return isValid;
            } catch (SQLException exception) {
                System.out.println(exception);
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        return isValid;
    }

    /**
     * @returns list of users in the users table
     */
    public List<String> getUsersList() {
        PreparedStatement statement;
        List<String> userList = new ArrayList<>();
        String user = "";
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = connection.prepareStatement(hotelapp.PreparedStatement.GET_USERNAME);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    userList.add(resultSet.getString("username"));
                }
                return userList;
            } catch (SQLException exception) {
                System.out.println(exception);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Checks whether Table hotel Exist in the list of tables
     *
     * @return true if table exist or else false
     */
    public boolean hotelsTableExists() {

        List<String> tables = getTableData();

        for (String table : tables) {
            if (table.equals("hotels")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether Table reviews Exist in the list of tables
     *
     * @return true if table exist or else false
     */
    public boolean reviewsTableExists() {

        List<String> tables = getTableData();

        for (String table : tables) {
            if (table.equals("reviews")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates hotels tables
     *
     * creates a connection first with the database and then creates a table
     */
    public void createHotelsTable() {
        Statement statement;

        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.createStatement();
            statement.executeUpdate(hotelapp.PreparedStatement.CREATE_HOTELS_TABLE);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /*   public List<String> hotelIdListFromTable(){
           PreparedStatement statement;
           List<String> hotelIdList = new ArrayList<>();
           try(Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))){
               statement = connection.prepareStatement(hotelapp.PreparedStatement.HOTELID_COLUMN);
               ResultSet results = statement.executeQuery();
               while (results.next()){
                   hotelIdList.add(results.getString("hotelID"));
               }

           }catch (SQLException e){
               System.out.println(e);
           }
           return hotelIdList;

       }*/

    /**
     * Inserts hotels array which was created after parsing json files
     *
     * @param hotels array of type hotels
     */
    public void insertArrayInHotelsTable(Hotel[] hotels) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            for (Hotel hotel : hotels) {
                statement = connection.prepareStatement(hotelapp.PreparedStatement.INSERT_INTO_HOTELS_TABLE);
                statement.setString(1, hotel.getHotelID());
                statement.setString(2, hotel.getName());
                statement.setDouble(3, hotel.getCoordinates().getLatitude());
                statement.setDouble(4, hotel.getCoordinates().getLongitude());
                statement.setString(5, hotel.getStreet());
                statement.setString(6, hotel.getCity());
                statement.setString(7, hotel.getState());
                statement.setString(8, hotel.getCountry());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Creates reviews tables
     *
     * creates a connection first with the database and then creates a table
     */
    public void createReviewsTable() {
        Statement statement;

        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.createStatement();
            statement.executeUpdate(hotelapp.PreparedStatement.CREATE_REVIEWS_TABLE);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Inserts reviews list which was created after parsing json files
     *
     * @param reviewList array of type hotels
     */
    public void insertListInReviewsTables(List<Review> reviewList) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            for (Review review : reviewList) {
                statement = connection.prepareStatement(hotelapp.PreparedStatement.INSERT_INTO_REVIEWS_TABLE);
                statement.setString(1, review.getReviewId());
                statement.setString(2, review.getHotelId());
                statement.setDouble(3, review.getRatingOverall());
                statement.setString(4, review.getTitle());
                statement.setString(5, review.getReviewText());
                statement.setString(6, review.getUserNickname());
                statement.setString(7, review.getReviewSubmissionDate().toString());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    /**
     * Creates a new reviewId
     *
     * @return reviewId of type String
     */
    public String generateReviewId() {

        byte[] bytes = new byte[8];
        random.nextBytes(bytes);

        String number = encodeHex(bytes, 18);
        String reviewId = "review" + number;

        return reviewId;
    }

    /**
     * Inserts a review in the reviews table
     *
     * @param hotelId hotelId of type String
     * @param ratingOverall ratingOverall of type String
     * @param title ratingOverall of type String
     * @param reviewText reviewText of type String
     * @param username username of type String
     */
    public void insertInReviewsTable(String hotelId, String ratingOverall, String title, String reviewText, String username) {

        LocalDate date = generateDate();
//        LocalTime time = generateTime();
        String submissionDate = date.toString();

        String reviewId = generateReviewId();

        Set<Review> reviewSet = getReviewObjectSet(hotelId);
        Set<String> reviewIdSet = new HashSet<>();
        for (Review review : reviewSet) {
            reviewIdSet.add(review.getReviewId());
        }

        while (reviewIdSet.contains(reviewId)) {
            reviewId = generateReviewId();
        }

        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.INSERT_INTO_REVIEWS_TABLE);

            statement.setString(1, reviewId);
            statement.setString(2, hotelId);
            statement.setDouble(3, Double.parseDouble(ratingOverall));
            statement.setString(4, title);
            statement.setString(5, reviewText);
            statement.setString(6, username);
            statement.setString(7, submissionDate);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Returns list of tables which are present in the database
     *
     * @return List of type String
     */
    public List<String> getTableData() {
        PreparedStatement statement;
        List<String> tables = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.SHOW_TABLES);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                tables.add(results.getString("Tables_in_user023"));
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return tables;

    }

    /**
     * Returns object of type Hotel
     * @param hotelId
     *
     * @return object of type Hotel
     */
    public Hotel getHotelObject(String hotelId) {
        PreparedStatement statement;
        Hotel hotel = null;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.GET_HOTEL_TABLE);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                String dbHotelId = results.getString("hotelID");
                if (dbHotelId.equals(hotelId)) {
                    String name = results.getString("name");
                    Double latitude = results.getDouble("latitude");
                    Double longitude = results.getDouble("longitude");
                    String street = results.getString("street");
                    String city = results.getString("city");
                    String state = results.getString("state");
                    String country = results.getString("country");
                    Coordinates coordinates = new Coordinates(latitude, longitude);
                    hotel = new Hotel(name, hotelId, coordinates, street, city, state, country);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return hotel;
    }

    /**
     * Returns Set of type Hotel
     *
     * @return Set of type Hotel
     */
    public Set<Hotel> getHotelObjectSet() {

        Set<Hotel> hotelSet = new HashSet<>();

        PreparedStatement statement;

        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.GET_HOTEL_TABLE);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                String hotelID = results.getString("hotelID");
                String name = results.getString("name");
                Double latitude = results.getDouble("latitude");
                Double longitude = results.getDouble("longitude");
                String street = results.getString("street");
                String city = results.getString("city");
                String state = results.getString("state");
                String country = results.getString("country");

                Coordinates coordinates = new Coordinates(latitude, longitude);
                Hotel hotel = new Hotel(name, hotelID, coordinates, street, city, state, country);
                hotelSet.add(hotel);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return hotelSet;
    }

    /**
     * Returns current date
     *
     * @return current date
     */
    public LocalDate generateDate() {
        return LocalDate.now();
    }

    /**
     * Returns current time
     *
     * @return current time
     */
    public LocalTime generateTime() {
        return LocalTime.now();
    }

    /**
     * Returns Set of type Review
     *
     * @param hotelId
     * @return Set of type Hotel
     */
    public Set<Review> getReviewObjectSet(String hotelId) {

        LocalTime time = generateTime();

        PreparedStatement statement;
        Set<Review> reviewSet = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.GET_REVIEW_TABLE);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                if (hotelId.equals(results.getString("hotelId"))) {
                    String reviewId = results.getString("reviewId");
                    double ratingOverall = results.getDouble("ratingOverall");
                    String title = results.getString("title");
                    String reviewText = results.getString("reviewText");
                    String userNickname = results.getString("userNickname");
                    String reviewSubmissionDate = results.getString("reviewSubmissionDate");
                    reviewSubmissionDate = reviewSubmissionDate + "T" + time + "Z";
                    Review review = new Review(hotelId, reviewId, ratingOverall, title, reviewText, userNickname, reviewSubmissionDate);

                    reviewSet.add(review);
                }


            }


        } catch (SQLException e) {
            System.out.println(e);
        }

        return reviewSet;
    }

    /**
     * Returns List of type Review
     *
     * @param hotelId
     * @param limit
     * @param offSet
     *
     * @return List of type Review
     */
    public List<Review> getReviewObjectSetLimitOffSet(String hotelId, int limit, int offSet){

        LocalTime time = generateTime();

        PreparedStatement statement;
        List<Review> reviewSet = new ArrayList<>();


        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.GET_REVIEW_TABLE_WITH_LIMIT_OFFSET);
            statement.setString(1, hotelId);
            statement.setInt(2, limit);
            statement.setInt(3, offSet);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                String reviewId = results.getString("reviewId");
                double ratingOverall = results.getDouble("ratingOverall");
                String title = results.getString("title");
                String reviewText = results.getString("reviewText");
                String userNickname = results.getString("userNickname");
                String reviewSubmissionDate = results.getString("reviewSubmissionDate");
                reviewSubmissionDate = reviewSubmissionDate + "T" + time + "Z";
                Review review = new Review(hotelId, reviewId, ratingOverall, title, reviewText, userNickname, reviewSubmissionDate);

                reviewSet.add(review);
            }


        } catch (SQLException e) {
            System.out.println(e);
        }

        return reviewSet;
    }

    /**
     * updates review
     *
     * @param hotelId
     * @param reviewId
     * @param title
     * @param reviewText
     * @param rating
     *
     * @return List of type Review
     */
    public void editReview(String hotelId, String reviewId, String title, String reviewText, String rating) {

        String reviewSubmissionDate = generateDate().toString();
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.EDIT_REVIEW);
            statement.setString(1, title);
            statement.setString(2, reviewText);
            statement.setString(3, rating);
            statement.setString(4, reviewSubmissionDate);
            statement.setString(5, hotelId);
            statement.setString(6, reviewId);
            statement.executeUpdate();
        } catch (SQLException e) {

        }
    }

    /**
     * deleteReview
     *
     * @param hotelId
     * @param reviewId
     *
     * @return List of type Review
     */
    public void deleteReview(String hotelId, String reviewId) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.DELETE_REVIEW);
            statement.setString(1, hotelId);
            statement.setString(2, reviewId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Creates createLastLoginTable tables
     *
     * creates a connection first with the database and then creates a table
     */
    public void createLastLoginTable() {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.CREATE_LAST_LOGIN_TABLE);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * fetches data from Login Table
     *
     * @param username
     *
     * @return dateandtime from LastLoginTable
     */
    public String fetchLastLoginFromTable(String username) {
        PreparedStatement statement;
        String dateAndTime = "";
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.FETCH_LAST_LOGIN);
            statement.setString(1, username);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                dateAndTime = results.getString("DateandTime");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return dateAndTime;
    }

    /**
     * updates last Login Table
     *
     * @param username
     *
     */
    public void updateLastLoginTable(String username) {
        PreparedStatement statement;
        String date = generateDate().toString();
        String time = generateTime().toString();
        String dateAndTime = date + "T" + time + "Z";

        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.UPDATE_LAST_LOGIN);
            statement.setString(1, dateAndTime);
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * updates last Login Table
     *
     * @param username
     *
     * @return a flag which can be either true or false
     */
    public boolean checkLastLoginTable(String username) {
        PreparedStatement statement;
        boolean flag = true;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.CHECK_LAST_LOGIN_TABLE);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            flag = result.next();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return flag;
    }

    /**
     * insert data into last login table
     *
     * @param username
     *
     */
    public void insertIntoLastLoginTable(String username) {
        PreparedStatement statement;

        String date = generateDate().toString();
        String time = generateTime().toString();
        String dateAndTime = date + "T" + time + "Z";

        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.INSERT_INTO_LAST_LOGIN);
            statement.setString(1, username);
            statement.setString(2, dateAndTime);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Creates createExpediaHistory tables
     *
     * creates a connection first with the database and then creates a table
     */
    public void createExpediaHistory() {
        PreparedStatement statement;

        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.CREATE_EXPEDIA_HISTORY_TABLE);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * insert data into ExpediaLink
     *
     * @param username
     * @param link
     *
     */
    public void storeExpediaLink(String username, String link) {
        PreparedStatement statement;

        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.INSERT_INTO_EXPEDIA_HISTORY_TABLE);
            statement.setString(1, username);
            statement.setString(2, link);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * returns expedia link from table
     *
     * @param username
     *
     * @return List of links
     */
    public List<String> getExpediaLinkHistory(String username) {
        PreparedStatement statement;
        List<String> links = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.FETCH_EXPEDIA_LINK_HISTORY);
            statement.setString(1, username);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                links.add(results.getString("Link"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return links;
    }

    /**
     * delete data from ExpediaLinkHistory
     *
     * @param username
     *
     * @return List of links
     */
    public void clearExpediaLinkHistory(String username){
        PreparedStatement statement;

        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.CLEAR_EXPEDIA_LINK_HISTORY);
            statement.setString(1, username);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * returns Coordinates object
     *
     * @param hotelId
     *
     * @return object of type Hotel
     */
    public Coordinates getCoordinates(String hotelId){
        PreparedStatement statement;
        double latitude = 0.0;
        double longitude = 0.0;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.FETCH_LATITUDE_LONGITUDE);
            statement.setString(1, hotelId);
            ResultSet results = statement.executeQuery();

            if(results.next()){
                latitude = results.getDouble("latitude");
                longitude = results.getDouble("longitude");
            }
        }catch (SQLException e) {
            System.out.println(e);
        }

        Coordinates coordinates = new Coordinates(latitude, longitude);

        return coordinates;
    }

    /**
     * Creates createFavoritesTable tables
     *
     * creates a connection first with the database and then creates a table
     */
    public void createFavoritesTable(){
        PreparedStatement statement;

        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.CREATE_FAVORITE_TABLE);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Store data in Favorites table
     *
     * @return boolean value, If data is duplicate returns false or else returns true
     */
    public boolean storeFavorites(String username, String hotelId, String hotelName){
        PreparedStatement statement;
        boolean isDuplicate = false;

        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.INSERT_INTO_FAVORITE_TABLE);
            statement.setString(1, username);
            statement.setString(2, hotelName);
            statement.setString(3, hotelId);
            statement.executeUpdate();
        } catch (SQLException e) {
            isDuplicate = true;
            System.out.println(e);
        }

        return isDuplicate;
    }

    /**
     * Fetches data from favorites hotel
     *
     * @param username
     *
     * @return List of type string
     */
    public List<String> getFavoriteHotels(String username){
        PreparedStatement statement;
        List<String> hotelNames = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.FETCH_FAVORITE_HOTELS);
            statement.setString(1, username);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                hotelNames.add(results.getString("hotelName"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return hotelNames;
    }

    /**
     * deletes data from Favorites table
     *
     * @param username
     *
     */
    public void clearFavorites(String username){
        PreparedStatement statement;

        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(hotelapp.PreparedStatement.CLEAR_FAVORITE);
            statement.setString(1, username);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e);
        }
    }
}
