package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.CategoryData;
import com.diogo.cookup.data.repository.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private final CategoryRepository categoryRepository;
    private final MutableLiveData<List<CategoryData>> categoriesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public CategoryViewModel() {
        categoryRepository = new CategoryRepository();
    }

    public LiveData<List<CategoryData>> getCategoriesLiveData() {
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
