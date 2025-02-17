package com.diogo.cookup.data.model;

public class UserData {
    private String firebase_uid;
    private String username;
    private String email;
    private String profile_picture;

    public UserData() {
    }

    public UserData(String firebase_uid, String username, String email, String profile_picture) {
        this.firebase_uid = firebase_uid;
        this.username = username;
        this.email = email;
        this.profile_picture = profile_picture;
    }

    public String getFirebase_uid() {
        return firebase_uid;
    }

    public void setFirebase_uid(String firebase_uid) {
        this.firebase_uid = firebase_uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
