package server.message.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.database.RetweetDB;
import server.database.SQLDB;
import server.database.TweetDB;
import server.enums.error.ErrorType;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Retweet extends Tweet {
    private int retweetedMessageId;
    @JsonProperty
    private int retweeterId;

    public Retweet(int messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<Integer> attachmentId, int likes, Integer[] comment, String[] hashtag, int retweetCount, int retweetId, int retweeterId) {
        super(messageId, authorId, text, postingTime, attachmentId, likes,  comment,  hashtag,  retweetCount);
        this.retweetedMessageId = retweetId;
        this.retweeterId = retweeterId;
    }

    public Retweet(){}


    public static ErrorType retweet(int retweetedMessageId, int retweeterId) {
        int retweetId = RetweetDB.createRetweet(retweetedMessageId, retweeterId);
        TweetDB.increaseRetweet(retweetedMessageId);
        return shareTweetWithFollowers(retweeterId, retweetId);
    }

    public int getRetweetedMessageId() {
        return retweetedMessageId;
    }

    public void setRetweetedMessageId(int retweetedMessageId) {
        this.retweetedMessageId = retweetedMessageId;
    }

    public int getRetweeterId() {
        return retweeterId;
    }

    public void setRetweeterId(int retweeterId) {
        this.retweeterId = retweeterId;
    }
}

