package server.message.Tweet;

import server.Tools;
import server.database.*;
import server.enums.FileType;
import server.enums.error.ErrorHandling;
import server.enums.error.ErrorType;
import server.message.Attachment;
import server.message.Message;
import server.user.User;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

public class Tweet extends Message{
    HashSet<User> like = new HashSet<>();
    HashSet<String> comment;
    ArrayList<String> hashtag;
    int retweetCount;
    Boolean faveStar;
    final static int FAVESTAR_NUMBER = 10;

    public Tweet(int messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<String> attachmentId) {
        super(messageId, authorId, text, postingTime, attachmentId);
    }


    public static ErrorType tweet(int userId, String context, ArrayList<Attachment> attachments, Integer[] hashtag){
        Integer[] attachmentId = AttachmentDB.creatAttachments(attachments);
        int tweetId = TweetDB.createTweet(userId, context, attachmentId, hashtag, LocalDateTime.now());
        if (validTweet(context) == ErrorType.SUCCESS){
            return shareTweetWithFollowers(userId,tweetId);
        }
        else return validTweet(context);
    }


    public static ErrorType validTweet(String context){
        //TODO override maybe?
        return validMessage(context);
    }

    public static ErrorType shareTweetWithFollowers(int userId, int tweetId){
        try {
            Object[] follower = (Object[])(UserDB.getFollower(userId).getArray());
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
        if(TweetDB.getNumberOfLikes(tweetId) >= FAVESTAR_NUMBER)
            faveStar(tweetId);
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

    public static void faveStar(int tweetId){
        TweetDB.setFaveStar(tweetId);
        ArrayList<Object> users = UserDB.getUsersId();
        for(Object objUserId : users){
            try {
                int userId = (int)objUserId;
                ChatBoxDB.appendMessage(Tools.jenkinsHash(userId, userId, false),tweetId);
            }catch (Exception e){}
        }
    }
}
