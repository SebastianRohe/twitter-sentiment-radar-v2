package com.project.sebastianrohe.twitter.services;

import com.project.sebastianrohe.twitter.data.Tweet;

import java.util.*;

public class TweetService {

    /**
     * This method takes a set of users and tweets and fills the user set with the user strings of every tweet.
     * Requires a set of tweets and a set of users.
     *
     * @param tweetSet A set of tweet objects.
     * @return Set of all users from tweets.
     */
    public Set<String> getAllUsers(Set<Tweet> tweetSet) {
        Set<String> userSet = new HashSet<>();

        // For every tweet the user string gets added as element in user set.
        for (Tweet tweet : tweetSet) {
            userSet.add(tweet.getUser());
        }

        // Return resulting userSet.
        return userSet;
    }

    /**
     * Method to calculate the average length of tweet content.
     * Requires a set of tweets and a set of users.
     *
     * @param tweetSet A set of tweet objects.
     * @return Average length of tweet content as double value.
     */
    public double getAverageTweetLength(Set<Tweet> tweetSet) {
        // Sum of tweet contents is initialized with 0
        int sum = 0;

        // Iterating over every tweet from the tweet set and adding the length of every content string
        // to sum of all tweet contents.
        for (Tweet tweet : tweetSet) {
            sum += tweet.getText().length(); // length of the content of every tweet is added to sum
        }

        // Divide the sum of tweet contents by the size of the tweet set (number of tweets in set).
        // Attention: To get exact result the size of tweetSet is cast to double.
        return sum / (double) tweetSet.size();

    }

    /**
     * Method to calculate the average word count in tweets. Requires a set of tweets.
     *
     * @param tweetSet A set of tweet objects.
     * @return Average number of words in tweets.
     */
    public double getAverageNumberOfWords(Set<Tweet> tweetSet) {
        // Number of words for all tweets is 0 at the start.
        int numberOfWordsInAllTweets = 0;

        // For every tweet its content is split in an array of the words.
        for (Tweet tweet : tweetSet) {
            String tweetContentString = tweet.getText();
            // Tweet content strings are split with regex "[\\s+]" (blank space) to get an array of words.
            String[] words = tweetContentString.split("[\\s+]");
            // Length value of word array is added to numberOfWordsInAllTweets.
            numberOfWordsInAllTweets += words.length;
        }
        // Divide the number if words in all tweets by the size of the given tweet set and return.
        return numberOfWordsInAllTweets / (double) tweetSet.size();
    }

    /**
     * This method calculates the average number of hashtags of the tweets. It requires a set of tweets.
     *
     * @param tweetSet A set of tweet objects.
     * @return Average number of hashtags in tweets.
     */
    public double getAverageNumberOfHashtags(List<String> usedHashtagsList, Set<Tweet> tweetSet) {
        // Calculate average number of used hashtags and return it.
        return usedHashtagsList.size() / (double) tweetSet.size();
    }

    /**
     * Method to get all hashtags from a set of tweets.
     *
     * @param tweetSet A set of tweet objects.
     * @return List of hashtag strings.
     */
    public List<String> getAllHashtags(Set<Tweet> tweetSet) {
        // Create empty List of hashtags.
        List<String> usedHashtags = new ArrayList<>();
        Set<String> hashtagsStringSet;

        // Iterating for every tweet in tweetSet.
        for (Tweet tweet : tweetSet) {
            hashtagsStringSet = tweet.getHashtags();
            // Nested for loop to get every String from the string set and add it to list of used hashtags.
            usedHashtags.addAll(hashtagsStringSet);
        }
        // Return resulting list of all hashtags.
        return usedHashtags;
    }


    /**
     * Method to count the occurrences of hashtags.
     *
     * @param listOfAllUsedHashtags List of all used hashtags.
     * @return A map of hashtags with their number of occurrences.
     */
    public Map<String, Integer> getOccurrencesOfHashtags(List<String> listOfAllUsedHashtags) {
        // hashmap to store the frequency of element
        Map<String, Integer> occurrencesHashMap = new HashMap<>();

        for (String hashtagString : listOfAllUsedHashtags) {
            Integer j = occurrencesHashMap.get(hashtagString);
            occurrencesHashMap.put(hashtagString, (j == null) ? 1 : j + 1);
        }
        return occurrencesHashMap;
    }

    /**
     * Method to get users which twittered tweets above average length.
     *
     * @param tweetObjectSet A set of tweet objects.
     * @return Set of username strings.
     */
    public Set<String> getUsersTwitteringLongTweets(Set<Tweet> tweetObjectSet) {
        // Using TreeSet to get alphabetical order.
        Set<String> usersTwitteringLongTweets = new TreeSet<>();
        // Use method to get average tweet length.
        double averageTweetLength = getAverageTweetLength(tweetObjectSet);

        for (Tweet tweet : tweetObjectSet) {
            if (tweet.getText().length() > averageTweetLength)
                // Compare all that are bigger than the average and add.
                usersTwitteringLongTweets.add(tweet.getUser());

        }
        // Return set of strings representing users with long tweets.
        return usersTwitteringLongTweets;
    }

}
