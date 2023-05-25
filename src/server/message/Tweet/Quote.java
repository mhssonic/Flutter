package server.message.Tweet;

import server.database.AttachmentDB;
import server.database.QuoteDB;
import server.database.TweetDB;
import server.enums.error.ErrorType;
import server.message.Attachment;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Quote extends Tweet{
    private int quotedMessageID;

    public static ErrorType Quote(int userId, String context, ArrayList<Attachment> attachments, Integer[] hashtag , int quotedMessageID){
        Integer[] attachmentId = AttachmentDB.creatAttachments(attachments);
        int quoteId = QuoteDB.createQuote(userId, context, attachmentId, hashtag, LocalDateTime.now(),quotedMessageID);
        if (validTweet(context) == ErrorType.SUCCESS){
            return shareTweetWithFollowers(userId,quoteId);
        }
        else return validTweet(context);
    }


}

