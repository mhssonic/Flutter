package server.database;

import java.sql.*;

public class SQLDB {
    protected static Connection connection;
    protected static Statement statement;

    public static void run(){
        try {
            creatConnection();
            creatTables();
        }catch (Exception e){};
    }

    public static void creatTables(){
        try {
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
    public static boolean containFieldKey(String table, String field, String key){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + field + " = ?");
            preparedStatement.setString(1, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
