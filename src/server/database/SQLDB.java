package server.database;

import server.Tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

public class SQLDB {
    protected static Connection connection;
    protected static Statement statement;
    protected static PreparedStatement preparedStatement;

    public static void main(String[] args) {
        run();
        appendToArrayField("users", "I44sUI10jHbXao7F", "follower", "sdfdsf");
//        removeFromArrayField("users", "I44sUI10jHbXao7F", "follower", "sdfdsf");
        System.out.println(getFieldObject("users", "I44sUI10jHbXao7F", "follower"));
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

    //check if table contain an object with filed = key
    protected static boolean containFieldKey(String table, String field,Object key){
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + field + " = ?");
            preparedStatement.setObject(1, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    //check if in table where id = "id" array(field) contain obj
    protected static boolean containInArrayFieldObject(String table, String id, String field, Object obj){
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE id = ? AND ? = ANY(" + field + ")");
            preparedStatement.setString(1, id);
            preparedStatement.setObject(2, obj);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            throw new RuntimeException(e);//TODO handle exception
        }
    }


    //push an obj to an array(field) of a row where id = "id" in table
    protected static void appendToArrayField(String table, String id, String field, Object obj){
        try {
            preparedStatement = connection.prepareStatement("UPDATE " + table + " SET "+ field +" = array_append("+ field + ",?) WHERE id = ?");
            preparedStatement.setObject(1, obj);
            preparedStatement.setString(2, id);

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    protected static void removeFromArrayField(String table, String id, String field, Object obj){
        try {
            preparedStatement = connection.prepareStatement("UPDATE " + table + " SET "+ field +" = array_remove("+ field + ",?) WHERE id = ?");
            preparedStatement.setObject(1, obj);
            preparedStatement.setString(2, id);

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    //update row based on HashMap<field, key>
    protected static void updateFieldsKeys(String table, String id, HashMap<String, Object> fieldKeys){
        try {
            StringBuilder sql =  new StringBuilder();
            sql.append("UPDATE ");
            sql.append(table);
            sql.append(" SET ");
            for(String field : fieldKeys.keySet()){
                sql.append(field);
                sql.append("=");
                sql.append("?,");
            }
            sql.setCharAt(sql.length() - 1, ' ');
            sql.append("where id = ?");

            preparedStatement = connection.prepareStatement(sql.toString());
            int i = 1;
            for(Object obj : fieldKeys.values()){
                preparedStatement.setObject(i, obj);
                i++;
            }
            preparedStatement.setString(i, id);

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    protected static Object getFieldObject(String table, String id, String field){
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE id = ?");
            preparedStatement.setString(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getObject(field);
        }catch (SQLException e){
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    //TODO let them add bio and etc at first
    public static void createUserProfile(String firstName , String lastName , String username , String password, String email , String phoneNumber , String country , LocalDate birthdate , String biography , String avatarPath , String headerPath){
        String profileId = ProfileDB.createProfile(firstName,lastName,email,phoneNumber,country,birthdate,biography,avatarPath,headerPath);
        UserDB.createUser(username,password,profileId);
    }
}
