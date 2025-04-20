package com.diogo.cookup.data.model;

public class CommentRequest {
    private int user_id;
    private int recipe_id;
    private String comment;

    public CommentRequest(int user_id, int recipe_id, String comment) {
        this.user_id = user_id;
        this.recipe_id = recipe_id;
        this.comment = comment;
    }

    public int getUser_id() { return user_id; }
    public int getRecipe_id() { return recipe_id; }
    public String getComment() { return comment; }
}
