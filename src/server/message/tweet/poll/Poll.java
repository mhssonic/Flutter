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
    private  Object[] choices;

    public Poll(int messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<Integer> attachmentId, int likes ,Integer[] comment, String[] hashtag, int retweetCount ,Object[] choiceId ) {
        super(messageId, authorId, text, postingTime, attachmentId, likes, comment, hashtag, retweetCount);
        this.choiceId = choiceId;
    }

    public Poll(){}
    public static ErrorType poll(int userId, String context, ArrayList<Integer> attachments, Object[] hashtag , Object[] choices){
        Integer[] choiceId = (Integer[]) choices;
        if(!AttachmentDB.checkAttachments(attachments))
            return ErrorType.DOESNT_EXIST;
        if (validTweet(context) == ErrorType.SUCCESS){
            int pollId = PollDB.createPoll(userId, context, attachments.toArray(new Integer[attachments.size()]), hashtag, LocalDateTime.now() , choiceId);
            return shareTweetWithFollowers(userId,pollId);
        }
        else return validTweet(context);

    }

    public static void Vote(int userId , int choiceId ){
        ChoiceDB.addVoters(userId ,choiceId);
    }

    public Object[] getChoiceId() {
        return choiceId;
    }

    public Object[] getChoices() {
        return choices;
    }

    public void setChoices(Object[] choices) {
        this.choices = choices;
    }

    public void setChoiceId(Object[] choiceId) {
        this.choiceId = choiceId;
    }
}
