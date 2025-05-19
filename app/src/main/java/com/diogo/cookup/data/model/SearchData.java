package com.diogo.cookup.data.model;

import java.util.List;

public class SearchData {

    private List<RecipeData> recipes;
    private List<IngredientData> ingredients;
    private List<RecipeCategoryData> recipe_categories;

    public List<RecipeData> getRecipes() {
        return recipes;
    }

    public List<IngredientData> getIngredients() {
        return ingredients;
    }

    public List<RecipeCategoryData> getRecipeCategories() {
        return recipe_categories;
    }
}
