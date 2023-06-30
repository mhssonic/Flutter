package server.database;

import server.enums.error.ErrorType;
import server.user.User;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
            if (resultSet.next()) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(resultSet.getInt("id"));
                return stringBuffer.toString();
            }
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public static int createUser(String username, String password) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO users (username , password) VALUES (?,?) returning id");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
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

    public static void updateUser(HashMap<String, Object> userUpdate, int userId) {
        SQLDB.updateFieldsKeys("users", userId, userUpdate);
    }

    public static ErrorType follow(int userId, int targetId) {
        if(userId == targetId) return ErrorType.SAME_PERSON;
        if (!SQLDB.containFieldKey("users", "id", targetId)) return ErrorType.DOESNT_EXIST;
        if (SQLDB.containInArrayFieldObject("users", userId, "following", targetId)) return ErrorType.ALREADY_EXIST;
        if (SQLDB.containInArrayFieldObject("users", targetId, "blocked", userId)) return ErrorType.BLOCKED;


        SQLDB.appendToArrayField("users", userId, "following", targetId);
        SQLDB.appendToArrayField("users", targetId, "follower", userId);
        return ErrorType.SUCCESS;
    }

    public static void addToFriend(int userId, int targetId) {
        SQLDB.appendToArrayField("users", userId, "friend", targetId);
        SQLDB.appendToArrayField("users", targetId, "friend", userId);
    }

    public static ErrorType unFollow(int userId, int targetId) {
        if(userId == targetId) return ErrorType.SAME_PERSON;
        if (!SQLDB.containFieldKey("users", "id", targetId)) return ErrorType.DOESNT_EXIST;
        if (!SQLDB.containInArrayFieldObject("users", userId, "following", targetId)) return ErrorType.HAVE_NOT_FOLLOWED;
        SQLDB.removeFromArrayField("users", userId, "following", targetId);
        SQLDB.removeFromArrayField("users", targetId, "follower", userId);

        return ErrorType.SUCCESS;
    }


    public static Array getFollower(int userId) {
        return (Array) getFieldObject("users", userId, "follower");
    }//TODO handle null

    public static Array getFriends(int userId) {
        return (Array) getFieldObject("users", userId, "friend");
    }

    public static HashSet<Integer> getFriendsInSet(int userId) throws SQLException {
        Array arrayFriends = UserDB.getFriends(userId);
        if(arrayFriends == null)
            return new HashSet<>();
        Object[] friends = (Object[]) (arrayFriends.getArray());
        HashSet<Integer> friendsId = new HashSet<>();
        for(Object obj : friends)
            friendsId.add((int) obj);
        return friendsId;
    }

    public static Array getFollowing(int userId) {
        return (Array) SQLDB.getFieldObject("users", userId, "following");
    }

    public static ErrorType block(int userId, int targetId) {
        if(userId == targetId) return ErrorType.SAME_PERSON;
        if (!SQLDB.containFieldKey("users", "id", targetId)) return ErrorType.DOESNT_EXIST;
        if (isBlocked(userId, targetId)) return ErrorType.ALREADY_EXIST;
        if (SQLDB.containInArrayFieldObject("users", userId, "following", targetId)) {
            SQLDB.removeFromArrayField("users", userId, "following", targetId);
            SQLDB.removeFromArrayField("users", targetId, "follower", userId);
        }
        SQLDB.appendToArrayField("users", userId, "blocked", targetId);
        return ErrorType.SUCCESS;
    }

    public static ErrorType unBlock(int userId, int targetId) {
        if(userId == targetId) return ErrorType.SAME_PERSON;
        if (!SQLDB.containFieldKey("users", "id", targetId)) return ErrorType.DOESNT_EXIST;
        if (!SQLDB.containInArrayFieldObject("users", userId, "blocked", targetId)) return ErrorType.HAVE_NOT_BLOCKED;
        SQLDB.removeFromArrayField("users", userId, "blocked", targetId);
        return ErrorType.SUCCESS;
    }

    public static boolean isBlocked(int userId, int targetId) {
        return containInArrayFieldObject("users", userId, "blocked", targetId);
    }

    public static ArrayList<Object> getUsersId() {
        return getAllKeyOfField("users", "id");
    }

    public static User getUser(int targetId) {
        try {
            ResultSet resultSet = getResultSet("users", targetId);
            if (!resultSet.next()) return null;

            String username = resultSet.getString("username");
            Object[] followerId = null;
            Object[] followingId = null;
            Object[] friendId = null;
            HashSet<Integer> follower = new HashSet<>();
            HashSet<Integer> following = new HashSet<>();
            HashSet<Integer> friend = new HashSet<>();

            if (resultSet.getArray("follower" )!= null){
                followerId = (Object[]) (resultSet.getArray("follower").getArray());
                for (Object tmp : followerId) {
                    follower.add((int) tmp);
                }
            }
            if (resultSet.getArray("following") != null){
                followingId = (Object[]) (resultSet.getArray("following").getArray());
                for (Object tmp : followingId) {
                    following.add((int) tmp);
                }
            }
            if (resultSet.getArray("friend" )!= null){
                friendId = (Object[]) (resultSet.getArray("friend").getArray());
                for (Object tmp : friendId) {
                    friend.add((int) tmp);
                }
            }

            User user = new User(targetId, username, following, follower, friend);
            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
