package server.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    Object[] attachmentId;
    ArrayList<Attachment> attachments = new ArrayList<>();



    public static void showMessage(int start , int finish , int chatBoxId){

    }

    public Message(Object messageId, int authorId, String text, LocalDateTime postingTime, Object[] attachmentId) {
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

    public Object[] getAttachmentId() {
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

    public void setAttachmentId(Object[] attachmentId) {
        this.attachmentId = attachmentId;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }
}
