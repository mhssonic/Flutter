package server.message.Tweet.poll;

import server.database.AttachmentDB;
import server.database.ChoiceDB;
import server.database.PollDB;
import server.enums.error.ErrorType;
import server.message.Attachment;
import server.message.Tweet.Tweet;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Poll extends Tweet {
    private ArrayList<String> choiceId;

    public static ErrorType poll(int userId, String context, ArrayList<Attachment> attachments, Integer[] hashtag , ArrayList<Choice> choices){
        Integer[] choiceId = ChoiceDB.creatChoices(choices);
        Integer[] attachmentId = AttachmentDB.creatAttachments(attachments);

        int pollId = PollDB.createPoll(userId, context, attachmentId, hashtag, LocalDateTime.now() , choiceId);
        if (validTweet(context) == ErrorType.SUCCESS){
            return shareTweetWithFollowers(userId,pollId);
        }
        else return validTweet(context);

    }

}
