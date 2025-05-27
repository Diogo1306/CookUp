package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.SearchData;
import com.diogo.cookup.data.repository.ExploreRepository;

public class SearchViewModel extends ViewModel {
    private final ExploreRepository repository = new ExploreRepository();

    private final MutableLiveData<ApiResponse<SearchData>> searchResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ApiResponse<SearchData>> getSearchResult() { return searchResult; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public void fetchSuggestions(String query) {
        repository.fetchSearchResults(query, searchResult, errorMessage);
    }

    public LiveData<ApiResponse<SearchData>> fetchSearchResult(String query) {
        return repository.fetchSearchResults(query);
    }
}
