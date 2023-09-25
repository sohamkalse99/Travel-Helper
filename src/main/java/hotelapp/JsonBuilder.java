package hotelapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

//class JsonBuilder
public class JsonBuilder {

    public String buildJsonData(String hotelId, int limit, int offset, HttpSession session, String isOnclick, String isNext) {


        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();


        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        List<Review> reviewSet = databaseHandler.getReviewObjectSetLimitOffSet(hotelId, limit, offset);


        if (reviewSet.size() != 0) {

            if (isOnclick.equals("false")) {
                if (isNext.equals("true")) {
                    if (reviewSet.size() == 10) {
                        session.setAttribute("offset", offset + limit);
                    } /*else {
                        session.setAttribute("offset", offset - limit);
                    }*/
                } else if (isNext.equals("false")) {
                    if (offset > 0) {
                        session.setAttribute("offset", offset - limit);
                        offset = offset - limit;
                        reviewSet = databaseHandler.getReviewObjectSetLimitOffSet(hotelId, limit, offset);
                    }
                }
            } else {
//                offset = 0;
                session.setAttribute("offset", 0);
            }
        } else if (reviewSet.size() == 0 && isNext.equals("false")) {
            session.setAttribute("offset", offset - limit);
        }

        if (!reviewSet.isEmpty()) {
            jsonObject.addProperty("success", true);
            jsonObject.addProperty("hotelId", hotelId);
            for (Review review : reviewSet) {
                JsonObject reviewObject = new JsonObject();
                reviewObject.addProperty("reviewId", review.getReviewId());
                reviewObject.addProperty("title", review.getTitle());
                reviewObject.addProperty("user", review.getUserNickname());
                reviewObject.addProperty("reviewText", review.getReviewText());
                reviewObject.addProperty("date", review.getReviewSubmissionDate().getMonthValue() + ":" + review.getReviewSubmissionDate().getDayOfMonth() + ":" + review.getReviewSubmissionDate().getYear());

                /*if (count < Integer.parseInt(num)) {
                    jsonArray.add(reviewObject);
                    count++;

                }*/

                jsonArray.add(reviewObject);

            }
            jsonObject.add("reviews", jsonArray);


        } else {
            jsonObject.addProperty("success", false);
            jsonObject.addProperty("hotelId", "invalid");
            return jsonObject.toString();
        }


        return jsonObject.toString();


    }
}
