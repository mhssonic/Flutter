package server.user;

import java.time.LocalDate;

public class Profile {
    private String profileId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String country;
    private String birthdate;
    private String biography;
    private String avatarPath;
    private String headerPath;
    private LocalDate lastEdite;

    public Profile(String firstName, String lastName, String email, String phoneNumber, String country, String birthdate, String biography, String avatarPath, String headerPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.birthdate = birthdate;
        this.biography = biography;
        this.avatarPath = avatarPath;
        this.headerPath = headerPath;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

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

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getHeaderPath() {
        return headerPath;
    }

    public void setHeaderPath(String headerPath) {
        this.headerPath = headerPath;
    }

    public LocalDate getLastEdite() {
        return lastEdite;
    }

    public void setLastEdite(LocalDate lastEdite) {
        this.lastEdite = lastEdite;
    }
}
