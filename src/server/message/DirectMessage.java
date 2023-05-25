package server.message;

import org.bson.Document;
import server.Tools;
import server.database.ChatBoxDB;
import server.database.DirectMessageDB;
import server.database.SQLDB;
import server.enums.error.ErrorType;

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

    public ErrorType sendDirectMessage(int user, int targetUser, String context, int reply, ArrayList<Integer> attachmentId){
        ErrorType errorType = validMessage(context);
        if(errorType != ErrorType.SUCCESS)
            return errorType;
        int id = SQLDB.getDirectMessageId();
        DirectMessageDB.createDirectMessage(id, user, context, reply, attachmentId);

        int chatBoxId = Tools.jenkinsHash(user, targetUser, true);
        if(!ChatBoxDB.containChatBox(chatBoxId))//TODO handle it better
            ChatBoxDB.creatChatBox(chatBoxId);
        ChatBoxDB.appendMessage(chatBoxId, messageId);
        return ErrorType.SUCCESS;
    }
}
