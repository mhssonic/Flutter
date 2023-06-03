package server.message.Direct;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.Document;
import server.Tools;
import server.database.*;
import server.enums.error.ErrorType;
import server.message.Attachment;
import server.message.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DirectMessage extends Message {
    int reply;
    @JsonProperty("target-id")
    int targetUser;

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

    public void setReply(int reply) {
        this.reply = reply;
    }

    public void setTargetUser(int targetUser) {
        this.targetUser = targetUser;
    }

    public DirectMessage(int messageId, int authorId, String text, LocalDateTime postingTime, ArrayList<Integer> attachmentId, int reply) {
        super(messageId, authorId, text, postingTime, attachmentId);
        this.reply = reply;
    }

    public DirectMessage(){}

    public static ErrorType sendDirectMessage(int user, int targetUser, String context, int reply, ArrayList<Integer> attachments){
        ErrorType errorType = validMessage(context);
        if(errorType != ErrorType.SUCCESS)
            return errorType;
        if(UserDB.isBlocked(user, targetUser))
            return ErrorType.BLOCKED;
        if(!AttachmentDB.checkAttachments(attachments))
            return ErrorType.DOESNT_EXIST;

        int messageId = SQLDB.getDirectMessageId();
        DirectMessageDB.createDirectMessage(messageId, user, context, reply, attachments);

        int chatBoxId = Tools.jenkinsHash(user, targetUser, true);
        if(!ChatBoxDB.containChatBox(chatBoxId))//TODO handle it better
            ChatBoxDB.creatChatBox(chatBoxId);
        ChatBoxDB.appendMessage(chatBoxId, messageId);
        return ErrorType.SUCCESS;
    }

    public int getReply() {
        return reply;
    }

    public int getTargetUser() {
        return targetUser;
    }
}
