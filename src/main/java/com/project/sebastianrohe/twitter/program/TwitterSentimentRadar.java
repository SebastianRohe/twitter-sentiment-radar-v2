package com.project.sebastianrohe.twitter.program;

import com.project.sebastianrohe.twitter.analysis.NLPAnalysis;
import com.mongodb.client.FindIterable;
import com.project.sebastianrohe.twitter.data.Tweet;
import com.project.sebastianrohe.twitter.data.impl.UserImpl;
import com.project.sebastianrohe.twitter.database.MongoDBConnectionHandler;
import com.project.sebastianrohe.twitter.helper.FileReaderHelper;
import com.project.sebastianrohe.twitter.helper.NLPHelper;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.bson.Document;
import com.project.sebastianrohe.twitter.services.TweetService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TwitterSentimentRadar {

    private static String filePath = "";
    private static String mongoPropertiesPath = "";

    // Create instance variable for sets of tweet objects and user objects.
    private static final Set<UserImpl> userObjectsSet = new HashSet<>();
    private static Set<Tweet> tweetObjectsSet = new HashSet<>();

    /**
     * Main() method to run all functionalities of the application.
     *
     * @param args Parameters which can be handed at program start.
     * @throws IOException If something goes wrong.
     */
    public static void main(String[] args) throws IOException, UIMAException {
        // Paths for CSV file and Properties file for MongoDB.
        filePath = "src/main/resources/twitter.csv";
        mongoPropertiesPath = "src/main/resources/MongoDBConfig.properties";

        // Create instance of classes to access all required methods and variables.
        MongoDBConnectionHandler mongoDBConnectionHandler = new MongoDBConnectionHandler(mongoPropertiesPath);
        TwitterSentimentRadar twitterSentimentRadar = new TwitterSentimentRadar(filePath);
        TweetService services = new TweetService();

        // User input is empty string by default.
        String userInput = "";
        Scanner userInputScanner = new Scanner(System.in);

        // Program runs in loop until user enters 'close' to terminate it.
        while (!userInput.equalsIgnoreCase("close")) {

            // Main menu.
            System.out.println("\n" + "function?");
            System.out.println("\t" + "list");
            System.out.println("\t" + "count");
            System.out.println("\t" + "search");
            System.out.println("\t" + "close");
            System.out.println("\t" + "import");
            System.out.println("\t" + "analyse");

            // User input gets converted to lower case for further processing.
            userInput = userInputScanner.nextLine().toLowerCase();

            // Switch statement to handle valid inputs.
            switch (userInput) {

                // Import all read tweets from a CSV file (stored in tweetObjectsSet) to the MongoDB.
                case "import":

                    System.out.println("Tweets getting inserted in MongoDB...");
                    // Call of the insertManyTweetDocuments() method from mongoDBConnectionHandler.
                    mongoDBConnectionHandler.insertManyTweetDocuments(tweetObjectsSet);

                    break;

                // Analyse all tweet documents in database collection which are not already analysed.
                case "analyse":

                    NLPAnalysis nlpAnalysis = new NLPAnalysis();
                    // Provide analyse engine with pipeline.
                    NLPHelper nlpHelper = new NLPHelper();

                    // Iterator to get everything in database collection.
                    FindIterable<Document> documentIterator = mongoDBConnectionHandler.getCollection().find();

                    // Create analyse engine with pipeline once.
                    AnalysisEngine analysisEngine = nlpHelper.getAnalysisEngine();

                    // For each found tweet document (by the iterator) in the database collection do the following.
                    for (Document tweetDocument : documentIterator) {
                        // If tweetDocument is not NLP analysed yet, analyse it. Check if document already has
                        // key 'sentiments' inside it. The key check is null if tweet document was not analysed yet.
                        if (tweetDocument.get("sentiments") == null) {
                            // Analyse tweet document, return resulting document and save it in variable.
                            Document analysedTweetDocument = nlpAnalysis.runNLP(tweetDocument, analysisEngine);

                            // Update the tweet document in database collection with result of NLP analysis.
                            mongoDBConnectionHandler.updateWithNLPAnalysedDocument(analysedTweetDocument);
                            System.out.println("Tweet analysed successfully. Tweet document updated in database... " + analysedTweetDocument);
                        }

                        // If tweet document is not analysed yet inform which tweet documents are already analysed.
                        else {
                            System.err.println("Tweet already analysed in database... " + tweetDocument);
                        }
                    }

                    System.out.println("NLP analysis completed.");

                    break;

                case "list":

                    // Submenu for list.
                    System.out.println("\t" + "users");
                    System.out.println("\t" + "tweets");
                    System.out.println("\t" + "hashtags");
                    System.out.println("\t" + "close");

                    userInput = userInputScanner.nextLine().toLowerCase();

                    switch (userInput) {

                        // Get all usernames in alphanumerical order.
                        case "users":

                            // Method to get all users from instance of TweetService class to get a unsorted set of user strings.
                            Set<String> unsortedSetOfUsers = services.getAllUsers(tweetObjectsSet);
                            // Convert set of all users strings to list of user strings.
                            List<String> listOfUsers = new ArrayList<>(unsortedSetOfUsers);
                            // Sort list of user strings alphanumerical. Comparing strings in list with each other.
                            listOfUsers.sort(String::compareTo);

                            // Print out every user for each user string in the list of all user strings.
                            for (String userString : listOfUsers)
                                System.out.println(userString);

                            break;

                        // Get all tweets by a user for entered username.
                        case "tweets":

                            // Empty set to fill in all tweets by a user.
                            Set<Tweet> tweetsByUserSet = new HashSet<>();

                            System.out.println("filter?");
                            System.out.println("\t" + "username");
                            System.out.println("\t" + "all");

                            Scanner inputScanner = new Scanner(System.in);
                            // User input is the user string to look for.
                            String userNameToLookFor = inputScanner.next();

                            // Filter for the username string the program user entered. Look in username field of every tweet.
                            twitterSentimentRadar.getUserObjectsSet().stream().filter(user -> user.getUsername().equals(userNameToLookFor))
                                    // Use stream and lambda expression to sort tweets of user by date. Comparing by date.
                                    .forEach(user -> user.getTweetsByUser().stream().sorted(Comparator.comparing(Tweet::getDate))
                                            // Add found tweets sorted by date to the set of all tweets by a user.
                                            .forEach(tweetsByUserSet::add));

                            // If user input equals 'all' get all tweets printed out regardless of user.
                            if (userNameToLookFor.equals("all")) {
                                // If input is 'all' all tweets sorted by date will be returned.
                                List<Tweet> allTweetsList = new ArrayList<>(tweetObjectsSet);
                                // Sort resulting list by date. Use of the Comparable <Tweet> interface see Tweet interface.
                                Collections.sort(allTweetsList);

                                for (Tweet tweet : allTweetsList) {
                                    System.out.println(tweet);
                                }

                            // If the set of tweets by user is empty return that no tweets for this username were found.
                            } else if (tweetsByUserSet.size() == 0) {
                                System.err.println("No tweets found for username " + "'" + userNameToLookFor + "'");

                            // Otherwise print out every tweet for the specific user.
                            } else {
                                // Print out every tweet for each tweet in the set of all tweets by a user.
                                for (Tweet userTweet : tweetsByUserSet) {
                                    System.out.println(userTweet);
                                }
                            }

                            break;

                        // Get all used hashtags without duplicates.
                        case "hashtags":

                            // Sort the list of all hashtags via stream() method by using a linked hashset.
                            Set<String> sortedSetOfAllHashtags = services.getAllHashtags(tweetObjectsSet).stream()
                                    .sorted().collect(Collectors.toCollection(LinkedHashSet::new));

                            // Print out every element from the resulting sorted list of all hashtags.
                            sortedSetOfAllHashtags.forEach(System.out::println);

                            break;

                        default:

                            System.err.println("No information available for this input. Try again.");

                    }

                    break;

                case "count":

                    // Submenu for count.
                    System.out.println("\t" + "Press '1' for average tweet length");
                    System.out.println("\t" + "Press '2' for average number of words in tweets");
                    System.out.println("\t" + "Press '3' for number of occurrences of every hashtag");
                    System.out.println("\t" + "Press '4' for average number of used hashtags");
                    System.out.println("\t" + "Press '5' to see users twittering long tweets");
                    System.out.println("\t" + "Press '6' to see which tweets contain the most hashtags");

                    userInput = userInputScanner.nextLine().toLowerCase();

                    switch (userInput) {

                        // Get average tweet length.
                        case "1":

                            double tweetContent = services.getAverageTweetLength(tweetObjectsSet);
                            System.out.println("Average tweet length: " + tweetContent);
                            break;

                        // Get average number of words in tweets.
                        case "2":

                            double averageNumberOfWordsInTweets = services.getAverageNumberOfWords(tweetObjectsSet);
                            System.out.println("Average number of words in tweets: " + averageNumberOfWordsInTweets);

                            break;

                        // Get all occurrences of hashtags. Without checking for retweet relation (implement maybe later).
                        case "3":

                            // Get the unsorted map of all hashtags.
                            Map<String, Integer> unsortedMapOfHashtags = services.getOccurrencesOfHashtags(services.getAllHashtags(tweetObjectsSet));

                            // Sort.
                            int finalIMin = 1;
                            unsortedMapOfHashtags.entrySet().stream().filter(e -> e.getValue() >= finalIMin).sorted((e1, e2) ->
                            {

                                int rInt = e1.getValue().compareTo(e2.getValue()) * -1;

                                if (rInt == 0) {
                                    rInt = e1.getKey().compareTo(e2.getKey());
                                }

                                return rInt;

                            }).forEach(t -> System.out.println(t.getKey() + " (" + t.getValue() + ")"));

                            break;

                        // Get average number of hashtags in tweets.
                        case "4":

                            // Method to get average number of hashtags.
                            double averageNumberOfHashtags = services.getAverageNumberOfHashtags(services.getAllHashtags(tweetObjectsSet), tweetObjectsSet);
                            System.out.println("Average number of hashtags in tweets: " + averageNumberOfHashtags);

                            break;

                        case "5":

                            Set<String> usersTwitteringLongTweetsStrings = services.getUsersTwitteringLongTweets(tweetObjectsSet);
                            System.out.println("Count of users with tweets above average length: " + usersTwitteringLongTweetsStrings.size());
                            System.out.println("Users twittering long tweets in alphabetical order: ");

                            for (String userString : usersTwitteringLongTweetsStrings)
                                System.out.println(userString);

                            break;

                        case "6":

                            // Listing and sorting of all tweets based on their use of hashtags. Tweets must have at least one hashtag.
                            tweetObjectsSet.stream().filter(tweet -> tweet.getHashtags().size() > 0)
                                    .sorted((t1, t2) -> Integer.compare(t1.getHashtags().size(), t2.getHashtags().size()) * -1)
                                    .forEach(tweet -> System.out.println("(" + tweet.getHashtags().size() + ") \t " + tweet));

                            break;

                        default:

                            // If no valid integer value is entered loop starts from the beginning (refers to while loop) with exception catching.
                            System.err.println("No information available for this integer input. Try again.");

                    }

                    break;

                case "search":

                    // Print all tweets that contain the entered search string in username or tweet content field.
                    System.out.print("Enter a string to search for in username or content field: ");

                    Scanner scanner = new Scanner(System.in);
                    String searchString = scanner.next();

                    // For each tweet it will be checked.
                    for (Tweet tweet : tweetObjectsSet) {
                        // Print out a tweet when it contains search string in username or content.
                        if (tweet.getUser().contains(searchString) || tweet.getText().contains(searchString)) {
                            System.out.println(tweet);
                        }
                    }

                    break;
            }
        }
    }

    /**
     * Constructor expects file path and runs init() method to read in tweets and users from a CSV file.
     *
     * @param filePath Path to the CSV file which should be processed.
     * @throws UIMAException If something goes wrong.
     */
    public TwitterSentimentRadar(String filePath) throws UIMAException {
        this.filePath = filePath;
        init();
    }

    /**
     * Method to fill set of tweet objects tweets and set of user objects with created users from CSV file.
     *
     * @throws UIMAException If something goes wrong.
     */
    public void init() throws UIMAException {
        // Read in all tweets from the CSV file with the help of the FileReaderHelper class.
        tweetObjectsSet = FileReaderHelper.convertReadInLines(filePath);
        // Empty set of all username strings.
        Set<String> usersNameStrings = new HashSet<>();

        // Get every username string for every tweet from the set of all tweet objects.
        for (Tweet tweet : tweetObjectsSet)
            usersNameStrings.add(tweet.getUser());

        // For each username string in the set of all username strings do the following.
        for (String userNameString : usersNameStrings) {
            // Filter out all tweets by a user from the set of all tweet objects which were read in with the help of the FileReaderHelper class before.
            Set<Tweet> tweetsOfUserSet = tweetObjectsSet.stream().filter
                    (t -> t.getUser().equals(userNameString)).collect(Collectors.toSet());

            // Initialize a new user object everytime and insert the username string as parameter for username.
            UserImpl user = new UserImpl(userNameString);

            // Add the set of all tweets by a user to the user object. The addMultipleTweetsToUser() method adds the tweets to a user.
            user.addMultipleTweetsToUser(tweetsOfUserSet);

            // Insert every single user object with a username and a set of tweets in the set of all user objects.
            userObjectsSet.add(user);
        }

        // Inform user about the number of read in tweets and users.
        System.out.println("Application is ready.");
        System.out.println("Number of read in tweets: " + getTweetObjectsSet().size());
        System.out.println("Number of users: " + getUserObjectsSet().size());
        System.out.println("==============================================");

    }

    // Getter methods for the attributes of TwitterSentimentRadar class.
    public Set<UserImpl> getUserObjectsSet() {
        return userObjectsSet;
    }

    public Set<Tweet> getTweetObjectsSet() {
        return tweetObjectsSet;
    }

    public static String getFilePath() {
        return filePath;
    }

    public static String getMongoPropertiesPath() {
        return mongoPropertiesPath;
    }

}

