package server.database;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ChatBoxDB extends SQLDB{
    public static void creatChatBox(int id) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO chat_box (id) VALUES (?)");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void appendMessage(int chatBox , int messageId){
        appendToArrayField("chat_box" , chatBox , "message_id", messageId);
    }

    public static boolean containChatBox(int id){
        return containFieldKey("chat_box", "id", id);
    }
}
