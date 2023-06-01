package server.message.tweet.poll;

import server.database.AttachmentDB;
import server.database.ChoiceDB;
import server.database.PollDB;
import server.enums.error.ErrorType;
import server.message.Attachment;
import server.message.tweet.Tweet;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Poll extends Tweet {
    private Object[] choiceId;

    public Poll(int messageId, int authorId, String text, LocalDateTime postingTime, Object[] attachmentId, int likes ,Object[] choiceId ) {
        super(messageId, authorId, text, postingTime, attachmentId, likes);
        this.choiceId = choiceId;
    }

    public static ErrorType poll(int userId, String context, ArrayList<Attachment> attachments, Integer[] hashtag , Object[] choices){
        Integer[] choiceId = (Integer[]) choices;
        Integer[] attachmentId = AttachmentDB.creatAttachments(attachments);

        int pollId = PollDB.createPoll(userId, context, attachmentId, hashtag, LocalDateTime.now() , choiceId);
        if (validTweet(context) == ErrorType.SUCCESS){
            return shareTweetWithFollowers(userId,pollId);
        }
        else return validTweet(context);

    }

    public static void Vote(int userId , int choiceId ){
        ChoiceDB.addVoters(userId ,choiceId);
    }

}
