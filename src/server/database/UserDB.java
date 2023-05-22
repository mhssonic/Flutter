package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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

    public static void createUser(String username , String password , String profileId){
        try{
            preparedStatement = connection.prepareStatement("INSERT INTO users (profile_id , username , password, following, follower, blocked) VALUES (?,?,?, '{}', '{}', '{}')");
            preparedStatement.setString(1,profileId);
            preparedStatement.setString(2,username);
            preparedStatement.setString(3,password);
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());
            throw  new RuntimeException(e);
        }
    }
}
