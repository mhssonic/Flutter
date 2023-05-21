package server.message;

import java.util.ArrayList;

public abstract class Message {
    private String messageId;
    private String authorId;
    private String text;

    ArrayList<String> attachmentId;

}
