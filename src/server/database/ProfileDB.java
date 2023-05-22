package server.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public static Boolean containUsername(String username) {
        return containFieldKey("users", "username", username);
    }

    public static Boolean containEmail(String email) {
        return containFieldKey("users", "email", email);
    }

    public static Boolean containPhoneNumber(String phoneNumber) {
        return containFieldKey("users", "phoneNumber", phoneNumber);
    }

    public static void CreateProfile(String firstName, String lastName, String email, String phoneNumber, String country, LocalDate birthdate, String biography , String avatarPath , String headerPath) {
        try {
            String profileId = new String();//TODO generate?
            LocalDateTime lastEdit = LocalDateTime.now();

            preparedStatement = connection.prepareStatement("INSERT INTO users VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, profileId);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, phoneNumber);
            preparedStatement.setString(4, country);
            preparedStatement.setString(4, birthdate.toString());//TODO
            preparedStatement.setString(4, lastEdit.toString());
            preparedStatement.setString(4, biography);
            preparedStatement.setString(4, avatarPath);
            preparedStatement.setString(4, headerPath);
            ResultSet resultSet = preparedStatement.executeQuery();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
