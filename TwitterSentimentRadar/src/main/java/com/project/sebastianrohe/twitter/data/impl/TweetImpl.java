package com.project.sebastianrohe.twitter.data.impl;

import com.project.sebastianrohe.twitter.data.Tweet;
import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to describe a tweet object.
 *
 * @author Sebastian Rohe
 */
public class TweetImpl implements Tweet {

    // Tweet attributes.
    private final long id;
    private final Date date;
    private final String user;
    private final String language;
    private final String text;
    private final Boolean retweet;

    // Retweet id has default value -1, because not every line of the csv file includes a retweet id.
    private long retweetId = -1;

    // Every tweet has a JCas object.
    protected JCas jCas = null;

    /**
     * Constructor.
     *
     * @param id       Tweet id.
     * @param date     Tweet date.
     * @param user     Name of tweet author.
     * @param language Tweet language.
     * @param text     Tweet text.
     * @param retweet  Check boolean for retweet relation.
     * @throws UIMAException If something goes wrong.
     */
    public TweetImpl(long id, Date date, String user, String language, String text, boolean retweet) throws UIMAException {
        this.id = id;
        this.date = date;
        this.user = user;
        this.language = language;
        this.text = text;
        this.retweet = retweet;
    }

    // All getter methods.
    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public String getLanguage() {
        return this.language;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public Boolean getRetweet() {
        return this.retweet;
    }

    @Override
    public long getRetweetId() {
        return this.retweetId;
    }

    // Setter method to set the retweet id with other value than -1.
    @Override
    public void setRetweetId(long id) {
        this.retweetId = id;
    }

    /**
     * Method to convert tweet to JCas.
     *
     * @return JCas.
     * @throws UIMAException If something goes wrong.
     */
    @Override
    public JCas toJCas() throws UIMAException {
        // If JCas is not empty return JCas.
        if (this.jCas != null) {
            return this.jCas;
        }

        // Otherwise create JCas with tweet content and language.
        else {
            return JCasFactory.createText(this.getText(), this.getLanguage());
        }
    }

    /**
     * Method to set JCas of tweet.
     *
     * @param jCas JCas to set.
     * @throws UIMAException If something goes wrong.
     */
    public void setJCas(JCas jCas) throws UIMAException {
        this.jCas = jCas;
    }

    /**
     * Method to get hashtags from tweet objects created out of the data from the csv file.
     *
     * @return Returns a set of hashtag strings.
     */
    public Set<String> getHashtags() {
        Set<String> allHashtagsSet = new HashSet<>();

        // Regex to describe valid pattern of hashtags.
        String hashtagRegex = "(#[A-Za-z0-9-_]+)(?:#[A-Za-z0-9-_]+)*\\b";
        Pattern tagMatcher = Pattern.compile(hashtagRegex);
        Matcher matcher = tagMatcher.matcher(this.getText());

        while (matcher.find()) {
            String tag = matcher.group(1);
            allHashtagsSet.add(tag);
        }
        // Return a set of all 'found' hashtags.
        return allHashtagsSet;
    }

    /**
     * Method to make tweets comparable by date.
     *
     * @param tweet Tweet object.
     * @return Integer value for comparison.
     */
    @Override
    public int compareTo(Tweet tweet) {
        return this.getDate().compareTo(tweet.getDate());
    }

    /**
     * Method to get string representation of tweet for console output.
     *
     * @return String representation of tweet objects for console.
     */
    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + this.id +
                ", date=" + this.date +
                ", user='" + this.user + '\'' +
                ", language='" + this.language + '\'' +
                ", content='" + this.text + '\'' +
                ", retweet=" + this.retweet +
                ", retweetId=" + this.retweetId +
                '}';
    }

}
