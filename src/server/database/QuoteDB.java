package server.database;

import server.message.tweet.Quote;
import server.message.tweet.Tweet;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class QuoteDB extends TweetDB {
    public static int createQuote(int authorId, String context, Integer[] attachmentId, Object[] hashtag, LocalDateTime postingTime, int quotedMessageID) {
        try {
            Array hashtags = connection.createArrayOf("INT", hashtag);
            Array attachments = connection.createArrayOf("INT", attachmentId);
            //TODO attachment and id
            preparedStatement = connection.prepareStatement("INSERT INTO quote (author ,favestar, hashtag, attachment , postingtime, context , quoted_message_id) VALUES (?,?,?,?,?,?,?) returning id");
            preparedStatement.setInt(1, authorId);
            preparedStatement.setBoolean(2, false);
            preparedStatement.setArray(3, hashtags);
            preparedStatement.setArray(4, attachments);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(postingTime));
            preparedStatement.setString(6, context);
            preparedStatement.setInt(7, quotedMessageID);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Tweet getTweet(int messageId) {

        try {
            ResultSet resultSet = getResultSet("quote", messageId);
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
            int quotedMessageId = resultSet.getInt("quoted_message_id");

            ArrayList<Integer> attachments  = new ArrayList<>();
            for(Object obj : attachmentId)
                attachments.add((Integer) obj);
            Quote quote = new Quote(messageId, author, context, postingTime, attachments, likes, quotedMessageId);
            return quote;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
