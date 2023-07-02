package server.message.tweet;

import server.database.AttachmentDB;
import server.database.QuoteDB;
import server.database.TweetDB;
import server.enums.error.ErrorType;
import server.message.Attachment;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Quote extends Tweet{
    private int quotedMessageID;

    public Quote(int messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<Integer> attachmentId , int likes, Integer[] comment, String[] hashtag, int retweetCount , int quotedMessageID) {
        super(messageId, authorId, text, postingTime, attachmentId , likes,  comment,  hashtag,  retweetCount);
        this.quotedMessageID = quotedMessageID;
    }
    public Quote(){}

    public static ErrorType quote(int userId, String context, ArrayList<Integer> attachments, Object[] hashtag , int quotedMessageID){
      
        if(attachments != null && !AttachmentDB.checkAttachments(attachments))
            return ErrorType.DOESNT_EXIST;
        if (validTweet(context) == ErrorType.SUCCESS){
            int quoteId;
            if(attachments == null)
                quoteId = QuoteDB.createQuote(userId, context, new Integer[0], hashtag, LocalDateTime.now(),quotedMessageID);
            else
                quoteId = QuoteDB.createQuote(userId, context, attachments.toArray(new Integer[attachments.size()]), hashtag, LocalDateTime.now(),quotedMessageID);
            return shareTweetWithFollowers(userId,quoteId);
        }
        else return validTweet(context);
    }


}

