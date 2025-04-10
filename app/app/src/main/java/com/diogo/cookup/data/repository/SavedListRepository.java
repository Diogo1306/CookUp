package com.diogo.cookup.data.repository;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.model.SavedListData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;

import java.util.List;

import retrofit2.Callback;

public class SavedListRepository {

    private final ApiService api = ApiRetrofit.getApiService();

    public void getUserLists(int userId, Callback<ApiResponse<List<SavedListData>>> callback) {
        api.getUserLists("lists", "lists", userId).enqueue(callback);
    }

    public void createList(int userId, String name, String color, Callback<ApiResponse<Void>> callback) {
        api.createList("lists", userId, name, color).enqueue(callback);
    }

    public void addRecipeToList(int listId, int recipeId, Callback<ApiResponse<Void>> callback) {
        api.addRecipeToList("list_recipes", listId, recipeId).enqueue(callback);
    }

    public void removeRecipeFromList(int listId, int recipeId, Callback<ApiResponse<Void>> callback) {
        api.removeRecipeFromList("deleteRecipeFromList", listId, recipeId).enqueue(callback);
    }

    public void getRecipesFromList(int listId, Callback<ApiResponse<List<RecipeData>>> callback) {
        api.getRecipesFromList("list_recipes", listId).enqueue(callback);
    }

    public void getRecipeListIds(int recipeId, Callback<ApiResponse<List<Integer>>> callback) {
        api.getRecipeListIds("recipeLists", recipeId).enqueue(callback);
    }

    public void deleteList(int listId, Callback<ApiResponse<Void>> callback) {
        api.deleteList("deleteList", listId).enqueue(callback);
    }

    public void updateList(int listId, String name, String color, Callback<ApiResponse<Void>> callback) {
        api.updateList("update_list", listId, name, color).enqueue(callback);
    }

    public void getUserSavedRecipeIds(int userId, Callback<ApiResponse<List<Integer>>> callback) {
        api.getSavedRecipeIds("savedRecipeIds", userId).enqueue(callback);
    }

}
