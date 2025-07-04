package com.diogo.cookup.data.model;

import com.google.gson.annotations.SerializedName;

public class TrackRequest {
    private int user_id;
    private int recipe_id;

    @SerializedName("interaction_type")
    private String type;

    public TrackRequest(int userId, int recipeId, String type) {
        this.user_id = userId;
        this.recipe_id = recipeId;
        this.type = type;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public String getType() {
        return type;
    }
}

