package server.database;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLDB {
    protected static Connection connection;
    protected static Statement statement;
    protected static PreparedStatement preparedStatement;

    public static void main(String[] args) {
        run();
    }

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
            System.out.println("2");
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
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + field + " = ?");
            preparedStatement.setString(1, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void createUserProfile(String firstName , String lastName , String username , String password, String email , String phoneNumber , String country , LocalDate birthdate){
//        statement.executeUpdate("INSERT INTO users")
    }


}
