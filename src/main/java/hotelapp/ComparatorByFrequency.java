package hotelapp;

import java.util.Comparator;

//class CompareByNumberOfOccurances
public class ComparatorByFrequency implements Comparator<ReviewWithFrequency> {

    private ComparatorByDate comparatorByDate = new ComparatorByDate();

    /**
     * compare Method
     *
     * @param o1 is object1 of type Reviews
     * @param o2 is object2 of type Reviews
     *           Method is used to compare o1 and o2 based on Number of occurrence, if Number of occurrences are equal, then sort on the basis of submission date
     */
    @Override
    public int compare(ReviewWithFrequency o1, ReviewWithFrequency o2) {
        if (o1.frequency == o2.frequency) {
//            return o1.review.getReviewSubmissionDate().compareTo(o2.review.getReviewSubmissionDate());
            return comparatorByDate.compare(o1.review, o2.review);
        } else {
            if (o1.frequency < o2.frequency)
                return 1;
            else
                return -1;
        }

    }
}
