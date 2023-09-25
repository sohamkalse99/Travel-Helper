package hotelapp;

//class ReviewWithFrequency
public class ReviewWithFrequency implements Comparable<ReviewWithFrequency> {
    Review review;
    int frequency;
    String word;

    /**
     * Class ReviewWithFrequency
     *
     * @param review
     * @param frequency
     * @param word
     */
    public ReviewWithFrequency(Review review, int frequency, String word) {
        this.review = review;
        this.frequency = frequency;
        this.word = word;
    }

    public Review getReview() {
        return review;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getWord() {
        return word;
    }

    /**
     * compare Method
     *
     * @param o is object of type ReviewWithFrequency
     *          Method is used to compare object o of type ReviewWithFrequency, frequencies are equal compare by submissiondate
     */
    @Override
    public int compareTo(ReviewWithFrequency o) {
        if (o.frequency == this.frequency) {
            return o.review.getReviewSubmissionDate().compareTo(this.review.getReviewSubmissionDate());
        } else if (o.frequency < this.frequency)
            return -1;
        else
            return 1;
    }

    /**
     * toString
     *
     * @return string representation of the ReviewWithFrequency
     */
    @Override
    public String toString() {
        return "review->" + review + System.lineSeparator() +
                "frequency->" + frequency + System.lineSeparator() +
                "word->" + word + System.lineSeparator();
    }
}
