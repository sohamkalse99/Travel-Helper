package hotelapp;

import java.time.LocalDate;
import java.util.Comparator;

//class CompareByDate
public class ComparatorByDate implements Comparator<Review> {

    /**
     * compare Method
     *
     * @param o1 is object1 of type Reviews
     * @param o2 is object2 of type Reviews
     *           Method is used to compare o1 and o2 based on Submission Date
     */
    @Override
    public int compare(Review o1, Review o2) {
        LocalDate date1 = o1.getReviewSubmissionDate();
        LocalDate date2 = o2.getReviewSubmissionDate();

        if (date1.equals(date2))
            return o1.getReviewId().compareTo(o2.getReviewId());
        return date2.compareTo(date1);
    }
}
