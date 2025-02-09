package com.diogo.cookup.data.model;

public class UserData {
    private String firebase_uid;
    private String username;
    private String email;
    private String profile_picture;

    public UserData(String firebase_uid, String username, String email, String profile_picture) {
        this.firebase_uid = firebase_uid;
        this.username = username;
        this.email = email;

        if (profile_picture == null || profile_picture.isEmpty()) {
            this.profile_picture = "";
        } else {
            this.profile_picture = profile_picture;
        }
    }

    public String getFirebaseUid() { return firebase_uid; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getProfilePicture() { return profile_picture; }

    public void setUsername(String username) { this.username = username; }
    public void setProfilePicture(String profile_picture) { this.profile_picture = profile_picture; }
}
