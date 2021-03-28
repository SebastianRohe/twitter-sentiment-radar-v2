package com.project.sebastianrohe.twitter.data.impl;

import com.project.sebastianrohe.twitter.data.Tweet;
import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.bson.Document;

import java.util.Date;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Class to describe a tweet as document from the database.
 */
public class TweetMongoDBImpl implements Tweet {

    // Attribute containing document from database.
    Document tweetDocument;

    /**
     * Constructor.
     *
     * @param tweetDocument Actual document from database which represents tweet.
     */
    public TweetMongoDBImpl(Document tweetDocument) {
        this.tweetDocument = tweetDocument;
    }

    /**
     * Method to get document attribute.
     *
     * @return Document.
     */
    public Document getTweetDocument() {
        return this.tweetDocument;
    }

    @Override
    public long getId() {
        return tweetDocument.getLong("_id");
    }

    @Override
    public Date getDate() {
        return tweetDocument.getDate("date");
    }

    @Override
    public String getUser() {
        return tweetDocument.getString("user");
    }

    @Override
    public String getLanguage() {
        return tweetDocument.getString("language");
    }

    @Override
    public String getText() {
        return tweetDocument.getString("text");
    }

    @Override
    public Boolean getRetweet() {
        return tweetDocument.getBoolean("retweet");
    }

    @Override
    public long getRetweetId() {
        return tweetDocument.getLong("retweetId");
    }

    // ToDo ...
    @Override
    public void setRetweetId(long id) {

    }

    @Override
    public JCas toJCas() throws UIMAException {
        // Pattern to filter for problematic characters.
        Pattern charFilter = Pattern.compile("[^\\x{00}-\\x{024F}]");
        String tweetText = charFilter.matcher(this.getText()).replaceAll("");

        // Return JCas created from tweet text and language.
        return JCasFactory.createText(tweetText, this.getLanguage());
    }

    // ToDo ...
    @Override
    public Set<String> getHashtags() {
        return null;
    }

    @Override
    public int compareTo(Tweet tweet) {
        return this.getDate().compareTo(tweet.getDate());
    }

}
