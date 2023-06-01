package server.database;

import server.user.Profile;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

public class ProfileDB extends SQLDB {

    public static void createProfile(int userId, String firstName, String lastName, String email, String phoneNumber, String country, LocalDate birthdate, String biography , String avatarPath , String headerPath) {
        try {
            LocalDateTime lastEdit = LocalDateTime.now();

            preparedStatement = connection.prepareStatement("INSERT INTO profile (first_name, last_name, email, phone_number, country, birthdate, last_edit, bio, avatar, header, id) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, country);
            preparedStatement.setDate(6, Date.valueOf(birthdate));//TODO
            preparedStatement.setTimestamp(7, Timestamp.valueOf(lastEdit));
            preparedStatement.setString(8, biography);
            preparedStatement.setString(9, avatarPath);
            preparedStatement.setString(10, headerPath);
            preparedStatement.setInt(11, userId);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //TODO PROFILE ID?
    public static void updateProfile(HashMap<String,Object> profileUpdate , int profileId){
        SQLDB.updateFieldsKeys("profile" , profileId , profileUpdate);
    }


    public static Profile getProfile(int targetId){
        try{
            ResultSet resultSet = getResultSet("profile" , targetId);
            if (!resultSet.next()) return null;

            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String phoneNumber = resultSet.getString("phone_number");
            String country = resultSet.getString("country");
            String birthdate = resultSet.getDate("birthdate").toString();//TODO
            String bio = resultSet.getString("bio");
            String avatar = resultSet.getString("avatar");
            String header = resultSet.getString("header");



            Profile profile = new Profile(firstName , lastName , email , phoneNumber , country , birthdate , bio , avatar , header);
            return profile;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
