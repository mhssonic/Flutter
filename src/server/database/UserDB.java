package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

public class UserDB extends SQLDB{
    public static void main(String[] args) {
        run();
//        createUser("mhs1", "pash1maki", "211312");
        createUserProfile("asdf", "asdf", "mhs2", "sdfsdf", "sdafasdf", "asdfasdf", "dsa", LocalDate.now(), "sdf", "asdf", "asdf");
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

    public static String createUser(String username , String password){
        try{
            preparedStatement = connection.prepareStatement("INSERT INTO users (username , password) VALUES (?,?) returning id");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("id");
        }catch (SQLException e){
            System.out.println(e.getMessage());
            throw  new RuntimeException(e);
        }
    }

    public static Boolean containUsername(String username) {
        return containFieldKey("users", "username", username);
    }

    public static Boolean containEmail(String email) {
        return containFieldKey("users", "email", email);
    }

    public static Boolean containPhoneNumber(String phoneNumber) {
        return containFieldKey("users", "phone-number", phoneNumber);
    }

    public static void updateUser(HashMap<String,Object> userUpdate , String userId){
        SQLDB.updateFieldsKeys("users" , userId , userUpdate);
    }
}
