package server.database;

import java.sql.SQLException;

public class AttachmentDB extends SQLDB{
    public static void creatTable(){
        try {
            try {
                statement.executeQuery("create type type_file as enum('image', 'video')");
            }catch(Exception e){}
            statement.executeQuery("CREATE TABLE IF NOT EXISTS attachment (\n" +
                    "\tid VARCHAR(16) PRIMARY KEY,\n" +
                    "\ttype type_file,\n" +
                    "\tpath VARCHAR(128)\n" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
