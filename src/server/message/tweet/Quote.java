package server.message.tweet;

import server.database.AttachmentDB;
import server.database.QuoteDB;
import server.enums.error.ErrorType;
import server.message.Attachment;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Quote extends Tweet{
    private int quotedMessageID;

    public Quote(int messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<Integer> attachmentId , int likes , int quotedMessageID) {
        super(messageId, authorId, text, postingTime, attachmentId , likes);
        this.quotedMessageID = quotedMessageID;
    }
    public Quote(){}

    public static ErrorType quote(int userId, String context, ArrayList<Integer> attachments, Object[] hashtag , int quotedMessageID){
      
        if(!AttachmentDB.checkAttachments(attachments))
            return ErrorType.DOESNT_EXIST;
        int quoteId = QuoteDB.createQuote(userId, context, attachments.toArray(new Integer[attachments.size()]), hashtag, LocalDateTime.now(),quotedMessageID);
        if (validTweet(context) == ErrorType.SUCCESS){
            return shareTweetWithFollowers(userId,quoteId);
        }
        else return validTweet(context);
    }


}

