package server.database;

import server.message.tweet.Tweet;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static server.database.SQLDB.connection;

public class PollDB extends TweetDB{
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

    public static Tweet getTweet(Object messageId) {

        try {
            ResultSet resultSet = getResultSet("tweet" , messageId);
            if (!resultSet.next()) return null;

            int author = resultSet.getInt("author");
            String context = resultSet.getString("context");
            Object[] attachmentId = (Object[]) (resultSet.getArray("attachment").getArray());
//            Attachment[] attachments = AttachmentDB.getAttachment(Object[]attachmentId);
            int retweet = resultSet.getInt("retweet");
            int likes = sizeOfArrayField("tweet", messageId, "likes");
            Object[] commentId = (Object[]) (resultSet.getArray("comments").getArray());
//            Comment[] comments = CommentDB.getComments(Object[]commentId);
            Object[] hashtag = (Object[]) (resultSet.getArray("comments").getArray());
            LocalDateTime postingTime = resultSet.getTimestamp("postingTime").toLocalDateTime();
            Tweet tweet = new Tweet(messageId , author , context , postingTime , attachmentId ,  likes );
            return tweet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
