package com.diogo.cookup.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.repository.RecipeRepository;
import java.util.List;

public class RecipeViewModel extends ViewModel {
    private final RecipeRepository recipeRepository;
    private final MutableLiveData<List<RecipeData>> recipesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

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
        Log.d("API_DEBUG", "Carregando receitas...");
        recipeRepository.getAllRecipes(recipesLiveData, errorMessage);
    }

}
