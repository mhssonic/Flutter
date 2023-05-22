package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AttachmentDB extends SQLDB{
    public static void creatTable(){
        try {
            try {
                statement.executeQuery("create type type_file as enum('image', 'video')");
            }catch(Exception e){}
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS attachment (\n" +
                    "\tid VARCHAR(16) PRIMARY KEY,\n" +
                    "\ttype type_file,\n" +
                    "\tpath VARCHAR(128)\n" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
