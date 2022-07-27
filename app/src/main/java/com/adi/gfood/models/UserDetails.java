package com.adi.gfood.models;

public class UserDetails {
    private String firstName, lastName, displayName, emailAddress, profileImage, phone, userImage;

    private String uid;

    public UserDetails(){}

    public UserDetails(String firstName, String lastName, String displayName, String emailAddress, String phone, String userImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.emailAddress = emailAddress;
        this.phone = phone;
        this.userImage = userImage;
    }

    public UserDetails(String firstName, String lastName, String emailAddress, String userImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = firstName + " " + lastName;
        this.emailAddress = emailAddress;
        this.userImage = userImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
