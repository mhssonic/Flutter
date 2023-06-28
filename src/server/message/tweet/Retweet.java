package server.message.tweet;

import server.database.RetweetDB;
import server.enums.error.ErrorType;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Retweet extends Tweet {
    private int retweetId;//TODO ?
    private int retweetedMessageId;
    private int retweeterId;

    public Retweet(int messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<Integer> attachmentId, int likes, Integer[] comment, String[] hashtag, int retweetCount, int retweetId, int retweeterId) {
        super(messageId, authorId, text, postingTime, attachmentId, likes,  comment,  hashtag,  retweetCount);
        this.retweetId = retweetId;
        this.retweeterId = retweeterId;
    }

    public Retweet(){}


    public static ErrorType retweet(int retweetedMessageId, int retweeterId) {
        int retweetId = RetweetDB.createRetweet(retweetedMessageId, retweeterId);
        return shareTweetWithFollowers(retweeterId, retweetId);
    }

    public int getRetweetId() {
        return retweetId;
    }

    public void setRetweetId(int retweetId) {
        this.retweetId = retweetId;
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

