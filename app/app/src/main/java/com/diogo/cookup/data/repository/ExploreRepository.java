package com.diogo.cookup.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.SearchData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreRepository {

    private final ApiService apiService;

    public ExploreRepository() {
        this.apiService = ApiRetrofit.getApiService();
    }

    public LiveData<ApiResponse<SearchData>> searchAll(String query) {
        MutableLiveData<ApiResponse<SearchData>> result = new MutableLiveData<>();

        apiService.searchAll("search_all", query).enqueue(new Callback<ApiResponse<SearchData>>() {
            @Override
            public void onResponse(Call<ApiResponse<SearchData>> call, Response<ApiResponse<SearchData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SearchData>> call, Throwable t) {
                result.setValue(null);
            }
        });

        return result;
    }

    public void searchAll(String query,
                          MutableLiveData<ApiResponse<SearchData>> resultLiveData,
                          MutableLiveData<String> errorLiveData) {

        apiService.searchAll("search_all", query).enqueue(new Callback<ApiResponse<SearchData>>() {
            @Override
            public void onResponse(Call<ApiResponse<SearchData>> call, Response<ApiResponse<SearchData>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    resultLiveData.postValue(response.body());
                } else {
                    errorLiveData.postValue("Erro: resposta inválida.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SearchData>> call, Throwable t) {
                errorLiveData.postValue("Falha: " + t.getMessage());
            }
        });
    }
}
