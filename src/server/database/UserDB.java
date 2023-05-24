package server.database;

import server.enums.error.ErrorType;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

public class UserDB extends SQLDB {
    public static void main(String[] args) {
        run();
//        createUser("mhs1", "pash1maki", "211312");
        createUserProfile("asdf", "asdf", "mhs2", "sdfsdf", "sdafasdf", "asdfasdf", "dsa", LocalDate.now(), "sdf", "asdf", "asdf");
    }

    public static boolean matchUserPass(String username, String password) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? and password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void createUser(String username, String password, String profileId) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO users (profile_id , username , password) VALUES (?,?,?)");
            preparedStatement.setString(1, profileId);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
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

    public static void updateUser(HashMap<String, Object> userUpdate, String userId) {
        SQLDB.updateFieldsKeys("users", userId, userUpdate);
    }

    //TODO Int id , userId?;
    public static ErrorType follow(String userId, String targetId) {
        if (SQLDB.containInArrayFieldObject("users", userId, "following", targetId)) return ErrorType.ALREADY_EXIST;
        if (SQLDB.containInArrayFieldObject("users", targetId, "blocked-user", userId)) return ErrorType.BLOCKED;
        //TODO check for reputation?

        SQLDB.appendToArrayField("users", userId, "following", targetId);
        SQLDB.appendToArrayField("users", targetId, "follower", userId);
        return ErrorType.SUCCESS;
    }

    public static void unFollow(String userId, String targetId) {
        SQLDB.removeFromArrayField("users", userId, "following", targetId);
        SQLDB.removeFromArrayField("users", targetId, "follower", userId);
    }


    public static Array getFollower(String userId) {
        return (Array) SQLDB.getFieldObject("users", userId, "follower");
    }
    public static Array getFollowing(String userId) {
        return (Array) SQLDB.getFieldObject("users", userId, "following");
    }

    public static ErrorType block(String userId, String targetId) {
        if (SQLDB.containInArrayFieldObject("users", targetId, "blocked-user", userId)) return ErrorType.ALREADY_EXIST;
        if (SQLDB.containInArrayFieldObject("users" , userId , "follower" , targetId)){
            SQLDB.removeFromArrayField("users", userId, "following", targetId);
            SQLDB.removeFromArrayField("users", targetId, "follower", userId);
        }
        SQLDB.appendToArrayField("users", userId, "blocked", targetId);
        return ErrorType.SUCCESS;
    }

    public static void unBlock (String userId, String targetId) {
        SQLDB.removeFromArrayField("users", userId, "blocked", targetId);
    }


}
