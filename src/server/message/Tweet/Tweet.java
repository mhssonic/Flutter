package server.message.Tweet;

import server.Tools;
import server.database.*;
import server.enums.FileType;
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
//        ArrayList<Attachment> attachments = new ArrayList<>();
//        attachments.add(new Attachment("123" , FileType.VIDEO));
//        tweet(-2000000000, "hi flutter its me your dad :) and your god :| i can do whatever i want with you and you can't do shit. from Bible Gateway 1 Corinthians 1", attachments, new Integer[1]);
//        like(-2000000000,-2000000000);
//        removeLike(-2000000000,-2000000000);
        removeTweet(-2000000000,-2000000000);

    }

    public static ErrorType tweet(int userId, String context, ArrayList<Attachment> attachments, Integer[] hashtag){

        Integer[] attachmentId = AttachmentDB.creatAttachments(attachments);
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

    public static ErrorType like(int userId, int tweetId){
        if(TweetDB.likedBefore(userId, tweetId))
            return ErrorType.ALREADY_LIKED;
        TweetDB.like(tweetId, userId);
        return ErrorType.SUCCESS;
    }

    public static ErrorType removeLike(int userId, int tweetId){
        if(!TweetDB.likedBefore(userId, tweetId))
            return ErrorType.HAVE_NOT_LIKED;
        TweetDB.removeLike(tweetId, userId);
        return ErrorType.SUCCESS;
    }

    public static ErrorType removeTweet(int userId, int tweetId){
        if(!TweetDB.hasAccessToTweet(userId, tweetId))
            return ErrorType.PERMISSION_DENIED;
        TweetDB.removeTweet(tweetId);
        return ErrorType.SUCCESS;
    }
}
