package com.diogo.cookup.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RecipeData implements Serializable {

    @SerializedName("recipe_id")
    private int recipeId;

    @SerializedName("author_id")
    private int authorId;

    @SerializedName("author_name")
    private String author_name;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("instructions")
    private String instructions;

    @SerializedName("categories")
    private List<CategoryData> categories;

    @SerializedName("preparation_time")
    private int preparationTime;

    @SerializedName("servings")
    private int servings;

    @SerializedName("finished_count")
    private int finishedcount;

    @SerializedName("views_count")
    private int viewsCount;

    @SerializedName("difficulty")
    private String difficulty;

    @SerializedName("image")
    private String image;

    @SerializedName("gallery")
    private List<String> gallery;

    @SerializedName("ingredients")
    private List<IngredientData> ingredients;

    @SerializedName("comments")
    private List<CommentData> comments;

    @SerializedName("average_rating")
    private float averageRating;

    public RecipeData() {}

    public RecipeData(int recipeId, int authorId, String author_name, String title, String description, String instructions,
                      List<CategoryData> categories, int preparationTime, int servings,
                      int viewsCount, int finishedcount, String difficulty, String image, List<String> gallery,
                      List<IngredientData> ingredients, List<CommentData> comments, float averageRating) {
        this.recipeId = recipeId;
        this.authorId = authorId;
        this.author_name = author_name;
        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.categories = categories;
        this.preparationTime = preparationTime;
        this.servings = servings;
        this.viewsCount = viewsCount;
        this.finishedcount = finishedcount;
        this.difficulty = difficulty;
        this.image = image;
        this.gallery = gallery;
        this.ingredients = ingredients;
        this.comments = comments;
        this.averageRating = averageRating;
    }

    // Getters
    public int getRecipeId() { return recipeId; }
    public int getAuthorId() { return authorId; }
    public String getAuthorName() {return author_name; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getInstructions() { return instructions; }
    public int getFinishedcount() { return finishedcount; }
    public List<CategoryData> getCategories() { return categories; }
    public int getPreparationTime() { return preparationTime; }
    public int getServings() { return servings; }
    public int getViewsCount() { return viewsCount; }
    public String getDifficulty() { return difficulty; }
    public String getImage() { return image; }
    public List<String> getGallery() { return gallery; }
    public List<IngredientData> getIngredients() { return ingredients; }
    public List<CommentData> getComments() { return comments; }
    public float getAverageRating() { return averageRating; }

    // Setters
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }
    public void SetAuthorName() { this.author_name = author_name; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public void setCategories(List<CategoryData> categories) { this.categories = categories; }
    public void setPreparationTime(int preparationTime) { this.preparationTime = preparationTime; }
    public void setServings(int servings) { this.servings = servings; }
    public void setViewsCount(int viewsCount) { this.viewsCount = viewsCount; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public void setImage(String image) { this.image = image; }
    public void setFinishedcount(int finishedcount) { this.finishedcount = finishedcount; }
    public void setGallery(List<String> gallery) { this.gallery = gallery; }
    public void setIngredients(List<IngredientData> ingredients) { this.ingredients = ingredients; }
    public void setComments(List<CommentData> comments) { this.comments = comments; }
    public void setAverageRating(float averageRating) { this.averageRating = averageRating; }
}