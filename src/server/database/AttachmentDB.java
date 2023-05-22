package server.database;

import java.sql.SQLException;

public class AttachmentDB extends SQLDB{
    public static void creatTable(){
        try {
            statement.executeQuery("create type IF NOT EXISTS type_file as enum('image', 'video')");
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
