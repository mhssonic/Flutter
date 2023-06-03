package server.database;

import server.enums.TweetType;
import server.message.Message;

import java.sql.*;
import java.util.ArrayList;

public class ChatBoxDB extends SQLDB {
    public static void main(String[] args) {
        run();
//        System.out.println(returnMessage(1713722456));
    }

    public static void creatChatBox(int id) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO chat_box (id) VALUES (?)");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void appendMessage(int chatBox, int messageId) {
        appendToArrayField("chat_box", chatBox, "message_id", messageId);
    }

    public static boolean containChatBox(int id) {
        return containFieldKey("chat_box", "id", id);
    }

    public static Array getMessageId(int chatBoxId) {
        return (Array) SQLDB.getFieldObject("chat_box", chatBoxId, "message_id");
    }
}

