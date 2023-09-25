package hotelapp;


import jettyServer.HotelServer;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The driver class for project 4
 * Copy your project 3 classes to the hotelapp package.
 * The main function should take the following command line arguments:
 * -hotels hotelFile -reviews reviewsDirectory -threads numThreads -output filepath
 * (order may be different)
 * and read general information about the hotels from the hotelFile (a JSON file),
 * as read hotel reviews from the json files in reviewsDirectory (can be multithreaded or
 * single-threaded).
 * The data should be loaded into data structures that allow efficient search.
 * If the -output flag is provided, hotel information (about all hotels and reviews)
 * should be output into the given file.
 * Then in the main method, you should start an HttpServer that responds to
 * requests about hotels and reviews.
 * See pdf description of the project for the requirements.
 * You are expected to add other classes and methods from project 3 to this project,
 * and take instructor's / TA's feedback from a code review into account.
 * Please download the input folder from Canvas.
 */
public class HotelSearch {

    private static Scanner sc = new Scanner(System.in);

    /*traverseArguments Method
     * @param args is an array of type String
     * Method is used to take arguments from command line and store in a hashmap
     * */
    public static Map<String, String> traverseArguments(String[] args) {

        Map<String, String> inputMap = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-")) {
                inputMap.put(args[i].substring(1), args[i + 1]);
                i++;
            }
        }

        return inputMap;
    }

    /*inputFromUser Method
     * @param hotelReviewData is object of type ThreadSafeHotelReviewData
     * Method is used to take input from user
     * */
    public static void getUserInput(ThreadSafeHotelReviewData hotelReviewData) {

        String hotelId;
        String word;

        String userInput = " ";
        while (!userInput.equals("Q")) {
            try {

                System.out.println("Please enter below queries or press q/Q to quit");
                System.out.println("find <hotelId> or findReviews <hotelId> or findWord <word>");
                userInput = sc.nextLine();

                if (userInput.contains("findReviews")) {
                    hotelId = (userInput.substring(12));
                    String reviews = hotelReviewData.findReviews(hotelId);
                    hotelReviewData.printReviews(reviews);

                } else if (userInput.contains("findWord")) {
                    word = userInput.substring(9);
                    String reviewWtihFrequency = hotelReviewData.findWord(word);
                    hotelReviewData.printWords(reviewWtihFrequency);
                } else if (userInput.toLowerCase().equals("q")) {
                    break;
                } else if (userInput.contains("find ")) {
                    hotelId = userInput.substring(5);
                    String hotelDetails = hotelReviewData.findHotel(hotelId);
                    hotelReviewData.printHotelDetails(hotelDetails);
                } else {
                    System.out.println("Invalid Input");
                }

            } catch (Exception e) {
                System.out.println("Invalid Input");
            }
        }
    }

    public static void main(String[] args) {
        // FILL IN CODE

        Map<String, String> inputMap = traverseArguments(args);

        String jsonHotelPath = "";
        String jsonReviewPath = "";
        int numberOfThreads = 0;
        String outputFilePath = "";

        //traverse through a map
        for (Map.Entry<String, String> input : inputMap.entrySet()) {

            if (input.getKey().equals("hotels")) {
                jsonHotelPath = input.getValue();
            } else if (input.getKey().equals("reviews")) {
                jsonReviewPath = input.getValue();
            } else if (input.getKey().equals("threads")) {
                numberOfThreads = Integer.parseInt(input.getValue());
            } else if (input.getKey().equals("output")) {
                outputFilePath = input.getValue();
            }
        }

        //traverse stopwords
        StopWords stopWords = new StopWords();
        stopWords.traverseFile();

        ThreadSafeHotelReviewData threadSafeHotelReviewData = new ThreadSafeHotelReviewData();
        if (numberOfThreads == 0)
            numberOfThreads = 1;

        JsonParsing jsonParsing = new JsonParsing(threadSafeHotelReviewData, numberOfThreads);

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        boolean tableHotelExists = databaseHandler.hotelsTableExists();
        if(!tableHotelExists){
            databaseHandler.createHotelsTable();
            //Parse Hotels
            jsonParsing.parseHotel(jsonHotelPath);

        }

        boolean tableReviewsExists = databaseHandler.reviewsTableExists();
        if (!jsonReviewPath.isEmpty() && !tableReviewsExists) {
            databaseHandler.createReviewsTable();
            jsonParsing.checkJsonFileReview(jsonReviewPath, outputFilePath);
            jsonParsing.closePhase();
            jsonParsing.shutdownExecutor();
        }

/*        if (outputFilePath.isEmpty()){
            getUserInput(threadSafeHotelReviewData);
        }
        else{
            HotelReviewWriter write = new HotelReviewWriter(outputFilePath);
            write.writeToFile(threadSafeHotelReviewData);
        }*/

/*        HttpServer server = new HttpServer(threadSafeHotelReviewData);

        server.addMapping("hotelInfo", HotelHandler.class);
        server.addMapping("reviews", ReviewsHandler.class);
        server.addMapping("index", WordHandler.class);
        server.addMapping("weather", WeatherHandler.class);*/

        HotelServer hotelServer = new HotelServer(threadSafeHotelReviewData);

        try {
            hotelServer.start();
            getUserInput(threadSafeHotelReviewData);

        } catch (Exception e) {
            System.out.println("Cannot start Jetty Server");
        }
    }

}
