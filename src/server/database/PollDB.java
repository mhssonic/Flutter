package server.database;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static server.database.SQLDB.connection;

public class PollDB extends SQLDB{
    public static int createPoll(int authorId, String context, Integer[] attachmentId, Integer[] hashtag, LocalDateTime postingTime , Integer[] choiceId) {
        try {
            Array hashtags = connection.createArrayOf("INT", hashtag);
            Array attachments = connection.createArrayOf("INT", attachmentId);
            Array choices = connection.createArrayOf("INT", choiceId);
            preparedStatement = connection.prepareStatement("INSERT INTO poll (author ,favestar, hashtag, attachment , postingtime, context , choiceid) VALUES (?,?,?,?,?,?,?) returning id");
            preparedStatement.setInt(1, authorId);
            preparedStatement.setBoolean(2, false);
            preparedStatement.setArray(3, hashtags);
            preparedStatement.setArray(4, attachments);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(postingTime));
            preparedStatement.setString(6, context);
            preparedStatement.setArray(7,choices);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
