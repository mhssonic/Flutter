package server.message.Tweet;

import server.database.RetweetDB;
import server.enums.error.ErrorType;

public class Retweet {
    private int retweetId;//TODO ?
    private int retweetedMessageId;
    private int retweeterId;

    public static ErrorType retweet( int retweetedMessageId, int retweeterId) {
        int retweetId = RetweetDB.createRetweet(retweetedMessageId, retweeterId);
        return ErrorType.SUCCESS;
    }

}

