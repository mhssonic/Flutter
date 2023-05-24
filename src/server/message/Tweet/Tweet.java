package server.message.Tweet;

import server.Tools;
import server.database.*;
import server.enums.error.ErrorHandling;
import server.enums.error.ErrorType;
import server.message.Attachment;
import server.user.User;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

public class Tweet {
    HashSet<User> like = new HashSet<>();
    HashSet<String> comment;
    ArrayList<String> hashtag;
    int retweetCount;
    Boolean faveStar;
    final static int MAX_LENGTH_TWEET = 160;

    public static void main(String[] args) {
        SQLDB.run();
        tweet(-1999999990, "hi flutter its me your dad :) and your god :| i can do whatever i want with you and you can't do shit. from Bible Gateway 1 Corinthians 1", null, null);
    }

    public static ErrorType tweet(int userId, String context, ArrayList<Attachment> attachments, Integer[] hashtag){
        SQLDB.run(); //TODO  WHY?

        Integer[] attachmentId = new Integer[attachments.size()];
        int i = 0;
        for ( Attachment attachment : attachments) {
            attachmentId[i] = AttachmentDB.createAttachment(attachment);
            i++;
        }

        int tweetId = TweetDB.createTweet(userId, context, attachmentId, hashtag, LocalDateTime.now());
        try {
            Object[] follower = (Object[])(UserDB.getFollower(userId).getArray());
            ErrorType errorType = ErrorHandling.validLength(context, MAX_LENGTH_TWEET);
            if (errorType != ErrorType.SUCCESS) {
                return errorType;
            }
            for(Object targetUser: follower){
                ChatBoxDB.appendMessage(Tools.jenkinsHash((int)targetUser, (int)targetUser, false), tweetId);
            }
            return ErrorType.SUCCESS;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ErrorType.SUCCESS;
        }
    }
}
