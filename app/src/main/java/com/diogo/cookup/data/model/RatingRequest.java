package com.diogo.cookup.data.model;

public class RatingRequest {
    private int user_id;
    private int recipe_id;
    private int rating;

    public RatingRequest(int user_id, int recipe_id, int rating) {
        this.user_id = user_id;
        this.recipe_id = recipe_id;
        this.rating = rating;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public int getRating() {
        return rating;
    }
}
