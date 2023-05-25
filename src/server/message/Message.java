package server.message;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public abstract class Message {
    private int messageId;
    private int authorId;
    private String text;
    private LocalDateTime postingTime;

    ArrayList<String> attachmentId;

    public Message(int messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<String> attachmentId) {
        this.messageId = messageId;
        this.authorId = authorId;
        this.text = text;
        this.postingTime = postingTime;
        this.attachmentId = attachmentId;
    }
}
