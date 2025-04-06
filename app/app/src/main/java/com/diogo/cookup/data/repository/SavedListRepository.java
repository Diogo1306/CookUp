package com.diogo.cookup.data.repository;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.model.SavedListData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;

import java.util.List;

import retrofit2.Callback;

public class SavedListRepository {

    private final ApiService api = ApiRetrofit.getClient().create(ApiService.class);

    public void createList(int userId, String name, String color, Callback<ApiResponse<Void>> callback) {
        api.createList("lists", userId, name, color).enqueue(callback);
    }

    public void getUserLists(int userId, Callback<ApiResponse<List<SavedListData>>> callback) {
        api.getUserLists("lists", "getLists", userId).enqueue(callback);
    }

    public void addRecipeToList(int listId, int recipeId, Callback<ApiResponse<Void>> callback) {
        api.addRecipeToList("list_recipes", listId, recipeId).enqueue(callback);
    }

    public void getRecipesFromList(int listId, Callback<ApiResponse<List<RecipeData>>> callback) {
        api.getRecipesFromList("list_recipes", listId).enqueue(callback);
    }
}