package server.message;

import server.enums.error.ErrorHandling;
import server.enums.error.ErrorType;

import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class Message {
    protected int messageId;
    protected int authorId;
    protected String text;
    protected LocalDateTime postingTime;
    protected final static int MAX_LENGTH_MESSAGE = 160;

    ArrayList<String> attachmentId;

    public Message(int messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<String> attachmentId) {
        this.messageId = messageId;
        this.authorId = authorId;
        this.text = text;
        this.postingTime = postingTime;
        this.attachmentId = attachmentId;
    }

    public static ErrorType validMessage(String context){
        ErrorType errorType = ErrorHandling.validLength(context, MAX_LENGTH_MESSAGE);
        if (errorType != ErrorType.SUCCESS) {
            return errorType;
        }
        return ErrorType.SUCCESS;
    }
}
