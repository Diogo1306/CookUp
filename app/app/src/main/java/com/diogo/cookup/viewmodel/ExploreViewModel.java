package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.CategoryData;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.repository.ExploreRepository;

import java.util.List;

public class ExploreViewModel extends ViewModel {
    private final ExploreRepository repository = new ExploreRepository();

    public LiveData<ApiResponse<List<RecipeData>>> getPopularRecipes() {
        return repository.getPopularRecipes();
    }

    public LiveData<ApiResponse<List<CategoryData>>> getPopularCategories() {
        return repository.getPopularCategories();
    }
}
