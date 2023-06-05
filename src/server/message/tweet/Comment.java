package server.message.tweet;

import server.database.AttachmentDB;
import server.database.CommentDB;
import server.enums.error.ErrorType;
import server.message.Attachment;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Comment extends Tweet{

    int replyFrom;
    public Comment(Object messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<Integer> attachmentId, int likes, int replyFrom) {
        super(messageId, authorId, text, postingTime, attachmentId, likes);
    }

    public Comment(){}

    public static ErrorType comment(int userId, String context, ArrayList<Integer> attachments, Object[] hashtag , int replyFrom){
        if(!AttachmentDB.checkAttachments(attachments))
            return ErrorType.DOESNT_EXIST;
        int commentId = CommentDB.createComment(userId, context, attachments.toArray(new Integer[attachments.size()]), hashtag, LocalDateTime.now(),replyFrom);
        if (validTweet(context) == ErrorType.SUCCESS){
            return shareTweetWithFollowers(userId,replyFrom);
        }
        else return validTweet(context);
    }

    public int getReplyFrom() {
        return replyFrom;
    }

    public void setReplyFrom(int replyFrom) {
        this.replyFrom = replyFrom;
    }
}
