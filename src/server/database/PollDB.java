package server.database;

import server.message.tweet.Tweet;
import server.message.tweet.poll.Poll;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class PollDB extends TweetDB{
    public static int createPoll(int authorId, String context, Integer[] attachmentId, Object[] hashtag, LocalDateTime postingTime , Integer[] choiceId) {
        try {
            Array hashtags = connection.createArrayOf("VARCHAR", hashtag);
            Array attachments = connection.createArrayOf("INT", attachmentId);
            Array choices = connection.createArrayOf("INT", choiceId);
            preparedStatement = connection.prepareStatement("INSERT INTO poll (author ,favestar, hashtag, attachment , postingtime, context , choice) VALUES (?,?,?,?,?,?,?) returning id");
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

    public static Tweet getTweet(int messageId) {

        try {
            ResultSet resultSet = getResultSet("poll" , messageId);
            if (!resultSet.next()) return null;

            int author = resultSet.getInt("author");
            String context = resultSet.getString("context");


            Object[] attachmentId = null;
            Array attachments =  (resultSet.getArray("attachment"));
            if (attachments != null){
                attachmentId = (Object[]) attachments.getArray();
            }

            int retweet = resultSet.getInt("retweet");
            int likes = sizeOfArrayField("poll", messageId, "likes");

            Object[] commentId = {};
            Array comments =  (resultSet.getArray("comments"));
            if (comments != null){
                commentId = (Object[]) comments.getArray();
            }

            Object[] hashtag = {};
            Array hashtags =  (resultSet.getArray("hashtag"));
            if (hashtags != null){
                hashtag = (Object[]) hashtags.getArray();
            }

            LocalDateTime postingTime = resultSet.getTimestamp("postingTime").toLocalDateTime();
            Object[] choiceId = (Object[]) (resultSet.getArray("choiceId")).getArray();
//            Choice[] choice = ChoiceDB.getChoice(Object[]choiceId);

            ArrayList<Integer> attachment  = new ArrayList<>();
            for(Object obj : attachmentId)
                attachment.add((Integer) obj);

            String[] strHashtag = Arrays.copyOf(hashtag, hashtag.length, String[].class);
            Integer[] intComment = Arrays.copyOf(commentId, commentId.length, Integer[].class);


            Poll poll = new Poll(messageId , author , context , postingTime , attachment ,  likes,intComment, strHashtag,  retweet, choiceId);
            return poll;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
