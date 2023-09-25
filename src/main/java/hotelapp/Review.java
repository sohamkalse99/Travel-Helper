package hotelapp;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/*Class Reviews*/
public class Review {
    final private String hotelId;

    final private String reviewId;

    final private double ratingOverall;

    final private String title;

    final private String reviewText;

    final private String userNickname;

    private ReviewWithFrequency reviewWithFrequency;

    @SerializedName(value = "reviewSubmissionTime")
    final private LocalDate reviewSubmissionDate;

    /**
     * Class Reviews
     *
     * @param hotelId
     * @param reviewId
     * @param ratingOverall        Hotel Rating
     * @param title                Title of the Rating
     * @param reviewText           Review of the Hotel
     * @param userNickname         Nickname of the Reviewer
     * @param reviewSubmissionDate Date of submission of the review
     */
    public Review(String hotelId, String reviewId, double ratingOverall, String title, String reviewText, String userNickname, String reviewSubmissionDate) {
        this.hotelId = hotelId;
        this.reviewId = reviewId;
        this.ratingOverall = ratingOverall;
        this.title = title;
        this.reviewText = reviewText;

        if (userNickname.isEmpty()) {
            userNickname = "Anonymous";
        }

        this.userNickname = userNickname;
//        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-DD");
//        this.reviewSubmissionDate = LocalDate.parse(reviewSubmissionDate.substring(0,10));

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        this.reviewSubmissionDate = LocalDate.parse(reviewSubmissionDate, formatter);

    }

    /**
     * toString
     *
     * @return string representation of the review
     */
    @Override
    public String toString() {
        return System.lineSeparator() +
                "HotelId->" + hotelId + System.lineSeparator() +
                "ReviewId->" + reviewId + System.lineSeparator() +
                "AverageRating->" + ratingOverall + System.lineSeparator() +
                "Title->" + title + System.lineSeparator() +
                "ReviewText->" + reviewText + System.lineSeparator() +
                "UserNickname->" + userNickname + System.lineSeparator() +
                "SubmissionDate->" + reviewSubmissionDate;
    }


    //Getters
    public String getHotelId() {
        return hotelId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public double getRatingOverall() {
        return ratingOverall;
    }

    public String getTitle() {
        return title;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public LocalDate getReviewSubmissionDate() {
        return reviewSubmissionDate;
    }


}

