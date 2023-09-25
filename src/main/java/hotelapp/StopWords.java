package hotelapp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StopWords {


//    private static List<String> stopWordList = new ArrayList<>();

    private static Set<String> stopWordList = new HashSet<>();

    /**
     * traverseFile Method
     * Method traverses through the StopWords.txt file and adds all the words to stopWordList List
     */
    public Set<String> traverseFile() {
//        String input = "src/input/StopWords.txt";
        String input = "/Users/sohamkalse/Desktop/Soham/USFCA/CS 601 Software Development/project4-sohamkalse99/src/input/StopWords.txt";
//        String input = "https://github.com/USF-CS601-Fall22/project4-sohamkalse99/blob/0c6e9b2e8950cf93ec986650ea035d215924d87d/src/input/StopWords.txt";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            while ((line = br.readLine()) != null) {
                stopWordList.add(line.toLowerCase());
            }
        } catch (IOException e) {
            System.out.println("Problems while reading StopWords.txt file");
        }

        return stopWordList;
    }

    public static Set<String> getStopWordList() {
        return stopWordList;
    }


    //Add a contains methods
    public static boolean contains(String word) {
        if (stopWordList.contains(word))
            return true;
        else
            return false;
    }

}
