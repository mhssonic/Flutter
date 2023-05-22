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
            statement.executeQuery("CREATE TABLE IF NOT EXISTS users (\n" +
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? and password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
