package com.diogo.cookup.data.model;

import com.google.gson.annotations.SerializedName;

public class CommentData {

    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("rating")
    private float rating;
    @SerializedName("comment")
    private String comment;
    @SerializedName("created_at")
    private String createdAt;

    public CommentData(String name, String image, float rating, String comment, String createdAt) {
        this.name = name;
        this.image = image;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
