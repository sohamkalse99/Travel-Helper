package hotelapp;

import com.google.gson.*;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;


//JsonParsing Class
public class JsonParsing {


    private ThreadSafeHotelReviewData threadSafeHotelReviewData;
    private ExecutorService poolOfThreads; // a pool of threads

    private Phaser phaser;

//    private Logger logger;

    public JsonParsing(ThreadSafeHotelReviewData threadSafeHotelReviewData, int numberOfThreads) {
        this.threadSafeHotelReviewData = threadSafeHotelReviewData;
        this.poolOfThreads = Executors.newFixedThreadPool(numberOfThreads);//adding a random number as of now
        this.phaser = new Phaser();
//        logger = LogManager.getLogger();
    }

    /**
     * parseHotel Method
     *
     * @param filename takes json file
     * @return Map which has key type as Integer and its value is a Set of type Hotel
     * Method is used to read Json file and convert into Java Object
     */
    public void parseHotel(String filename) {
        Hotel[] hotels = null;
        Gson gson = new Gson();
        List<Hotel> hotelList = new ArrayList<>();
        try (FileReader br = new FileReader(filename)) {
            JsonParser parser = new JsonParser();
            JsonObject obj = (JsonObject) parser.parse(br);

            JsonArray jsonArr = obj.getAsJsonArray("sr");

            hotels = gson.fromJson(jsonArr, Hotel[].class);
//            hotelList = Arrays.asList(hotels);

        } catch (IOException e) {
            System.out.println("Error while reading the file");
        }
        threadSafeHotelReviewData.buildHotelMap(hotels);

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        /*boolean tableExists = databaseHandler.hotelTableExists();
        if(!tableExists){
            databaseHandler.createHotelsTable();
        }
        */
        databaseHandler.insertArrayInHotelsTable(hotels);

    }

    /**
     * shutdownExecute Method
     * Method is used to terminate poolofthreads
     */
    public void shutdownExecutor() {
        poolOfThreads.shutdown();
        try {
            poolOfThreads.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    /*closePhase Method
     * awaitAdvance method waits for all the threads to complete and then calls the shutdownExecute method
     * */
    public void closePhase() {
        phaser.awaitAdvance(phaser.getPhase());
        shutdownExecutor();
    }

    /**
     * checkJsonFile Method
     *
     * @param foldername Takes Foldername
     * @param outputPath Takes outputpath
     * @return Map which has key type as Integer and its value is a Set of type Reviews
     * Method is used to traverse through reviews folder and check for json files
     */
    public void checkJsonFileReview(String foldername, String outputPath) {


        Path p = Paths.get(foldername);
        try (DirectoryStream<Path> pathsInDir = Files.newDirectoryStream(p)) {
            for (Path path : pathsInDir) {
                if (Files.isDirectory(path)) {
                    checkJsonFileReview(path.toString(), outputPath);
                } else if (path.toString().endsWith(".json")) {
                    JsonFileWorker worker = new JsonFileWorker(path.toString(), outputPath);
//                    logger.debug("Created a worker for " + file.toString());
                    phaser.register();
                    poolOfThreads.submit(worker);
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot open directory");
        }

    }

    //class JsonFileWorker
    class JsonFileWorker implements Runnable {

        private String filename;
        private String outputPath;

        /**
         * Class JsonFileWorker
         *
         * @param filename
         * @param outputPath
         */
        public JsonFileWorker(String filename, String outputPath) {
            this.filename = filename;
            this.outputPath = outputPath;
        }

        /**
         * run Method
         * Method is used to parse Reviews and send it to map
         */
        @Override
        public void run() {

            try {
                parseReview(filename, outputPath);
            } finally {
                phaser.arriveAndDeregister();
            }
        }
    }

    /*parseReview Method
     * @param filename takes json file
     * @return Map which has key type as Integer and its value is a Set of type Reviews
     * Method is used to read Json file and convert into Java Object
     * */
    public void parseReview(String Filename, String OutputPath) {

        List<Review> reviewList = new ArrayList<>();
        Gson gson = new Gson();

        try (FileReader br = new FileReader(Filename)) {
            JsonParser parser = new JsonParser();
            JsonObject jo = (JsonObject) parser.parse(br);

            Gson gsonForWriting = new GsonBuilder().setPrettyPrinting().create();
            String jsonInString = gsonForWriting.toJson(jo);
//            System.out.println(jsonInString);

            JsonArray jsonArray = jo.getAsJsonObject("reviewDetails").getAsJsonObject("reviewCollection").getAsJsonArray("review");

            for (JsonElement je : jsonArray) {
                JsonObject jsonObject = je.getAsJsonObject();
                Review review = new Review(
                        jsonObject.get("hotelId").getAsString(),
                        jsonObject.get("reviewId").getAsString(),
                        jsonObject.get("ratingOverall").getAsDouble(),
                        jsonObject.get("title").getAsString(),
                        jsonObject.get("reviewText").getAsString(),
                        jsonObject.get("userNickname").getAsString(),
                        jsonObject.get("reviewSubmissionTime").getAsString()
                );

                reviewList.add(review);

            }
//            logger.debug("Worker is done processing file: " + Filename);
            threadSafeHotelReviewData.buildReviewsMap(reviewList);
            DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
            databaseHandler.insertListInReviewsTables(reviewList);
            if (OutputPath.isEmpty())
                threadSafeHotelReviewData.buildWordMap(reviewList);

        } catch (IOException e) {
            System.out.println("Error while parsing review files");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }


}






