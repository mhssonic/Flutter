package server.database;

import server.message.Attachment;
import server.message.tweet.Comment;
import server.message.tweet.Tweet;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CommentDB extends TweetDB {
    public static int createComment(int authorId, String context, Integer[] attachmentId, String[] hashtag, LocalDateTime postingTime, int reply) {
        try {
            Array hashtags = connection.createArrayOf("VARCHAR(50)", hashtag);
            Array attachments = connection.createArrayOf("INT", attachmentId);
            //TODO attachment and id
            preparedStatement = connection.prepareStatement("INSERT INTO comment (author ,favestar, hashtag, attachment , postingtime, context, reply) VALUES (?,?,?,?,?,?,?) returning id");
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

    public static ArrayList<Comment> getComment(int[] commentIds) {
        ResultSet resultSet;
        ArrayList<Comment> comments = new ArrayList<>();
        for (int commentId : commentIds) {
            resultSet = getResultSet("comment", commentId);
            try {
                if (resultSet.next()) {
                    int author = resultSet.getInt("author");
                    String context = resultSet.getString("context");
                    Object[] attachmentId = (Object[]) (resultSet.getArray("attachment").getArray());
//                  Attachment[] attachments = AttachmentDB.getAttachment(Object[]attachmentId); //TODO add to constructor?
                    int retweet = resultSet.getInt("retweet");
                    int likes = sizeOfArrayField("comment", commentId, "likes");
                    Object[] commentIdS = (Object[]) (resultSet.getArray("comments").getArray());
//                  Comment[] comments = CommentDB.getComments(Object[]commentId);
                    Object[] hashtag = (Object[]) (resultSet.getArray("comments").getArray());
                    LocalDateTime postingTime = resultSet.getTimestamp("postingTime").toLocalDateTime();
                    int replyFrom = resultSet.getInt("reply");

                    Comment comment = new Comment(commentId, author, context, postingTime, attachmentId, likes, replyFrom);
                    comments.add(comment);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return comments;
    }

    public static Tweet getTweet(int messageId) {
        ResultSet resultSet;

        resultSet = getResultSet("comment", messageId);
        try {
            if (!resultSet.next()) return null;
                int author = resultSet.getInt("author");
                String context = resultSet.getString("context");
                Object[] attachmentId = (Object[]) (resultSet.getArray("attachment").getArray());
//                  Attachment[] attachments = AttachmentDB.getAttachment(Object[]attachmentId); //TODO add to constructor?
                int retweet = resultSet.getInt("retweet");
                int likes = sizeOfArrayField("comment", messageId, "likes");
                Object[] commentIdS = (Object[]) (resultSet.getArray("comments").getArray());
//                  Comment[] comments = CommentDB.getComments(Object[]commentId);
                Object[] hashtag = (Object[]) (resultSet.getArray("comments").getArray());
                LocalDateTime postingTime = resultSet.getTimestamp("postingTime").toLocalDateTime();
                int replyFrom = resultSet.getInt("reply");

                Comment comment = new Comment(messageId, author, context, postingTime, attachmentId, likes, replyFrom);
                return comment;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
