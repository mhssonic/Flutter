package server.database;

import java.sql.SQLException;

public class TweetDB extends  SQLDB{
    public static void creatTable(){
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS tweet (\n" +
                    "\tid VARCHAR(16) PRIMARY KEY,\n" +
                    "\tauthor VARCHAR(16),\n" +
                    "\tcontext VARCHAR(280),\n" +
                    "\tattachmenst VARCHAR(16) array[8],\n" +
                    "\tretweet smallserial,\n" +
                    "\tlikes VARCHAR(16) array[1024],\n" +
                    "\tfavestar bool,\n" +
                    "\tcomments VARCHAR(16) Array[1024],\n" +
                    "\thashtag VARCHAR(16) array[16]\n" +
                    "\tpostingTime timestamp,\n" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}