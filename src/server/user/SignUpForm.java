package server.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

public class SignUpForm {
    private int id;
    @JsonProperty("first-name")
    private String firstName;
    @JsonProperty("last-name")
    private String lastName;
    private String email;

    private String phoneNumber;
    private String country;
    private String birthdate;
    private String biography;
    private int avatar;
    private int header;

    private String username;
    private String password;

    private String confirmPassword;

    private HashSet<Integer> friend;
    private HashSet<Integer> follower;
    private HashSet<Integer> following;

    private int countFollowing;
    private int countFollower;

    public SignUpForm(User user , Profile profile) {
        this.id = user.getUserId();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.email = profile.getEmail();
        this.phoneNumber = profile.getPhoneNumber();
        this.country = profile.getCountry();
        this.birthdate = profile.getBirthdate();
        this.biography = profile.getBiography();
        this.avatar = profile.getAvatar();
        this.header = profile.getHeader();
        this.username = user.getUserName();
        this.follower = user.getFollower();
        this.following = user.getFollowing();
        this.friend = user.getFriend();
        this.countFollower = follower.size();
        this.countFollowing = following.size();
    }
@JsonProperty("count-following")
    public int getCountFollowing() {
        return countFollowing;
    }
    @JsonIgnore
    public void setCountFollowing(int countFollowing) {
        this.countFollowing = countFollowing;
    }

    @JsonProperty("count-follower")
    public int getCountFollower() {
        return countFollower;
    }
    @JsonIgnore
    public void setCountFollower(int countFollower) {
        this.countFollower = countFollower;
    }

    @JsonIgnore
    public HashSet<Integer> getFriend() {
        return friend;
    }
    @JsonIgnore
    public void setFriend(HashSet<Integer> friend) {
        this.friend = friend;
    }
    @JsonProperty
    public HashSet<Integer> getFollower() {
        return follower;
    }
    @JsonIgnore
    public void setFollower(HashSet<Integer> follower) {
        this.follower = follower;
    }
    @JsonProperty
    public HashSet<Integer> getFollowing() {
        return following;
    }
    @JsonIgnore
    public void setFollowing(HashSet<Integer> following) {
        this.following = following;
    }

    public SignUpForm(){}

    @JsonProperty
    public int getId() {
        return id;
    }
    @JsonIgnore
    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public String getConfirmPassword() {
        return confirmPassword;
    }

    @JsonProperty("confirm-password")
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonIgnore
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phone-number")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}
