package server.user;

import java.util.HashSet;

public class User {
    private String userId;
    private String profileId;
    private String userName;
    private String password;

    HashSet<User> following = new HashSet<>();
    HashSet<User> follower = new HashSet<>();
    HashSet<User> blocked = new HashSet<>();
}
