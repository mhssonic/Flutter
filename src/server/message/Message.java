package server.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import server.database.*;
import server.enums.TweetType;
import server.enums.error.ErrorHandling;
import server.enums.error.ErrorType;

import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class Message {
    protected Object messageId;
    @JsonProperty("author-id")
    protected int authorId;

    @JsonProperty("text")
    protected String text;

    protected LocalDateTime postingTime;
    protected final static int MAX_LENGTH_MESSAGE = 280;
    @JsonProperty("attachment-id")
    ArrayList<Integer> attachmentId;
    @JsonIgnore
    ArrayList<Attachment> attachments = new ArrayList<>();



    public static void showMessage(int start , int finish , int chatBoxId){

    }

    public Message(Object messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<Integer> attachmentId) {
        this.messageId = messageId;
        this.authorId = authorId;
        this.text = text;
        this.postingTime = postingTime;
        this.attachmentId = attachmentId;
    }
    public Message(){}

    public static ErrorType validMessage(String context){
        ErrorType errorType = ErrorHandling.validLength(context, MAX_LENGTH_MESSAGE);
        if (errorType != ErrorType.SUCCESS) {
            return errorType;
        }
        return ErrorType.SUCCESS;
    }

    public Object getMessageId() {
        return messageId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getPostingTime() {
        return postingTime;
    }

    public ArrayList<Integer> getAttachmentId() {
        return attachmentId;
    }

    public void setMessageId(Object messageId) {
        this.messageId = messageId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public void setText(String text) {
        this.text = text;
    }


    public void setPostingTime(LocalDateTime postingTime) {
        this.postingTime = postingTime;
    }

    public void setAttachmentId(ArrayList<Integer> attachmentId) {
        this.attachmentId = attachmentId;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }

    public static ArrayList<Message> getMessages(Object[] messageIds) {
        try {
            Message message = null;
            ArrayList<Message> messages = new ArrayList<>();
            for (Object messageId : messageIds) {
                message = getMessage((int)messageId);
                if (!(message == null)) {
                    messages.add(message);
                }
            }
            return messages;
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    public static Message getMessage(int messageId) {
        int type = messageId % (TweetType.count);
        switch (type) {
            case 0: return TweetDB.getTweet(messageId);
            case 1: return CommentDB.getTweet(messageId);
            case 2: return RetweetDB.getTweet(messageId);
            case 3: return PollDB.getTweet(messageId);
            case 4: return QuoteDB.getTweet(messageId);
            case 5: return DirectMessageDB.getMessage(messageId);
        }
        return null;
    }
}
