package server.database;

import server.message.tweet.Retweet;
import server.message.tweet.Tweet;

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

    public static Tweet getTweet(int messageId){
        ResultSet resultSet = getResultSet("retweet" , messageId);

        try {
            if (!resultSet.next()) return null;

            int retweetId = resultSet.getInt("id");
            int author = resultSet.getInt("author");
            int retweetedMessageId = resultSet.getInt("retweeted_message_id");

            Tweet tweet = TweetDB.getTweet(retweetedMessageId);
            Retweet retweet = new Retweet(retweetId , tweet.getAuthorId() , tweet.getText() , tweet.getPostingTime() , tweet.getAttachmentId() , tweet.getLikes(), tweet.getComment(), tweet.getHashtag(), tweet.getRetweetCount() , retweetedMessageId , author );
            return  retweet;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
