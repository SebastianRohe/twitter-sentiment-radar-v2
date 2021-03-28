package com.project.sebastianrohe.twitter.data.impl;

import com.project.sebastianrohe.twitter.data.Tweet;
import com.project.sebastianrohe.twitter.data.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Class to describe a user object.
 *
 * @author Sebastian Rohe
 */
public class UserImpl implements User {

    // User attributes.
    private final String username;
    private Set<Tweet> tweetsByUser = new HashSet<>();

    /**
     * Constructor.
     *
     * @param username Name of User.
     */
    public UserImpl(String username) {
        this.username = username;
    }

    // Getter methods.
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public Set<Tweet> getTweetsByUser() {
        return this.tweetsByUser;
    }

    // Setter method to set tweets by user.
    @Override
    public void setTweetsByUser(Set<Tweet> tweets) {
        this.tweetsByUser = tweets;
    }

    @Override
    public void addTweetToUser(Tweet tweet) {
        this.tweetsByUser.add(tweet);
    }

    @Override
    public void addMultipleTweetsToUser(Set<Tweet> tweetSet) {
        for (Tweet tweet : tweetSet) {
            this.addTweetToUser(tweet);
        }
    }

    /**
     * Method to get string representation of user for console output.
     *
     * @return String representation of user objects for console.
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + this.username + '\'' +
                ", tweetsByUser=" + this.tweetsByUser +
                '}';
    }
}
