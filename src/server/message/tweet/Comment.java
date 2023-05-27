package server.message.tweet;

import java.time.LocalDateTime;

public class Comment extends Tweet{
    int replyFrom;
    public Comment(Object messageId, int authorId, String text, LocalDateTime postingTime, Object[] attachmentId, int likes, int replyFrom) {
        super(messageId, authorId, text, postingTime, attachmentId, likes);
    }
}
