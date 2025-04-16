package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeViewModel extends ViewModel {

    private final RecipeRepository recipeRepository;
    private final MutableLiveData<List<RecipeData>> recipesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Integer>> savedRecipeIds = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<RecipeData> recipeDetail = new MutableLiveData<>();


    public RecipeViewModel() {
        recipeRepository = new RecipeRepository();
    }

    public LiveData<RecipeData> getRecipeDetailLiveData() {
        return recipeDetail;
    }

    public LiveData<List<RecipeData>> getRecipesLiveData() {
        return recipesLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<Integer>> getSavedRecipeIds() {
        return savedRecipeIds;
    }

    public void loadRecipes() {
        recipeRepository.getAllRecipes(recipesLiveData, errorMessage);
    }

    public void loadRecipeDetail(int recipeId) {
        recipeRepository.getRecipeDetail(recipeId, recipeDetail, errorMessage);
    }

}
