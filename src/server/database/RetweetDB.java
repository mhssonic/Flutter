package server.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class RetweetDB extends SQLDB {
    public static int createRetweet(int retweetedMessageId, int retweeterId) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO retweet (retweeted_message_id, author) VALUES (?,?) returning id");
            preparedStatement.setInt(retweetedMessageId, retweeterId);//TODO could we?
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
