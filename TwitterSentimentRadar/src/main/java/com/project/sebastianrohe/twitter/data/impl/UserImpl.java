package com.project.sebastianrohe.twitter.data.impl;

import com.project.sebastianrohe.twitter.data.Tweet;

import java.util.HashSet;
import java.util.Set;

public class UserImpl {

    private final String username;
    private Set<TweetImpl> tweetsByUser = new HashSet<>();

    public String getUsername() {
        return this.username;
    }

    public Set<TweetImpl> getTweets() {
        return this.tweetsByUser;
    }

    public void setTweets(Set<TweetImpl> tweets) {
        this.tweetsByUser = tweets;
    }

    public UserImpl(String username) {
        this.username = username;
    }

    // Method to add a tweet to the tweetsByUser set.
    public void addTweetToUser(TweetImpl tweet) {
        this.tweetsByUser.add(tweet);
    }

    public void addMultipleTweetsToUser(Set<Tweet> tweetSet) {

        for (Tweet tweet : tweetSet)
            this.addTweetToUser((TweetImpl) tweet);

    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + this.username + '\'' +
                ", tweetsByUser=" + this.tweetsByUser +
                '}';
    }
}
