package com.project.sebastianrohe.twitter.data;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;

import java.util.Date;
import java.util.Set;

public interface Tweet extends Comparable<Tweet> {

    /**
     * Get ID of Tweet.
     *
     * @return long
     */
    long getId();

    /**
     * Get content of tweet.
     *
     * @return String
     */
    String getText();

    /**
     * Get username of the owner.
     *
     * @return String
     */
    String getUser();

    /**
     * Get the retweeted tweet, if existing.
     *
     * @return Tweet
     */
    Boolean getRetweet();

    /**
     * Get language of Tweet.
     *
     * @return String
     */
    String getLanguage();

    /**
     * Get the creation date of tweet.
     *
     * @return Date the tweet was created.
     */
    Date getDate();

    /**
     * Get the retweet id of tweet.
     *
     * @return Retweet id.
     */
    long getRetweetId();

    /**
     * Convert tweet text to JCas.
     *
     * @return JCas
     */
    JCas toJCas() throws UIMAException;

    /**
     * Method to return all used hashtags from a tweet.
     *
     * @return Set<String> Set of hashtags strings.
     */
    Set<String> getHashtags();

    /**
     * Method to set the retweet id with other value than default value -1.
     *
     * @param id Id to be set.
     */
    void setRetweetId(long id);
}
