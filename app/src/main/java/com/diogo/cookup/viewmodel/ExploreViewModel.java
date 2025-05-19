package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.SearchData;
import com.diogo.cookup.data.repository.ExploreRepository;

public class ExploreViewModel extends ViewModel {

    private final ExploreRepository repository;

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<SearchData>> searchResult = new MutableLiveData<>();

    public ExploreViewModel() {
        repository = new ExploreRepository();
    }

    public LiveData<ApiResponse<SearchData>> getSearchResult() {
        return searchResult;
    }

    public LiveData<ApiResponse<SearchData>> searchAll(String query) {
        return repository.searchAll(query);
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void search(String query) {
        repository.searchAll(query, searchResult, errorMessage);
    }
}
