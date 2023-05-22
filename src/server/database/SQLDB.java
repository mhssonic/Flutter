package server.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
            SQLScripRunner("functions");
            SQLScripRunner("types");
            SQLScripRunner("tables");
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
    public static boolean containFieldKey(String table, String field,Object key){
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + field + " = ?");
            preparedStatement.setObject(1, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static void createUserProfile(String firstName , String lastName , String username , String password, String email , String phoneNumber , String country , LocalDate birthdate){
//        statement.executeUpdate("INSERT INTO users")
    }

    public static void SQLScripRunner(String fileName){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/server/sql/" + fileName + ".sql"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            reader.close();
            String sql = sb.toString();
            statement.executeUpdate(sql);
        }catch (SQLException | IOException e){
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        }
    }


}
