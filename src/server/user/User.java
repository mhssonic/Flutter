package server.user;

import java.util.HashSet;

public class User {
    private String userId;
    private String profileId;
    private String userName;
    private String password;

    HashSet<Integer> following = new HashSet<>();
    HashSet<Integer> follower = new HashSet<>();
    HashSet<Integer> blocked = new HashSet<>();

    public User(String userName, HashSet<Integer> following, HashSet<Integer> follower) {
        this.userName = userName;
        this.following = following;
        this.follower = follower;
    }

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

    public HashSet<Integer> getFollowing() {
        return following;
    }

    public void setFollowing(HashSet<Integer> following) {
        this.following = following;
    }

    public HashSet<Integer> getFollower() {
        return follower;
    }

    public void setFollower(HashSet<Integer> follower) {
        this.follower = follower;
    }

    public HashSet<Integer> getBlocked() {
        return blocked;
    }

    public void setBlocked(HashSet<Integer> blocked) {
        this.blocked = blocked;
    }
}
