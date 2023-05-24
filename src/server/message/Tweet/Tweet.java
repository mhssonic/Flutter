package server.message.Tweet;

import server.Tools;
import server.database.ChatBoxDB;
import server.database.SQLDB;
import server.database.TweetDB;
import server.database.UserDB;
import server.enums.error.ErrorHandling;
import server.enums.error.ErrorType;
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
        tweet(-2000000000, "hi flutter its me your dad :) and your god :| i can do whatever i want with you and you can't do shit. from Bible Gateway 1 Corinthians 1", null, null);
    }

    public static ErrorType tweet(int userId, String context, Integer[] attachments, Integer[] hashtag){
        SQLDB.run();
        int tweetId = TweetDB.createTweet(userId, context, attachments, hashtag, LocalDateTime.now());
        try {
            int[] follower = (int[])(UserDB.getFollower(userId).getArray());
            ErrorType errorType = ErrorHandling.validLength(context, MAX_LENGTH_TWEET);
            if (errorType != ErrorType.SUCCESS) {
                return errorType;
            }
            for(int targetUser: follower){
                ChatBoxDB.appendMessage(Tools.jenkinsHash(targetUser, targetUser, false), tweetId);
            }
            return ErrorType.SUCCESS;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ErrorType.SUCCESS;
        }
    }
}
