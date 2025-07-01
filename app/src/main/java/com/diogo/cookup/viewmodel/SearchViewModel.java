package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.model.SearchData;
import com.diogo.cookup.data.repository.ExploreRepository;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private final ExploreRepository repository = new ExploreRepository();

    private final MutableLiveData<ApiResponse<SearchData>> searchResult = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<List<RecipeData>>> filteredRecipesResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private String currentQuery = "";
    private String filter = "";
    private String difficulty = "";
    private Integer maxTime = null;
    private Integer maxIngredients = null;

    public LiveData<ApiResponse<SearchData>> getSearchResult() {
        return searchResult;
    }

    public void saveSearchState(String query, String filter, String difficulty, Integer maxTime, Integer maxIngredients) {
        this.currentQuery = query;
        this.filter = filter;
        this.difficulty = difficulty;
        this.maxTime = maxTime;
        this.maxIngredients = maxIngredients;
    }

    public String getCurrentQuery() { return currentQuery; }
    public String getFilter() { return filter; }
    public String getDifficulty() { return difficulty; }
    public Integer getMaxTime() { return maxTime; }
    public Integer getMaxIngredients() { return maxIngredients; }

    public LiveData<ApiResponse<List<RecipeData>>> getFilteredRecipesResult() {
        return filteredRecipesResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchSuggestions(String query) {
        repository.fetchSearchResults(query, searchResult, errorMessage);
    }

    public void searchRecipesWithFilters(String query, String filter, Integer userId,
                                         String difficulty, Integer minTime, Integer maxTime,
                                         Integer maxIngredients, int limit, int offset) {
        repository.searchRecipesWithFilters(query, filter, userId, difficulty, minTime, maxTime, maxIngredients, limit, offset)
                .observeForever(response -> {
                    filteredRecipesResult.postValue(response);
                });
    }
}
