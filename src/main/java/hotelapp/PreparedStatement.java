package hotelapp;

/**
 * Prepared Statements
 */
public class PreparedStatement {

    /**
     * Used to insert a new user into the database.
     */
    public static final String REGISTER_SQL =
            "insert into users(username, password, usersalt) values (?,?,?);";

    /**
     * Used to authenticate a user.
     */
    public static final String AUTHENTICATE_SQL =
            "select username from users where username = ? and password = ?";

    /**
     * Displays all the usernames in the users table.
     */
    public static final String GET_USERNAME =
            "select username from users";

    /**
     * Used to retrieve the salt associated with a specific user.
     */
    public static final String SALT_SQL =
            "select usersalt from users where username = ?";

    /**
     * Used to display all tables
     */
    public static final String SHOW_TABLES =
            "show tables;";

    /**
     * Create Hotels table
     */
    public static final String CREATE_HOTELS_TABLE =
            "CREATE TABLE hotels (" +
                    "hotelID VARCHAR(64) PRIMARY KEY, " +
                    "name VARCHAR(64) NOT NULL, " +
                    "latitude DOUBLE NOT NULL, " +
                    "longitude DOUBLE NOT NULL, " +
                    "street VARCHAR(64) NOT NULL, " +
                    "city VARCHAR(64) NOT NULL, " +
                    "state VARCHAR(64) NOT NULL, " +
                    "country VARCHAR(64) NOT NULL);";

    /**
     * Returns all hotelId from hotels table
     */
    public static final String HOTELID_COLUMN =
            "select hotelID from hotels;";

    /**
     * Insert data into Hotels table
     */
    public static final String INSERT_INTO_HOTELS_TABLE =
            "Insert into hotels(hotelID, name, latitude, longitude, street, city, state, country) " +
                    "values(?, ?, ?, ?, ?, ?, ?, ?);";

    /**
     * Create reviews table
     */
    public static final String CREATE_REVIEWS_TABLE =
            "CREATE TABLE reviews (" +
                    "reviewId VARCHAR(64) PRIMARY KEY, " +
                    "hotelId VARCHAR(64), FOREIGN KEY (hotelID) references hotels (hotelID), " +
                    "ratingOverall DOUBLE, " +
                    "title VARCHAR(64), " +
                    "reviewText VARCHAR(2000), " +
                    "userNickname VARCHAR(64), " +
                    "reviewSubmissionDate VARCHAR(64)); ";

    /**
     * Insert data into reviews table
     */
    public static final String INSERT_INTO_REVIEWS_TABLE =
            "Insert into reviews(reviewId, hotelId, ratingOverall, title, reviewText, userNickname, reviewSubmissionDate) " +
                    "values(?, ?, ?, ?, ?, ?, ?);";

    /**
     *  returns all data from hotels table
     */
    public static final String GET_HOTEL_TABLE =
            "Select * from hotels;";

    /**
     *  returns all data from reviews table
     */
    public static final String GET_REVIEW_TABLE =
            "select * from reviews;";

    /**
     *  update reviews table
     */
    public static final String EDIT_REVIEW =
            "update reviews set title=?, reviewText=?, ratingOverall=?, reviewSubmissionDate=? " +
                    "where hotelId=? and reviewId=? ;";

    /**
     *  delete data from reviews table
     */
    public static final String DELETE_REVIEW =
            "delete from reviews where hotelId=? and reviewId=?";

    /**
     *  creates last login table
     */
    public static final String CREATE_LAST_LOGIN_TABLE =
            "create table lastlogin(Username VARCHAR(64), DateandTime VARCHAR(64));";

    /**
     *  fetches data from last login table
     */
    public static final String FETCH_LAST_LOGIN =
            "select DateandTime from lastlogin where Username =?;";

    /**
     *  updates last login table
     */
    public static final String UPDATE_LAST_LOGIN =
            "update lastlogin set DateandTime= ? where Username=?;";

    /**
     *  returns username from login table
     */
    public static final String CHECK_LAST_LOGIN_TABLE =
            "select username from lastlogin where username=?;";

    /**
     *  inserts data into login table
     */
    public static final String INSERT_INTO_LAST_LOGIN =
            "insert into lastlogin(Username, DateandTime) " +
                    "values(?,?);";

    /**
     *  creates expedia history table
     */
    public static final String CREATE_EXPEDIA_HISTORY_TABLE =
            "create table expediaHistory(Username VARCHAR(64), Link VARCHAR(200));";

    /**
     *  inserts data into expedia history table
     */
    public static final String INSERT_INTO_EXPEDIA_HISTORY_TABLE =
            "insert into expediaHistory(Username, Link) " +
                    "values(?,?);";

    /**
     *  Fetch data from expedia history table
     */
    public static final String FETCH_EXPEDIA_LINK_HISTORY =
            "select link from expediaHistory where Username =?;";

    /**
     *  clear data from expedia history table
     */
    public static final String CLEAR_EXPEDIA_LINK_HISTORY =
            "delete from expediaHistory where Username =?;";

    /**
     *  returns latitude and longitude from hotels table
     */
    public static final String FETCH_LATITUDE_LONGITUDE =
            "select latitude, longitude from hotels where hotelID=?;";

    /**
     *  create FavoriteHotel table
     */
    public static final String CREATE_FAVORITE_TABLE =
            "create table favoriteHotel(Username VARCHAR(64), hotelName VARCHAR(200), hotelId VARCHAR(64), PRIMARY KEY(hotelId, Username));";

    /**
     *  inserts data into favoriteHotel table
     */
    public static final String INSERT_INTO_FAVORITE_TABLE =
            "insert into favoriteHotel(Username, hotelName, hotelId) " +
                    "values(?,?,?);";

    /**
     *  returns hotelname from favoriteHotel table
     */
    public static final String FETCH_FAVORITE_HOTELS =
            "select hotelName from favoriteHotel where Username =?;";

    /**
     *  delete data from favoriteHotel table
     */
    public static final String CLEAR_FAVORITE =
            "delete from favoriteHotel where Username =?;";

    /**
     *  Fetches data from reviews table for a particular limit and offset
     */
    public static final String GET_REVIEW_TABLE_WITH_LIMIT_OFFSET =
            "select * from reviews where hotelId=? order by reviewSubmissionDate desc limit ? offset ?;";
}
