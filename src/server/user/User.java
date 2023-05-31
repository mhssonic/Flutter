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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashSet<User> getFollowing() {
        return following;
    }

    public void setFollowing(HashSet<User> following) {
        this.following = following;
    }

    public HashSet<User> getFollower() {
        return follower;
    }

    public void setFollower(HashSet<User> follower) {
        this.follower = follower;
    }

    public HashSet<User> getBlocked() {
        return blocked;
    }

    public void setBlocked(HashSet<User> blocked) {
        this.blocked = blocked;
    }
}
