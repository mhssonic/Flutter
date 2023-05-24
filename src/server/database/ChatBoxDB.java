package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatBoxDB extends SQLDB{

    public static void appendMessage(int chatBox , String messageId){
        SQLDB.appendToArrayField("chat_box" , chatBox , "message_id", messageId);
    }

}
