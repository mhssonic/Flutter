package server.database;

import server.enums.TweetType;
import server.message.tweet.Tweet;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
public class TweetDB extends SQLDB {
    public static int createTweet(int authorId, String context, Integer[] attachmentId, String[] hashtag, LocalDateTime postingTime) {
        try {
            Array hashtags = connection.createArrayOf("VARCHAR", hashtag);
            Array attachments = connection.createArrayOf("INT", attachmentId);
            //TODO attachment and id
            preparedStatement = connection.prepareStatement("INSERT INTO tweet (author ,favestar, hashtag, attachment , postingtime, context) VALUES (?,?,?,?,?,?) returning id");
            preparedStatement.setInt(1, authorId);
            preparedStatement.setBoolean(2, false);
            preparedStatement.setArray(3, hashtags);
            preparedStatement.setArray(4, attachments);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(postingTime));
            preparedStatement.setString(6, context);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean hasAccessToTweet(int userId, int tweetId) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM tweet WHERE id = ? and author = ?");
            preparedStatement.setInt(1, tweetId);
            preparedStatement.setInt(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeTweet(int messageId) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM tweet WHERE id = ?");
            preparedStatement.setInt(1, messageId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void likeFromTable(String table, int tweetId, int userId) {
        SQLDB.appendToArrayField(table, tweetId, "likes", userId);
    }

    public static void removeLikeFromTable(String table, int tweetId, int userId) {
        SQLDB.removeFromArrayField(table, tweetId, "likes", userId);
    }

    public static boolean likedBeforeFromTable(String table, int tweetId, int userId) {
        return SQLDB.containInArrayFieldObject(table, tweetId, "likes", userId);
    }

    public static int getNumberOfLikesFromTable(String table, int tweetId) {
        return sizeOfArrayField(table, tweetId, "likes");
    }

    public static void addToCommentFromTable(String table, int replayFrom, int tweetId) {
        SQLDB.appendToArrayField(table, replayFrom, "comments", tweetId);
    }

    public static void increaseRetweetFromTable(String table, int tweetId) {
        SQLDB.increaseFieldKeyByOne(table, tweetId, "retweet");
    }


    public static void like(int tweetId, int userId) {
        if(tweetId % TweetType.count == TweetType.TWEET.getMod()) likeFromTable("tweet", tweetId, userId);
        else if(tweetId % TweetType.count == TweetType.RETWEET.getMod()) likeFromTable("retweet", tweetId, userId);
        else if(tweetId % TweetType.count == TweetType.COMMENT.getMod()) likeFromTable("comment", tweetId, userId);
        else if(tweetId % TweetType.count == TweetType.QUOTE_TWEET.getMod()) likeFromTable("quote", tweetId, userId);
        else if(tweetId % TweetType.count == TweetType.POLL.getMod()) likeFromTable("poll", tweetId, userId);
    }

    public static void removeLike(int tweetId, int userId) {
        if(tweetId % TweetType.count == TweetType.TWEET.getMod()) removeLikeFromTable("tweet", tweetId, userId);
        else if(tweetId % TweetType.count == TweetType.RETWEET.getMod()) removeLikeFromTable("retweet", tweetId, userId);
        else if(tweetId % TweetType.count == TweetType.COMMENT.getMod()) removeLikeFromTable("comment", tweetId, userId);
        else if(tweetId % TweetType.count == TweetType.QUOTE_TWEET.getMod()) removeLikeFromTable("quote", tweetId, userId);
        else if(tweetId % TweetType.count == TweetType.POLL.getMod()) removeLikeFromTable("poll", tweetId, userId);
    }

    public static boolean likedBefore(int tweetId, int userId) {
        if(tweetId % TweetType.count == TweetType.TWEET.getMod()) return likedBeforeFromTable("tweet", tweetId, userId);
        if(tweetId % TweetType.count == TweetType.RETWEET.getMod()) return likedBeforeFromTable("retweet", tweetId, userId);
        if(tweetId % TweetType.count == TweetType.COMMENT.getMod()) return likedBeforeFromTable("comment", tweetId, userId);
        if(tweetId % TweetType.count == TweetType.QUOTE_TWEET.getMod()) return likedBeforeFromTable("quote", tweetId, userId);
        if(tweetId % TweetType.count == TweetType.POLL.getMod()) return likedBeforeFromTable("poll", tweetId, userId);
        return false;
    }

    public static int getNumberOfLikes(int tweetId) {
        if(tweetId % TweetType.count == TweetType.TWEET.getMod()) return getNumberOfLikesFromTable("tweet", tweetId);
        if(tweetId % TweetType.count == TweetType.RETWEET.getMod()) return getNumberOfLikesFromTable("retweet", tweetId);
        if(tweetId % TweetType.count == TweetType.COMMENT.getMod()) return getNumberOfLikesFromTable("comment", tweetId);
        if(tweetId % TweetType.count == TweetType.QUOTE_TWEET.getMod()) return getNumberOfLikesFromTable("quote", tweetId);
        if(tweetId % TweetType.count == TweetType.POLL.getMod()) return getNumberOfLikesFromTable("poll", tweetId);
        return 0;
    }

    public static void increaseRetweet(int tweetId) {
        if(tweetId % TweetType.count == TweetType.TWEET.getMod())  increaseRetweetFromTable("tweet", tweetId);
        if(tweetId % TweetType.count == TweetType.RETWEET.getMod())  increaseRetweetFromTable("retweet", tweetId);
        if(tweetId % TweetType.count == TweetType.COMMENT.getMod())  increaseRetweetFromTable("comment", tweetId);
        if(tweetId % TweetType.count == TweetType.QUOTE_TWEET.getMod())  increaseRetweetFromTable("quote", tweetId);
        if(tweetId % TweetType.count == TweetType.POLL.getMod())  increaseRetweetFromTable("poll", tweetId);
    }

    public static void setFaveStarToTable(String table, int tweetId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("favestar", true);
        updateFieldsKeys(table, tweetId, map);
    }

    public static void setFaveStar(int tweetId){
        if(tweetId % TweetType.count == TweetType.TWEET.getMod()) setFaveStarToTable("tweet", tweetId);
        else if(tweetId % TweetType.count == TweetType.RETWEET.getMod()) setFaveStarToTable("retweet", tweetId);
        else if(tweetId % TweetType.count == TweetType.COMMENT.getMod()) setFaveStarToTable("comment", tweetId);
        else if(tweetId % TweetType.count == TweetType.QUOTE_TWEET.getMod()) setFaveStarToTable("quote", tweetId);
        else if(tweetId % TweetType.count == TweetType.POLL.getMod()) setFaveStarToTable("poll", tweetId);
    }

    public static void addToComment(int replayFrom, int tweetId) {
        if(replayFrom % TweetType.count == TweetType.TWEET.getMod()) addToCommentFromTable("tweet", replayFrom, tweetId);
        else if(replayFrom % TweetType.count == TweetType.RETWEET.getMod()) addToCommentFromTable("retweet", replayFrom, tweetId);
        else if(replayFrom % TweetType.count == TweetType.COMMENT.getMod()) addToCommentFromTable("comment", replayFrom, tweetId);
        else if(replayFrom % TweetType.count == TweetType.QUOTE_TWEET.getMod()) addToCommentFromTable("quote", replayFrom, tweetId);
        else if(replayFrom % TweetType.count == TweetType.POLL.getMod()) addToCommentFromTable("poll", replayFrom, tweetId);
    }

    public static Tweet getTweet(int messageId) {

        try{
            ResultSet resultSet = getResultSet("tweet" , messageId);
            if (!resultSet.next()) return null;

            int author = resultSet.getInt("author");
            String context = resultSet.getString("context");

            Object[] attachmentId = null;
            Array attachments =  (resultSet.getArray("attachment"));
            if (attachments != null){
                attachmentId = (Object[]) attachments.getArray();
            }

            int retweet = resultSet.getInt("retweet");
            int likes = sizeOfArrayField("tweet", messageId, "likes");

            Object[] commentId = {};
            Array comments =  (resultSet.getArray("comments"));
            if (comments != null){
                 commentId = (Object[]) comments.getArray();
            }

            Object[] hashtag = {};
            Array hashtags =  (resultSet.getArray("hashtag"));
            if (hashtags != null){
                hashtag = (Object[]) hashtags.getArray();
            }

            LocalDateTime postingTime = resultSet.getTimestamp("postingTime").toLocalDateTime();

            ArrayList<Integer> attachment  = new ArrayList<>();
            for(Object obj : attachmentId){
                attachment.add((Integer) obj);
            }

            String[] strHashtag = Arrays.copyOf(hashtag, hashtag.length, String[].class);
            Integer[] intComment = Arrays.copyOf(commentId, commentId.length, Integer[].class);

            Tweet tweet = new Tweet(messageId , author , context , postingTime , attachment,  likes, intComment, strHashtag, retweet);
            return tweet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}