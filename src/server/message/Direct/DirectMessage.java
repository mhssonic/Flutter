package server.message.Direct;

import org.bson.Document;
import server.Tools;
import server.database.ChatBoxDB;
import server.database.DirectMessageDB;
import server.database.SQLDB;
import server.database.UserDB;
import server.enums.error.ErrorType;
import server.message.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DirectMessage extends Message {
    public static void main(String[] args) {
        SQLDB.run();
        DirectMessageDB.run();
        ArrayList<Integer> attachment = new ArrayList<>();
        attachment.add(123);
        sendDirectMessage(-2000000000, -1999999999, "hey whats up", 0, attachment);
    }
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

    public static ErrorType sendDirectMessage(int user, int targetUser, String context, int reply, ArrayList<Integer> attachmentId){
        ErrorType errorType = validMessage(context);
        if(errorType != ErrorType.SUCCESS)
            return errorType;
        if(UserDB.isBlocked(user, targetUser))
            return ErrorType.BLOCKED;

        int messageId = SQLDB.getDirectMessageId();
        DirectMessageDB.createDirectMessage(messageId, user, context, reply, attachmentId);

        int chatBoxId = Tools.jenkinsHash(user, targetUser, true);
        if(!ChatBoxDB.containChatBox(chatBoxId))//TODO handle it better
            ChatBoxDB.creatChatBox(chatBoxId);
        ChatBoxDB.appendMessage(chatBoxId, messageId);
        return ErrorType.SUCCESS;
    }
}
