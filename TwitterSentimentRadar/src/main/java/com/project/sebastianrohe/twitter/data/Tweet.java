package com.project.sebastianrohe.twitter.data;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;

import java.util.Date;
import java.util.Set;

/**
 * Interface to represent tweet.
 *
 * @author Sebastian Rohe
 */
public interface Tweet extends Comparable<Tweet> {

    /**
     * Get id of tweet.
     *
     * @return long
     */
    long getId();

    /**
     * Get the creation date of tweet.
     *
     * @return Date the tweet was created.
     */
    Date getDate();

    /**
     * Get username of the creator.
     *
     * @return String.
     */
    String getUser();

    /**
     * Get language of tweet.
     *
     * @return String.
     */
    String getLanguage();

    /**
     * Get text of tweet.
     *
     * @return String.
     */
    String getText();

    /**
     * Method to determinate if a tweet is a retweet. If true tweet is retweet.
     *
     * @return Boolean retweet value.
     */
    Boolean getRetweet();

    /**
     * Get the retweet id of tweet.
     *
     * @return Retweet id.
     */
    long getRetweetId();

    /**
     * Method to set the retweet id with other value than default value -1.
     *
     * @param id Id to set to.
     */
    void setRetweetId(long id);

    /**
     * Create JCas from tweet text and language.
     *
     * @return JCas.
     */
    JCas toJCas() throws UIMAException;

    /**
     * Method to return all used hashtags from a tweet.
     *
     * @return Set of hashtags strings.
     */
    Set<String> getHashtags();

}
