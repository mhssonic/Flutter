package server.message;

import org.bson.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DirectMessage extends Message{
    int reply;

    public static Document messageToDoc(int id , int user , String context, int reply, LocalDateTime dateTime, ArrayList<Integer> attachmentId){
        Document message = new Document();
        message.put("_id", id);
        message.put("author", user);
        message.put("context", context);
        message.put("attachment", attachmentId);
        message.put("time", dateTime);
        message.put("reply", reply);
        return message;
    }

    public DirectMessage(int messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<String> attachmentId, int reply) {
        super(messageId, authorId, text, postingTime, attachmentId);
        this.reply = reply;
    }
}
