package com.diogo.cookup.viewmodel;

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

    public MutableLiveData<List<RecipeData>> getRecipesLiveData() {
        return recipesLiveData;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadRecipes() {
        recipeRepository.getAllRecipes(recipesLiveData, errorMessage);
    }
}
