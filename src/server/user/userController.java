package server.user;

import server.database.SQLDB;
import server.database.UserDB;
import server.enums.error.ErrorHandling;
import server.enums.error.ErrorType;

import java.security.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class userController {
    public static String signIn(String username, String password) {
        if (UserDB.matchUserPass(username, password) == null) {
            return null;
        }
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair pair = generator.generateKeyPair();

            PublicKey publicKey = pair.getPublic();
            PrivateKey privateKey = pair.getPrivate();

        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        return null;
    }

//    fos.write(publicKey.getEncoded());
//    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//    EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
//    keyFactory.generatePublic(publicKeySpec);


    public static ErrorType signUp(String firstName, String lastName, String username, String password, String confirmPassword, String email, String phoneNumber, String country, String birthdate, String biography, String avatarPath, String headerPath) {
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

        SQLDB.createUserProfile(firstName, lastName, username, password, email, phoneNumber, country, date, biography, avatarPath, headerPath);

        return ErrorType.SUCCESS;
    }
}
