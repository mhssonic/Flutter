package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatBoxDB extends SQLDB{
    public static void creatTable(){
        try {
            statement.executeQuery("CREATE TABLE IF NOT EXISTS chat_box (\n" +
                    "\tid VARCHAR(16) PRIMARY KEY,\n" +
                    "\tmessage_id VARCHAR(16) array[4096]\n" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
