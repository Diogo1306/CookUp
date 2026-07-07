package com.diogo.cookup.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SearchData {

    @SerializedName("recipes")
    private List<RecipeData> recipes;

    @SerializedName("ingredients")
    private List<IngredientData> ingredients;

    @SerializedName("recipe_categories")
    private List<CategoryData> recipeCategories;

    public List<RecipeData> getRecipes() {
        return recipes;
    }

    public List<IngredientData> getIngredients() {
        return ingredients;
    }

    public List<CategoryData> getRecipeCategories() {
        return recipeCategories;
    }
}
