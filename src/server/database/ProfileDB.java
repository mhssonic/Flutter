package server.database;

import server.enums.error.ErrorType;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

public class ProfileDB extends SQLDB {
    public static void creatTable() {
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS profile (\n" +
                    "\tid VARCHAR(16) PRIMARY KEY,\n" +
                    "\tfirst_name VARCHAR(50),\n" +
                    "\tlast_name VARCHAR(50),\n" +
                    "\temail VARCHAR(64),\n" +
                    "\tphone_number VARCHAR(13),\n" +
                    "\tcountry VARCHAR(3),\n" +
                    "\tbirthdate date,\n" +
                    "\tlast_edit timestamp,\n" +
                    "\tbio VARCHAR(160),\n" +
                    "\tavatar VARCHAR(128),\n" +
                    "\theader VARCHAR(128)\n" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createProfile(String firstName, String lastName, String email, String phoneNumber, String country, LocalDate birthdate, String biography , String avatarPath , String headerPath) {
        try {
            LocalDateTime lastEdit = LocalDateTime.now();

            preparedStatement = connection.prepareStatement("INSERT INTO profile (first_name, last_name, email, phone_number, country, birthdate, last_edit, bio, avatar, header) VALUES (?,?,?,?,?,?,?,?,?,?) returning id");
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
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String profileId = resultSet.getString("id");
            return profileId;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //TODO PROFILE ID?
    public static void updateProfile(HashMap<String,Object> profileUpdate , String profileId){
        SQLDB.updateFieldsKeys("profile" , profileId , profileUpdate);
    }

}
