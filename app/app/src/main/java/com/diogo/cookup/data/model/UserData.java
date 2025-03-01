package com.diogo.cookup.data.model;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("firebase_uid")
    private String firebaseUid;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("profile_picture")
    private String profilePicture;

    public UserData() {}

    public UserData(String firebaseUid, String username, String email, String profilePicture) {
        this.firebaseUid = firebaseUid;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public String getFirebaseUid() { return firebaseUid; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getProfilePicture() { return profilePicture; }

    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }
    public void setUsername(String username) { this.username = username;}
    public void setEmail(String email) { this.email = email; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
}
