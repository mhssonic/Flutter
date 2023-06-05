package server.database;

import server.Tools;
import server.enums.error.ErrorHandling;
import server.enums.error.ErrorType;
import server.user.Profile;
import server.user.SignUpForm;
import server.user.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLDB {
    protected static Connection connection;
    protected static Statement statement;
    protected static PreparedStatement preparedStatement;

    public static void main(String[] args) {
        run();
//        Integer[] one = new Integer[10];
//        ArrayList<Integer> array = new ArrayList<>();
//        array.add(-2000000000);
//        array.add(-199999998);
//        System.out.println(containIdsInTable("users", array));
//        createUserProfile("Mohammad hadi", "setak", "mhs", "a powerful password", "email", "", "CA", LocalDate.of(2004, 3, 11), "", "", "");
//        createUserProfile("mahya", "be", "coco", "Cotton_candy", "beheshtimahya11@gmail.com", "", "Ir", LocalDate.now(), "", "", "");
//        createUserProfile("random guy", "random family", "random", "r@ndom", "email", "", "CA", LocalDate.of(2004, 9, 11), "", "", "");
//        createUserProfile("random guy2", "random family2", "random2", "r@ndom", "email2", "", "CA", LocalDate.of(2004, 9, 11), "", "", "");
//        UserDB.follow(-1999999999, -2000000000 );
//        ArrayList<Attachment> attachments = new ArrayList<>();
//        attachments.add(new Attachment("123" , FileType.VIDEO));
//        ArrayList<String> choices = new ArrayList<>();
//        choices.add("stupid");
//        choices.add("not stupid");
//        Poll.poll(-2000000000 ,"Am i stupid?" , attachments , one ,choices);
//        Tweet.tweet(-2000000000, "hi,I want to be added to chatBox",attachments , one);
//        Quote.quote(-2000000000, "hi,I like the fact that you want to be added",attachments , one , -1999999990);
//        UserDB.unBlock(-1999999999 ,  -2000000000);
//        UserDB.follow(-1999999999, -2000000000 );
//        ArrayList<Attachment> attachments = new ArrayList<>();
//        attachments.add(new Attachment("123" , FileType.VIDEO));
//        Tweet.tweet(-2000000000, "hi,I want to be added to chatBox",attachments , one);
//        UserDB.block(-1999999999 ,  -2000000000);
//        TweetDB.removeTweet(-1999999980);
//        System.out.println(getDirectMessageId());
//        increaseFieldKeyByOne("tweet", -2000000000, "retweet");
    }

    public static void run() {
        try {
            creatConnection();
            SQLScripRunner("sequences");
            SQLScripRunner("functions");
            SQLScripRunner("types");
            SQLScripRunner("tables");
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    private static void creatConnection() {
        String url = "jdbc:postgresql://localhost:5432/flutter";
        String user = "postgres";
        String password = "pashmak";

        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void SQLScripRunner(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/server/sql/" + fileName + ".sql"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            reader.close();
            String sql = sb.toString();
            statement.executeUpdate(sql);
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        }
    }

    //check if table contain an object with filed = key
    public static boolean containFieldKey(String table, String field, Object key) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + field + " = ?");
            preparedStatement.setObject(1, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    //check if in table where id = "id" array(field) contain obj
    protected static boolean containInArrayFieldObject(String table, Object id, String field, Object obj) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE id = ? AND ? = ANY(" + field + ")");
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, obj);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    protected static boolean containArrayInArrayFieldObject(String table, Object id, String field, ArrayList<Object> arrayObject) {
        try {
            StringBuilder query = new StringBuilder("SELECT * FROM " + table + " WHERE id = ? AND Array[");
            for(int i = 0; i < arrayObject.size(); i++)
                query.append("?,");
            query.setCharAt(query.length() - 1, ']');
            query.append(" <@ ").append(field);

            preparedStatement = connection.prepareStatement(query.toString());
            preparedStatement.setObject(1, id);
            int i = 2;
            for (Object obj : arrayObject){
                preparedStatement.setObject(i, obj);
                i++;
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    protected static boolean containIdsInTable(String table, ArrayList<Integer> ids) {
        if (ids.size() == 0)
            return true;
        StringBuilder arrayString = new StringBuilder("(\'{");
        for(Integer id : ids)
            arrayString.append(id).append(',');
        arrayString.setCharAt(arrayString.length() - 1, '}');
        arrayString.append("\')");
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE id = Any" + arrayString);
            ResultSet resultSet = preparedStatement.executeQuery();

            int size = 0;
            while (resultSet.next())
                size++;
            return size == ids.size();
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    //push an obj to an array(field) of a row where id = "id" in table
    protected static void appendToArrayField(String table, Object id, String field, Object obj) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE " + table + " SET " + field + " = array_append(" + field + ",?) WHERE id = ?");
            preparedStatement.setObject(1, obj);
            preparedStatement.setObject(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    protected static void removeFromArrayField(String table, Object id, String field, Object obj) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE " + table + " SET " + field + " = array_remove(" + field + ",?) WHERE id = ?");
            preparedStatement.setObject(1, obj);
            preparedStatement.setObject(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    protected static int sizeOfArrayField(String table, Object id, String field) {
        try {
            preparedStatement = connection.prepareStatement("SELECT ARRAY_LENGTH(" + field + ", 1) FROM " + table + " WHERE id = ?");
            preparedStatement.setObject(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("array_length");
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    //value row based on HashMap<field, key>
    protected static void updateFieldsKeys(String table, Object id, HashMap<String, Object> fieldKeys) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE ");
            sql.append(table);
            sql.append(" SET ");
            for (String field : fieldKeys.keySet()) {
                sql.append(field);
                sql.append("=");
                sql.append("?,");
            }
            sql.setCharAt(sql.length() - 1, ' ');
            sql.append("where id = ?");

            preparedStatement = connection.prepareStatement(sql.toString());
            int i = 1;
            for (Object obj : fieldKeys.values()) {
                preparedStatement.setObject(i, obj);
                i++;
            }
            preparedStatement.setObject(i, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected static Object getFieldObject(String table, Object id, String field) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE id = ?");
            preparedStatement.setObject(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            return resultSet.getObject(field);
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    protected static ArrayList<Object> getAllKeyOfField(String table, String field) {
        try {
            preparedStatement = connection.prepareStatement("SELECT " + field + " FROM " + table);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Object> arrayResult = new ArrayList<>();
            while (resultSet.next())
                arrayResult.add(resultSet.getObject(field));
            return arrayResult;
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    //TODO let them add bio and etc at first
    public static void createUserProfile(String firstName, String lastName, String username, String password, String email, String phoneNumber, String country, LocalDate birthdate, String biography, int avatar, int header) {
        int userId = UserDB.createUser(username, password);
        ProfileDB.createProfile(userId, firstName, lastName, email, phoneNumber, country, birthdate, biography, avatar, header);
        ChatBoxDB.creatChatBox(Tools.jenkinsHash(userId, userId, false));
    }

    public static ErrorType updateUserProfile(HashMap<String, Object> updatedData, int userId) {
        HashMap<String, Object> userUpdate = new HashMap<>();
        HashMap<String, Object> profileUpdate = new HashMap<>();
        ErrorType output = null;

        for (String key : updatedData.keySet()) {
            String value = (String) updatedData.get(key);
            switch (key) {
                case "first_name":
                case "last_name":
                    output = ErrorHandling.validLength(value, 50);
                    if (output != ErrorType.SUCCESS) return output;
                    profileUpdate.put(key, value);
                    break;
                case "username":
                    output = ErrorHandling.validLength(value, 32);
                    if (output != ErrorType.SUCCESS) return output;
                    output = ErrorHandling.validUsername(value);
                    if (output != ErrorType.SUCCESS) return output;
                    userUpdate.put(key, value);
                    break;
                case "password":
                    output = ErrorHandling.validLength(value, 32);
                    if (output != ErrorType.SUCCESS) return output;
                    output = ErrorHandling.validPass(value);
                    if (output != ErrorType.SUCCESS) return output;
                    userUpdate.put(key, value);
                    break;
                case "email":
                    output = ErrorHandling.validLength(value, 64);
                    if (output != ErrorType.SUCCESS) return output;
                    output = ErrorHandling.validEmail(value);
                    if (output != ErrorType.SUCCESS) return output;
                    profileUpdate.put(key, value);
                    break;
                case "phone_number":
                    output = ErrorHandling.validLength(value, 13);
                    if (output != ErrorType.SUCCESS) return output;
                    output = ErrorHandling.validPhoneNumber(value);
                    if (output != ErrorType.SUCCESS) return output;
                    profileUpdate.put(key, value);
                    break;
                case "birthdate":
                    output = ErrorHandling.validBirthDate(value);
                    if (output != ErrorType.SUCCESS) return output;
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate date = LocalDate.parse(value, dtf);
                    profileUpdate.put(key, value);
                    break;
                case "bio":
                    output = ErrorHandling.validLength(value, 160);
                    if (output != ErrorType.SUCCESS) return output;
                    profileUpdate.put(key, value);
                    break;
                case "country":
                    output = ErrorHandling.validCountry(value);
                    if (output != ErrorType.SUCCESS) return output;
                    profileUpdate.put(key, value);
                    break;
                case "header":
                case "avatar":
                    output = ErrorHandling.validPicture(Integer.parseInt(value));
                    if (output != ErrorType.SUCCESS) return output;
                    profileUpdate.put(key, value);
                    break;
            }
        }
        if (!userUpdate.isEmpty()) UserDB.updateUser(userUpdate, userId);
        if (!profileUpdate.isEmpty()) ProfileDB.updateProfile(profileUpdate, userId);

        return ErrorType.SUCCESS;
    }

    public static int getDirectMessageId() {
        try {
            preparedStatement = connection.prepareStatement("select NEXTVAL('seq_message_id')  ");

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("nextval");
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    public static ResultSet getResultSet(String table, Object messageId) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " where id=?");
            preparedStatement.setObject(1, messageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void increaseFieldKeyByOne(String table, Object id, String field) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE " + table + " SET " + field + " = " + field + "+ 1 WHERE id = ?");
            preparedStatement.setObject(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    protected static void decreaseFieldKeyByOne(String table, Object id, String field) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE " + table + " SET " + field + " = " + field + "- 1 WHERE id = ?");
            preparedStatement.setObject(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO handle exception
        }
    }

    public static SignUpForm getUserProfileByUsername(String targetUsername) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM  users WHERE username = ?");
            preparedStatement.setObject(1, targetUsername);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                return null; //TODO USERNAME OR ID?
            }
            int targetId = resultSet.getInt("id");

            User user = UserDB.getUser(targetId);
            Profile profile = ProfileDB.getProfile(targetId);
            SignUpForm signUpForm = new SignUpForm(user , profile);
            return signUpForm;
    }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static SignUpForm getUserProfileByUserID(int targetID) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM  users WHERE id = ?");
            preparedStatement.setObject(1, targetID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                return null; //TODO USERNAME OR ID?
            }

            User user = UserDB.getUser(targetID);
            Profile profile = ProfileDB.getProfile(targetID);
            SignUpForm signUpForm = new SignUpForm(user , profile);
            return signUpForm;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
