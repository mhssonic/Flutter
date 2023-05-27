package server.message.tweet;

import server.database.AttachmentDB;
import server.database.QuoteDB;
import server.enums.error.ErrorType;
import server.message.Attachment;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Quote extends Tweet{
    private int quotedMessageID;

    public Quote(int messageId, int authorId, String text, LocalDateTime postingTime, Object[] attachmentId , int likes) {
        super(messageId, authorId, text, postingTime, attachmentId , likes);
    }

    public static ErrorType quote(int userId, String context, ArrayList<Attachment> attachments, Integer[] hashtag , int quotedMessageID){
      
        Integer[] attachmentId = AttachmentDB.creatAttachments(attachments);
        int quoteId = QuoteDB.createQuote(userId, context, attachmentId, hashtag, LocalDateTime.now(),quotedMessageID);
        if (validTweet(context) == ErrorType.SUCCESS){
            return shareTweetWithFollowers(userId,quoteId);
        }
        else return validTweet(context);
    }


}

