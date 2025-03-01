package com.diogo.cookup.data.model;

public class RecipeData {
    private int id;
    private String title;
    private String description;
    private String instructions;
    private String category;
    private int preparationTime;
    private int servings;
    private String image;

    public RecipeData(int id, String title, String description, String instructions, String category, int preparationTime, int servings, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.category = category;
        this.preparationTime = preparationTime;
        this.servings = servings;
        this.image = image;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getInstructions() { return instructions; }
    public String getCategory() { return category; }
    public int getPreparationTime() { return preparationTime; }
    public int getServings() { return servings; }
    public String getImage() { return image; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public void setCategory(String category) { this.category = category; }
    public void setPreparationTime(int preparationTime) { this.preparationTime = preparationTime; }
    public void setServings(int servings) { this.servings = servings; }
    public void setImage(String image) { this.image = image; }
}
