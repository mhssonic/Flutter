package server.database;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CommentDB extends TweetDB{
    public static int createComment(int authorId, String context, Integer[] attachmentId, String[] hashtag, LocalDateTime postingTime, int reply) {
        try {
            Array hashtags = connection.createArrayOf("VARCHAR(50)", hashtag);
            Array attachments = connection.createArrayOf("INT", attachmentId);
            //TODO attachment and id
            preparedStatement = connection.prepareStatement("INSERT INTO tweet (author ,favestar, hashtag, attachment , postingtime, context, reply) VALUES (?,?,?,?,?,?,?) returning id");
            preparedStatement.setInt(1, authorId);
            preparedStatement.setBoolean(2, false);
            preparedStatement.setArray(3, hashtags);
            preparedStatement.setArray(4, attachments);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(postingTime));
            preparedStatement.setString(6, context);
            preparedStatement.setInt(7, reply);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
