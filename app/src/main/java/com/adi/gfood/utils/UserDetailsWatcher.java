package com.adi.gfood.utils;

import com.adi.gfood.models.UserDetails;

public class UserDetailsWatcher {
    public static boolean allOk;

    private static String firstName, lastName, displayName, emailAddress, profileImage, phone;

    private static String uid;

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        UserDetailsWatcher.uid = uid;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        UserDetailsWatcher.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        UserDetailsWatcher.lastName = lastName;
    }

    public static String getDisplayName() {
        return displayName;
    }

    public static void setDisplayName(String displayName) {
        UserDetailsWatcher.displayName = displayName;
    }

    public static String getEmailAddress() {
        return emailAddress;
    }

    public static void setEmailAddress(String emailAddress) {
        UserDetailsWatcher.emailAddress = emailAddress;
    }

    public static String getProfileImage() {
        return profileImage;
    }

    public static void setProfileImage(String profileImage) {
        UserDetailsWatcher.profileImage = profileImage;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        UserDetailsWatcher.phone = phone;
    }

    public static void setUserDetails(UserDetails userDetails){
        allOk = true;
        setFirstName(userDetails.getFirstName());
        setLastName(userDetails.getLastName());
        setDisplayName(userDetails.getDisplayName());
        setPhone(userDetails.getPhone());
        setEmailAddress(userDetails.getEmailAddress());
        setProfileImage(userDetails.getUserImage());
        setUid(userDetails.getUid());
    }
}
