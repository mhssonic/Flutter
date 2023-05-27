package server.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class RetweetDB extends TweetDB {
    public static int createRetweet(int retweetedMessageId, int retweeterId) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO retweet (retweeted_message_id, author) VALUES (?,?) returning id");
            preparedStatement.setInt(1,retweetedMessageId );
            preparedStatement.setInt(2,retweeterId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
