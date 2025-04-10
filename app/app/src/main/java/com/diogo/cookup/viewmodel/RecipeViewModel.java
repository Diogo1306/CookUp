package com.diogo.cookup.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewModel extends ViewModel {
    private final RecipeRepository recipeRepository;
    private final MutableLiveData<List<RecipeData>> recipesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Integer>> savedRecipeIds = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Integer>> getSavedRecipeIds() { return savedRecipeIds; }

    public RecipeViewModel() {
        recipeRepository = new RecipeRepository();
    }

    public LiveData<List<RecipeData>> getRecipesLiveData() {
        return recipesLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadRecipes() {
        recipeRepository.getAllRecipes(recipesLiveData, errorMessage);
    }

    public void loadSavedRecipeIds(int userId) {
    }

}
