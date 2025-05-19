package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.RecipeCategoryData;
import com.diogo.cookup.data.repository.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private final CategoryRepository categoryRepository;
    private final MutableLiveData<List<RecipeCategoryData>> categoriesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public CategoryViewModel() {
        categoryRepository = new CategoryRepository();
    }

    public LiveData<List<RecipeCategoryData>> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadCategories() {
        categoryRepository.getCategories(categoriesLiveData, errorMessage);
    }

    public void loadUserCategories(int userId) {
        categoryRepository.getRecommendedCategories(userId, categoriesLiveData, errorMessage);
    }
}
