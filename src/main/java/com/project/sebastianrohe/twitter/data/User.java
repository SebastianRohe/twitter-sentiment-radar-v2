package com.project.sebastianrohe.twitter.data;

import com.project.sebastianrohe.twitter.data.impl.TweetImpl;

import java.util.Set;

/**
 * Interface to represent user.
 *
 * @author Sebastian Rohe
 */
public interface User {

    /**
     * Get username of the user.
     *
     * @return String.
     */
    String getUsername();

    /**
     * Method to get all tweets by a user.
     *
     * @return Set of tweets.
     */
    Set<Tweet> getTweetsByUser();

    /**
     * Set the tweets by a user.
     *
     * @param tweets A set of tweets.
     */
    void setTweetsByUser(Set<Tweet> tweets);

    /**
     * Add a tweet to the set of all tweets by a user.
     *
     * @param tweet tweet.
     */
    void addTweetToUser(Tweet tweet);

    /**
     * Add multiple tweets to the set of all tweets by a user.
     *
     * @param tweetSet Set of tweets.
     */
    void addMultipleTweetsToUser(Set<Tweet> tweetSet);

}
