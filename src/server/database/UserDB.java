package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDB extends SQLDB{
    public static void main(String[] args) {
        run();
        System.out.println(containFieldKey("users", "username", "sdf"));
    }
    public static void creatTable(){
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (\n" +
                    "\tid VARCHAR(16) PRIMARY KEY,\n" +
                    "\tprofile_id VARCHAR(16),\n" +
                    "\tusername VARCHAR(32),\n" +
                    "\tpassword VARCHAR(32),\n" +
                    "\tfollowing VARCHAR(16) array[1024],\n" +
                    "\tfollower VARCHAR(16) array[1024],\n" +
                    "\tblocked VARCHAR(16) array[1024]\n" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean matchUserPass(String username, String password){
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? and password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void CreateUser(String username , String password , String profileId){
        try{
            String id = new String(); //TODO
            preparedStatement = connection.prepareStatement("INSERT INTO users (id,profile_id , username , password) VALUES (?,?,?,?)");
            preparedStatement.setString(1,id);
            preparedStatement.setString(2,profileId);
            preparedStatement.setString(3,username);
            preparedStatement.setString(4,password);
            ResultSet resultSet = preparedStatement.executeQuery();

        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }
}
