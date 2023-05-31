package server.message.tweet;

import server.database.RetweetDB;
import server.enums.error.ErrorType;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Retweet extends Tweet {
    private int retweetId;//TODO ?
    private int retweetedMessageId;
    private int retweeterId;

    public Retweet(int messageId, int authorId, String text, LocalDateTime postingTime, Object[] attachmentId, int likes, int retweetId, int retweeterId) {
        super(messageId, authorId, text, postingTime, attachmentId, likes);
        this.retweetId = retweetId;
        this.retweeterId = retweeterId;
    }

    public static ErrorType retweet(int retweetedMessageId, int retweeterId) {
        int retweetId = RetweetDB.createRetweet(retweetedMessageId, retweeterId);
        return shareTweetWithFollowers(retweeterId, retweetId);
    }

}

