package server.message.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.database.*;
import server.enums.error.ErrorType;
import server.message.Attachment;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Comment extends Tweet{
    int replyFrom;
    public Comment(Object messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<Integer> attachmentId, int likes, Integer[] comment, String[] hashtag, int retweetCount, int replyFrom) {
        super(messageId, authorId, text, postingTime, attachmentId, likes, comment, hashtag, retweetCount);
        this.replyFrom = replyFrom;
    }

    public Comment(){}

    public static ErrorType comment(int userId, String context, ArrayList<Integer> attachments, String[] hashtag , int replyFrom){
        if(attachments != null && !AttachmentDB.checkAttachments(attachments))
            return ErrorType.DOESNT_EXIST;
        if (validTweet(context) == ErrorType.SUCCESS){
            int commentId;
            if(attachments == null)
                commentId = CommentDB.createComment(userId, context, new Integer[0], hashtag, LocalDateTime.now(),replyFrom);
            else
                commentId = CommentDB.createComment(userId, context, attachments.toArray(new Integer[attachments.size()]), hashtag, LocalDateTime.now(),replyFrom);
            TweetDB.addToComment(replyFrom, commentId);
            return shareTweetWithFollowers(userId,commentId);
        }
        else return validTweet(context);
    }

    @JsonProperty("reply-from")
    public int getReplyFrom() {
        return replyFrom;
    }
    @JsonProperty("reply-from")
    public void setReplyFrom(int replyFrom) {
        this.replyFrom = replyFrom;
    }
}
