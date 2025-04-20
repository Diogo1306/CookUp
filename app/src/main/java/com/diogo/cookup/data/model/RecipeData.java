package com.diogo.cookup.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RecipeData implements Serializable {

    private int recipe_id;
    private String title;
    private String description;
    private String instructions;
    private String category;
    private int preparation_time;
    private List<String> utensils;
    private int servings;
    private String difficulty;
    private String image;
    private List<IngredientData> ingredients;
    private List<CommentData> comments;
    @SerializedName("average_rating")
    private float averageRating;

    public RecipeData(int recipe_id, String title, String description, String instructions, String category, int preparation_time, List<String> utensils, int servings, String difficulty, String image) {
        this.recipe_id = recipe_id;
        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.category = category;
        this.preparation_time = preparation_time;
        this.utensils = utensils;
        this.servings = servings;
        this.difficulty = difficulty;
        this.image = image;
    }

    public List<IngredientData> getIngredients() {return ingredients;}
    public int getRecipeId() { return recipe_id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getInstructions() { return instructions; }
    public String getCategory() { return category; }
    public int getPreparationTime() { return preparation_time; }
    public List<String> getUtensils() { return utensils; }
    public String getDifficulty() { return difficulty; }
    public int getServings() { return servings; }
    public String getImage() { return image; }
    public List<CommentData> getComments() {
        return comments;
    }
    public float getAverageRating() {return averageRating;}

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public void setCategory(String category) { this.category = category; }
    public void setPreparationTime(int preparationTime) { this.preparation_time = preparationTime; }
    public void setUtensils(List<String> utensils) { this.utensils = utensils; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public void setServings(int servings) { this.servings = servings; }
    public void setImage(String image) { this.image = image; }
    public void setIngredients(List<IngredientData> ingredients) {this.ingredients = ingredients;}
    public void setComments(List<CommentData> comments) {
        this.comments = comments;
    }
}
