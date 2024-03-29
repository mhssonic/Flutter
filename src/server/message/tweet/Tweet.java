package server.message.tweet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import server.Tools;
import server.database.AttachmentDB;
import server.database.ChatBoxDB;
import server.database.TweetDB;
import server.database.UserDB;
import server.enums.error.ErrorType;
import server.message.Attachment;
import server.message.Message;
import server.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Tweet extends Message{
    @JsonIgnore
    HashSet<User> like = new HashSet<>();
    int likes;
    Integer[] comment;
    String[] hashtag;
    @JsonProperty("retweet-count")
    int retweetCount;
    Boolean faveStar;
    final static int FAVESTAR_NUMBER = 10;

    public Tweet(Object messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<Integer> attachmentId, int likes, Integer[] comment, String[] hashtag, int retweetCount) {
        super(messageId, authorId, text, postingTime, attachmentId);
        this.likes = likes;
        this.retweetCount = retweetCount;
        this.hashtag = hashtag;
        this.comment = comment;
    }
    public Tweet(){
        super();
    }


    public HashSet<User> getLike() {
        return like;
    }

    public int getLikes() {
        return likes;
    }

    public Integer[] getComment() {
        return comment;
    }

    public String[] getHashtag() {
        return hashtag;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public Boolean getFaveStar() {
        return faveStar;
    }

    public static ErrorType tweet(int userId, String context, ArrayList<Integer> attachments, String[] hashtag ){
        if (attachments != null && !AttachmentDB.checkAttachments(attachments))
            return ErrorType.DOESNT_EXIST;
        if (validTweet(context) == ErrorType.SUCCESS){
            int tweetId;
            if(attachments != null)
                tweetId = TweetDB.createTweet(userId, context, attachments.toArray(new Integer[attachments.size()]), hashtag, LocalDateTime.now());
            else
                tweetId = TweetDB.createTweet(userId, context, new Integer[0], hashtag, LocalDateTime.now());
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
        if(TweetDB.likedBefore(tweetId, userId))
            return ErrorType.ALREADY_LIKED;
      
        TweetDB.like(tweetId, userId);
        if(TweetDB.getNumberOfLikes(tweetId) >= FAVESTAR_NUMBER)
            faveStar(tweetId);
        return ErrorType.SUCCESS;
    }

    public static ErrorType removeLike(int userId, int tweetId){
        if(!TweetDB.likedBefore(tweetId, userId))
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
    public static void showTweet(int userId , int start , int finish){
        int chatBoxId = Tools.jenkinsHash(userId, userId, false);
        showMessage(start , finish , chatBoxId);
    }

    public void setLike(HashSet<User> like) {
        this.like = like;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setComment(Integer[] comment) {
        this.comment = comment;
    }

    public void setHashtag(String[] hashtag) {
        this.hashtag = hashtag;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public void setFaveStar(Boolean faveStar) {
        this.faveStar = faveStar;
    }
}
