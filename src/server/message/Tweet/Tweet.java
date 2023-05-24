package server.message.Tweet;

import server.Tools;
import server.database.ChatBoxDB;
import server.database.TweetDB;
import server.database.UserDB;
import server.enums.error.ErrorHandling;
import server.enums.error.ErrorType;
import server.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

public class Tweet {
    HashSet<User> like = new HashSet<>();
    HashSet<String> comment;
    ArrayList<String> hashtag;
    int retweet;
    Boolean faveStar;

//    public static ErrorType tweet(String userId, String context, ArrayList<String> attachments, ArrayList<String> hashtag){
//        String tweetId = TweetDB.createTweet(userId, context, attachments, hashtag, LocalDateTime.now());
//        ArrayList<String> follower = UserDB.getFollower(userId);
//        ErrorType errorType = ErrorHandling.validMessage(context);
//        if (errorType != ErrorType.SUCCESS)
//            return errorType;
//        for(String targetUser: follower){
//            ChatBoxDB.append(Tools.jenkinsHash(userId, userId, false), tweetId);
//        }
//        return ErrorType.SUCCESS;
//    }
}
