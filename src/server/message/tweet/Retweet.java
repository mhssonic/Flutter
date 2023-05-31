package server.message.tweet;

import server.database.RetweetDB;
import server.enums.error.ErrorType;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Retweet extends Tweet {
    private int retweetId;//TODO ?
    private int retweetedMessageId;
    private int retweeterId;

    public Retweet(int messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<String> attachmentId) {
        super(messageId, authorId, text, postingTime, attachmentId);
    }

    public static ErrorType retweet(int retweetedMessageId, int retweeterId) {
        int retweetId = RetweetDB.createRetweet(retweetedMessageId, retweeterId);
        return shareTweetWithFollowers(retweeterId, retweetId);
    }

}

