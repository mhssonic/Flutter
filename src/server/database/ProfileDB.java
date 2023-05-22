package server.database;

import java.sql.SQLException;

public class ProfileDB extends SQLDB{
    public static void creatTable(){
        try {
            statement.executeQuery("CREATE TABLE IF NOT EXISTS profile (\n" +
                    "\tid VARCHAR(16) PRIMARY KEY,\n" +
                    "\tfirst_name VARCHAR(50),\n" +
                    "\tlast_name VARCHAR(50),\n" +
                    "\temail VARCHAR(64),\n" +
                    "\tphone_number VARCHAR(13),\n" +
                    "\tcountry VARCHAR(3),\n" +
                    "\tbirthdate date,\n" +
                    "\tlast_edit timestamp,\n" +
                    "\tbio VARCHAR(160),\n" +
                    "\tavatar VARCHAR(128),\n" +
                    "\theader VARCHAR(128)\n" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
