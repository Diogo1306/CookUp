package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.CategoryData;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.repository.ExploreRepository;

import java.util.ArrayList;
import java.util.List;

public class ExploreViewModel extends ViewModel {

    private final ExploreRepository repository = new ExploreRepository();

    private final MutableLiveData<List<RecipeData>> allRecipes = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isLastPage = new MutableLiveData<>(false);
    private final MutableLiveData<ApiResponse<List<CategoryData>>> categories = new MutableLiveData<>();

    private int currentPage = 1;
    public static final int PAGE_SIZE = 6;

    public LiveData<List<RecipeData>> getAllRecipes() {return allRecipes;}
    public LiveData<Boolean> getIsLoading() {return isLoading;}
    public LiveData<Boolean> getIsLastPage() {return isLastPage;}
    public LiveData<ApiResponse<List<CategoryData>>> getPopularCategories() {return categories;}

    public void loadCategories() {
        repository.getPopularCategories().observeForever(response -> {
            if (response != null && response.getData() != null) {
                categories.setValue(response);
            } else {
                categories.setValue(new ApiResponse<>(false, "Nenhuma categoria", new ArrayList<>()));
            }
        });
    }

    public void loadNextPage() {
        if (Boolean.TRUE.equals(isLoading.getValue()) || Boolean.TRUE.equals(isLastPage.getValue())) {
            return;
        }

        isLoading.setValue(true);
        int pageToCall = currentPage;

        repository.getPopularRecipes(pageToCall).observeForever(response -> {
            isLoading.setValue(false);

            if (response != null && response.isSuccess() && response.getData() != null) {
                List<RecipeData> novas = response.getData();

                if (!novas.isEmpty()) {
                    List<RecipeData> listaAtual = allRecipes.getValue();
                    if (listaAtual == null) {
                        listaAtual = new ArrayList<>();
                    }
                    int oldSize = listaAtual.size();
                    listaAtual.addAll(novas);
                    allRecipes.setValue(listaAtual);

                    if (novas.size() < PAGE_SIZE) {
                        isLastPage.setValue(true);
                    } else {
                        currentPage++;
                    }
                } else {
                    isLastPage.setValue(true);
                }
            } else {
                isLastPage.setValue(true);
            }
        });
    }

    public void resetPagination() {
        currentPage = 1;
        allRecipes.setValue(new ArrayList<>());
        isLastPage.setValue(false);
    }
}
