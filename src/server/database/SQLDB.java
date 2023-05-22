package server.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLDB {
    private static Connection connection;
    protected static Statement statement;

    public static void main(String[] args) {
        creatTables();
    }

    public static void creatTables(){
        try {
            creatConnection();
            TweetDB.creatTable();
            UserDB.creatTable();
            AttachmentDB.creatTable();
            ChatBoxDB.creatTable();
            ProfileDB.creatTable();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void creatConnection(){
        String url = "jdbc:postgresql://localhost:5432/flutter";
        String user = "postgres";
        String password = "pashmak";

        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
