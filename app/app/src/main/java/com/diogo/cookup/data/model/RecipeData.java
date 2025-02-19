package com.diogo.cookup.data.model;

public class RecipeData {
    private int id;
    private String title;
    private String description;
    private String instructions;
    private String category;
    private int preparation_time	;
    private int servings;
    private String image;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getInstructions() { return instructions; }
    public String getCategory() { return category; }
    public int getPreparation_time	() { return preparation_time; }
    public int getServings() { return servings; }
    public String getImage() { return image; }
}
