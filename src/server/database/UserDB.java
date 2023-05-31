package server.database;

import server.enums.error.ErrorType;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserDB extends SQLDB {
    public static void main(String[] args) {
        run();
//        createUser("mhs1", "pash1maki", "211312");
        follow(-2000000000, -1999999990);
//        createUserProfile("asdf", "asdf", "mhs2", "sdfsdf", "sdafasdf", "asdfasdf", "dsa", LocalDate.now(), "sdf", "asdf", "asdf");
    }

    public static String matchUserPass(String username, String password) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? and password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(resultSet.getInt("id"));
                return stringBuffer.toString();
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static int createUser(String username , String password){
        try{
            preparedStatement = connection.prepareStatement("INSERT INTO users (username , password) VALUES (?,?) returning id");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
        }catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
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

    public static void updateUser(HashMap<String, Object> userUpdate, int userId) {
        SQLDB.updateFieldsKeys("users", userId, userUpdate);
    }

    //TODO Int id , userId?;
    public static ErrorType follow(int userId, int targetId) {
        if (SQLDB.containInArrayFieldObject("users", userId, "following", targetId)) return ErrorType.ALREADY_EXIST;
        if (SQLDB.containInArrayFieldObject("users", targetId, "blocked", userId)) return ErrorType.BLOCKED;
        //TODO check for reputation?


        SQLDB.appendToArrayField("users", userId, "following", targetId);
        SQLDB.appendToArrayField("users", targetId, "follower", userId);
        return ErrorType.SUCCESS;
    }

    public static void unFollow(int userId, int targetId) {
        SQLDB.removeFromArrayField("users", userId, "following", targetId);
        SQLDB.removeFromArrayField("users", targetId, "follower", userId);

    }


    public static Array getFollower(int userId) {
        return (Array) SQLDB.getFieldObject("users", userId, "follower");
    }
    public static Array getFollowing(int userId) {
        return (Array) SQLDB.getFieldObject("users", userId, "following");
    }

    public static ErrorType block(int userId, int targetId) {
        if (isBlocked(targetId, userId)) return ErrorType.ALREADY_EXIST;
        if (SQLDB.containInArrayFieldObject("users" , userId , "following" , targetId)){
            SQLDB.removeFromArrayField("users", userId, "following", targetId);
            SQLDB.removeFromArrayField("users", targetId, "follower", userId);
        }
        SQLDB.appendToArrayField("users", userId, "blocked", targetId);
        return ErrorType.SUCCESS;
    }

    public static void unBlock (int userId, int targetId) {
        SQLDB.removeFromArrayField("users", userId, "blocked", targetId);
    }

    public static boolean isBlocked(int userId, int targetId){
        return containInArrayFieldObject("users", userId, "blocked", targetId);
    }

    public static ArrayList<Object> getUsersId(){
        return getAllKeyOfField("users", "id");
    }
}
