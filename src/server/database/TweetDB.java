package server.database;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;

public class TweetDB extends SQLDB {
    public static int createTweet(int authorId, String context, Integer[] attachmentId, Integer[] hashtag, LocalDateTime postingTime) {
        try {
            Array hashtags = connection.createArrayOf("INT", hashtag);
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

    public static boolean hasAccessToTweet(int userId, int tweetId){
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

    public static void like(int tweetId, int userId) {
        SQLDB.appendToArrayField("tweet", tweetId, "likes", userId);
    }

    public static void removeLike(int tweetId, int userId) {
        SQLDB.removeFromArrayField("tweet", tweetId, "likes", userId);
    }

    public static boolean likedBefore(int userId, int tweetId){
        return SQLDB.containInArrayFieldObject("tweet", tweetId, "likes", userId);
    }

    public static int getNumberOfLikes(int tweetId){
        return sizeOfArrayField("tweet", tweetId, "likes");
    }

    public static void setFaveStar(int tweetId){
        HashMap<String, Object> map = new HashMap<>();
        map.put("favestar", true);
        updateFieldsKeys("tweet", tweetId, map);
    }
}