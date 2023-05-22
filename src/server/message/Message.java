package server.message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public abstract class Message {
    private String messageId;
    private String authorId;
    private String text;
    private LocalDateTime postingTime;

    ArrayList<String> attachmentId;

}
