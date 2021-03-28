package com.project.sebastianrohe.twitter.helper;

import com.project.sebastianrohe.twitter.data.Tweet;
import com.project.sebastianrohe.twitter.data.impl.TweetImpl;
import org.apache.uima.UIMAException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to manage reading from CSV files.
 *
 * @author Sebastian Rohe
 */
public class FileReaderHelper {

    /**
     * This method reads in every line of a given csv file and converts it to a set of strings.
     * Every string in the set represents a line of the original CSV file. Empty lines will be ignored.
     *
     * @param filePath Path of csv file to read in.
     * @return Set of strings from all read in lines.
     */
    public static Set<String> readInLineByLine(String filePath) {
        // tweet strings. Every string represents a read in line from the csv file.
        Set<String> allReadInLines = new HashSet<>(0);

        // Try to read in csv file from given file path and save every line as string in a set and return it.
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String line = reader.readLine();

            // While line is not empty read in lines, convert them and add strings to set.
            while (line != null) {
                // Add line string to readInLines set.
                allReadInLines.add(line);
                // Next line gets read in.
                line = reader.readLine();
            }
            // Close reader when all lines are read in.
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Return the set of all read in lines as set of strings.
        return allReadInLines;
    }

    /**
     * This method takes in the path of a csv file, processes the resulting set of line strings (every line represents a tweet)
     * from the readInLineByLine() method and converts every line string to an actual tweet object. Every tweet is stored in a set.
     *
     * @param filePath Path of csv file to read in.
     * @return A set of all resulting tweet objects.
     */
    public static Set<Tweet> convertReadInLines(String filePath) throws UIMAException {
        // Empty set will get filled with tweets.
        Set<Tweet> actualTweets = new HashSet<>();
        // Set with read in line strings from readInLineByLine() method.
        Set<String> tweetStrings = FileReaderHelper.readInLineByLine(filePath);

        // For each line string in the set we will execute following code.
        for (String tweetString : tweetStrings) {
            // If length of the line string is not 0 and the string contains tabs it is split in different string
            // parts with tabs as separators.
            if (tweetString.length() != 0 && tweetString.contains("\t")) {
                String[] splitLine = tweetString.split("\t");
                // Create tweet objects with string values from split method.
                // String value for date is convert via Date class for proper date representation.
                Tweet createdTweet = new TweetImpl(Long.parseLong(splitLine[0]), new Date(Long.parseLong(splitLine[1])),
                        splitLine[2], splitLine[3], splitLine[4], Boolean.parseBoolean(splitLine[5]));

                // If boolean value of retweet variable is true, retweet id is changed from default value -1 to string
                // value of the retweet id which gets parsed to long.
                // If this is not the case and the parsed retweet variable is false the value for retweet id remains -1.
                if (Boolean.parseBoolean(splitLine[5])) {
                    createdTweet.setRetweetId(Long.parseLong(splitLine[6]));
                }
                // Created tweet object is added to actualTweets set after every run of this foreach loop.
                actualTweets.add(createdTweet);
            }
        }
        // At the end a set of all resulting tweets is returned.
        return actualTweets;
    }
}




