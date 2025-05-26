package com.diogo.cookup.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CategoryData {

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("category_image_url")
    private String imageUrl;

    @SerializedName("category_color")
    private String color;

    @SerializedName("recipes")
    private List<RecipeData> recipes;

    public int getCategoryId() { return categoryId; }
    public String getColor() { return color; }
    public String getCategoryName() { return categoryName; }
    public String getImageUrl() { return imageUrl; }
    public List<RecipeData> getRecipes() { return recipes; }
}
