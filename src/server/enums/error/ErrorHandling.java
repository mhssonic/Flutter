package server.enums.error;

import server.database.AttachmentDB;
import server.database.SQLDB;
import server.message.Attachment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ErrorHandling {
    public static ErrorType validUsername(String username) {
        if (SQLDB.containFieldKey("users", "username" , username)) return ErrorType.DUPLICATED_USERNAME;
        return ErrorType.SUCCESS;
    }

    public static ErrorType validPass(String password) {
        if (password.length() < 8) return ErrorType.INVALID_PASS;
        if (password.equals(password.toUpperCase()) || password.equals(password.toLowerCase()))
            return ErrorType.INVALID_PASS;
        return ErrorType.SUCCESS;
    }

    public static ErrorType validConfirm(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) return ErrorType.MISMATCH;
        return ErrorType.SUCCESS;
    }

    public static ErrorType validEmail(String email) {
        if (SQLDB.containFieldKey("profile","email" , email)) return ErrorType.DUPLICATED_EMAIL;
        String regex = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) return ErrorType.INVALID_EMAIL;
        return ErrorType.SUCCESS;
    }

    public static ErrorType validPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 13) return ErrorType.INVALID_PHONE_NUMBER;
        if (SQLDB.containFieldKey("profile", "phone_number" , phoneNumber)) return ErrorType.DUPLICATED_PHONE_NUMBER;
        return ErrorType.SUCCESS;
    }

    public static ErrorType validBirthDate(String birthDate) {
        //TODO slow
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(birthDate, dtf);
            date = date.plusYears(18);
            LocalDate certainTime = LocalDate.now();

            if (certainTime.isBefore(date)) return ErrorType.UNDER_AGE;
            return ErrorType.SUCCESS;
        } catch (Exception e) {
            return ErrorType.INVALID_BIRTHDATE;
        }
    }

    public static ErrorType validLength(String str , int maxLen){
        if (str.length() > maxLen) return ErrorType.TOO_LONG;
        return ErrorType.SUCCESS;
    }

    public static ErrorType validCountry(String str){
        Set<String> countries = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2);
        if(countries.contains(str))
            return ErrorType.SUCCESS;
        return ErrorType.NOT_VALID_COUNTRY;
    }

    public static ErrorType validPicture(Integer id){
        if(id == 0) return ErrorType.SUCCESS;
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(id);
        if (!AttachmentDB.checkAttachments(temp))
            return ErrorType.DOESNT_EXIST;
        return ErrorType.SUCCESS;
    }
}
