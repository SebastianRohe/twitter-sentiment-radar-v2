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
 * Class to describe a tweet from the com.project.sebastianrohe.twitter.database.
 */
public class TweetMongoDBImpl implements Tweet {

    // Attribute should contains document from com.project.sebastianrohe.twitter.database.
    Document tweetDocument;

    /**
     * Constructor.
     *
     * @param tweetDocument Actual document from com.project.sebastianrohe.twitter.database which represents tweet.
     */
    public TweetMongoDBImpl(Document tweetDocument) {
        this.tweetDocument = tweetDocument;
    }

    @Override
    public long getId() {
        return tweetDocument.getLong("_id");
    }

    @Override
    public String getText() {
        return tweetDocument.getString("text");
    }

    @Override
    public String getUser() {
        return tweetDocument.getString("user");
    }

    @Override
    public Boolean getRetweet() {
        return tweetDocument.getBoolean("retweet");
    }

    @Override
    public String getLanguage() {
        return tweetDocument.getString("language");
    }

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public long getRetweetId() {
        return tweetDocument.getLong("retweetId");
    }

    @Override
    public JCas toJCas() throws UIMAException {
        // This filters problematic characters.
        Pattern charFilter = Pattern.compile("[^\\x{00}-\\x{024F}]");
        String tweetText = charFilter.matcher(this.getText()).replaceAll("");

        JCas jCas = JCasFactory.createText(tweetText, this.getLanguage());
        return jCas;
    }

    @Override
    public Set<String> getHashtags() {
        return null;
    }

    @Override
    public void setRetweetId(long id) {

    }

    @Override
    public int compareTo(Tweet tweet) {
        return this.getDate().compareTo(tweet.getDate());
    }

    public Document getTweetDocument() {
        return this.tweetDocument;
    }
}
