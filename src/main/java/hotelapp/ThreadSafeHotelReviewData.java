package hotelapp;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//class ThreadSafeHotelReviewData
public class ThreadSafeHotelReviewData extends HotelReviewData {

    private ReentrantReadWriteLock lock;

    public ThreadSafeHotelReviewData() {
        lock = new ReentrantReadWriteLock();
    }

    /**
     * buildHotelMap Method
     *
     * @param hotelArray is an array of type Hotel
     *                   Method is used to call method from it's parent class, customlock write lock is applied
     */
    public void buildHotelMap(Hotel[] hotelArray) {
        lock.writeLock().lock();
        try {
            super.buildHotelMap(hotelArray);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * buildReviewsMap Method
     *
     * @param reviewsList is a list of type Review
     *                    Method is used to call method from it's parent class, customlock write lock is applied
     */
    public void buildReviewsMap(List<Review> reviewsList) {
        lock.writeLock().lock();
//        System.out.println("Adding to hashmap");
        try {
            super.buildReviewsMap(reviewsList);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * buildWordMap Method
     *
     * @param reviewsList is a list of type Review
     *                    Method is used to call method from it's parent class, customlock write lock is applied
     */
    public void buildWordMap(List<Review> reviewsList) {
        lock.writeLock().lock();
        try {
            super.buildWordMap(reviewsList);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * printHotelDetails Method
     *
     * @param Hoteldetails is a variable of type String
     *                     Method is used to call method from it's parent class, customlock read lock is applied
     */
    public void printHotelDetails(String Hoteldetails) {
        lock.readLock().lock();

        try {
            super.printHotelDetails(Hoteldetails);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * findHotel Method
     *
     * @param hotelID is a variable of type String
     *                Method is used to call method from it's parent class, customlock read lock is applied
     */
    public String findHotel(String hotelID) {
        lock.readLock().lock();
        try {
            return super.findHotel(hotelID);
        } finally {
            lock.readLock().unlock();
        }
    }

    public String findHotelAsJson(String hotelId) {
        lock.readLock().lock();
        try {
            return super.findHotelAsJson(hotelId);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * printReviews Method
     *
     * @param reviewString is a variable of type String
     *                     Method is used to call method from it's parent class, customlock read lock is applied
     */
    public void printReviews(String reviewString) {
        lock.readLock().lock();
        try {
            super.printReviews(reviewString);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * findReviews Method
     *
     * @param hotelId is a variable of type String
     *                Method is used to call method from it's parent class, customlock read lock is applied
     */
    public String findReviews(String hotelId) {
        lock.readLock().lock();
        try {
            return super.findReviews(hotelId);
        } finally {
            lock.readLock().unlock();
        }
    }

    public String findReviewsAsJson(String hotelId, String num) {
        lock.readLock().lock();
        try {
            return super.findReviewsAsJson(hotelId, num);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * printWords Method
     *
     * @param reviewWithFrequency is a variable of type String
     *                            Method is used to call method from it's parent class, customlock read lock is applied
     */
    public void printWords(String reviewWithFrequency) {
        lock.readLock().lock();
        try {
            super.printWords(reviewWithFrequency);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * findReviews Method
     *
     * @param word is a variable of type String
     *             Method is used to call method from it's parent class, customlock read lock is applied
     */
    public String findWord(String word) {
        lock.readLock().lock();

        try {
            return super.findWord(word);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * findReviews Method
     *
     * @param word is a variable of type String
     * @param num  is a variable of type String
     *             Method is used to call method from it's parent class, customlock read lock is applied
     */
    public String findWordAsJson(String word, String num) {
        lock.readLock().lock();

        try {
            return super.findWordAsJson(word, num);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * findWeatherData Method
     *
     * @param hotelId is a variable of type String
     *                Method is used to call method from it's parent class, customlock read lock is applied
     */
    public String findWeatherData(String hotelId) {
        lock.readLock().lock();

        try {
            return super.findWeatherData(hotelId);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * getHotelIdAsSet Method
     * Method is used to call method from it's parent class, customlock read lock is applied
     */
    public Set<String> getHotelIdAsSet() {
        lock.readLock().lock();

        try {
            Set<String> hotelIdSet = super.getHotelIdAsSet();
            return hotelIdSet;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * findHotelObjectAsList Method
     *
     * @param hotelIdSet is a set of type String
     * @param keyWord    is a variable of type String
     *                   Method is used to call method from it's parent class, customlock read lock is applied
     */
    public List<Hotel> findHotelObjectAsList(Set<String> hotelIdSet, String keyWord) {

        lock.readLock().lock();

        try {
            List<Hotel> hotelObjects = super.findHotelObjectAsList(hotelIdSet, keyWord);
            return hotelObjects;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * getHotelObject Method
     *
     * @param hotelId is a variable of type String
     *                Method is used to call method from it's parent class, customlock read lock is applied
     */
    public Hotel getHotelObject(String hotelId) {
        lock.readLock().lock();
        try {
            Hotel hotel = super.getHotelObject(hotelId);
            return hotel;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * getReviewSet Method
     *
     * @param hotelId is a variable of type String
     *                Method is used to call method from it's parent class, customlock read lock is applied
     */
    public Set<Review> getReviewSet(String hotelId) {
        lock.readLock().lock();
        try {
            Set<Review> reviewSet = super.getReviewSet(hotelId);
            return reviewSet;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * getAverageRating Method
     *
     * @param hotelId is a variable of type String
     *                Method is used to call method from it's parent class, customlock read lock is applied
     */
    public double getAverageRating(String hotelId) {
        lock.readLock().lock();
        try {
            double avgRating = super.getAverageRating(hotelId);
            return avgRating;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * generateLink Method
     *
     * @param hotelId is a variable of type String
     *                Method is used to call method from it's parent class, customlock read lock is applied
     */
    public String generateLink(String hotelId) {
        lock.readLock().lock();
        try {
            String link = super.generateLink(hotelId);
            return link;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * storeReview Method
     *
     * @param hotelId       is a variable of type String
     * @param ratingOverall is a variable of type String
     * @param title         is a variable of type String
     * @param reviewText    is a variable of type String
     * @param username      is a variable of type String
     *                      Method is used to call method from it's parent class, customlock read lock is applied
     */
    public void storeReview(String hotelId, String ratingOverall, String title, String reviewText, String username) {
        lock.readLock().lock();

        try {
            super.storeReview(hotelId, ratingOverall, title, reviewText, username);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * checkReviewByUsername Method
     *
     * @param reviewSet is a Set of type Review
     * @param user      is a variable of type String
     *                  Method is used to call method from it's parent class, customlock read lock is applied
     */
    public List<String> checkReviewByUsername(Set<Review> reviewSet, String user) {
        lock.readLock().lock();
        try {
            List<String> userList = super.checkReviewByUsername(reviewSet, user);
            return userList;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**deleteReview Method
     * @param reviewId is a variable of type String
     * @param reviewSet is a Set of type Review
     * Method is used to call method from it's parent class, customlock read lock is applied
     * */
/*
    public void deleteReview(String reviewId, Set<Review> reviewSet){
        lock.readLock().lock();
        try {
            super.deleteReview(reviewId, reviewSet);
        }
        finally {
            lock.readLock().unlock();
        }
    }
*/

    /**
     * deleteReviewFromMap Method
     *
     * @param hotelId  is a variable of type String
     * @param reviewId is a variable of type String
     *                 Method is used to call method from it's parent class, customlock read lock is applied
     */
    public void deleteReviewFromMap(String hotelId, String reviewId) {
        lock.readLock().lock();
        try {
            super.deleteReviewFromMap(hotelId, reviewId);
        } finally {
            lock.readLock().unlock();
        }
    }
}
