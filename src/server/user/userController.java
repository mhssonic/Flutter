package server.user;

import server.enums.error.ErrorHandling;
import server.enums.error.ErrorType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class userController {
    public static String signIn(String username, String password) {
        if (DataBase.checkPassword(username, password)) {
            //TODO token
            return ErrorType.SUCCESS.toString();
        }
        return null;
    }

    public static ErrorType signUp(String firstName, String lastName, String username, String password, String confirmPassword, String email, String phoneNumber, String country, String birthdate) {
        ErrorType output;
        output = ErrorHandling.validUsername(username);
        if (output != ErrorType.SUCCESS) return output;

        output = ErrorHandling.validPass(password);
        if (output != ErrorType.SUCCESS) return output;

        output = ErrorHandling.validConfirm(password, confirmPassword);
        if (output != ErrorType.SUCCESS) return output;

        output = ErrorHandling.validEmail(email);
        if (output != ErrorType.SUCCESS) return output;

        output = ErrorHandling.validPhoneNumber(phoneNumber);
        if (output != ErrorType.SUCCESS) return output;

        output = ErrorHandling.validBirthDate(birthdate);
        if (output != ErrorType.SUCCESS) return output;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-d");
        LocalDate date = LocalDate.parse(birthdate, dtf);
        DataBase.creatUser(firstName, lastName, username, password, email, phoneNumber, country, date);
        return ErrorType.SUCCESS;
    }


}
