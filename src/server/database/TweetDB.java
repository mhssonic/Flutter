package server.database;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class TweetDB extends SQLDB {
    public static void createTweet(String tweetId, String authorId, String context, Object[] attachmentId, Object[] hashtag) {
        LocalDateTime postingTime = LocalDateTime.now();
        try {
            Array hashtags = connection.createArrayOf("text", hashtag);
            Array attachments = connection.createArrayOf("text", attachmentId);
            //TODO attachmenst and id
            preparedStatement = connection.prepareStatement("INSERT INTO profile (id, athour ,favestar, hashtag, attachmenst , poatingtime) VALUES (?,?,?,?,?,?)");
            preparedStatement.setString(1, tweetId);
            preparedStatement.setString(2, authorId);
            preparedStatement.setBoolean(3, false);
            preparedStatement.setArray(4, hashtags);
            preparedStatement.setArray(5, attachments);
            preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void removeTweet(int messageId) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM tweet WHERE id = ?");
            preparedStatement.setInt(1 , messageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}