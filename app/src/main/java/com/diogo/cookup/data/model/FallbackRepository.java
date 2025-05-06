package com.diogo.cookup.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diogo.cookup.data.model.ApiResponseWithFilled;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FallbackRepository {

    private final ApiService apiService;

    public FallbackRepository() {
        apiService = ApiRetrofit.getApiService();
    }

    public void getFallbackRecipes(MutableLiveData<List<RecipeData>> fallbackLiveData, MutableLiveData<String> errorLiveData) {
        apiService.getFallbackRecipes().enqueue(new Callback<ApiResponseWithFilled<List<RecipeData>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponseWithFilled<List<RecipeData>>> call, @NonNull Response<ApiResponseWithFilled<List<RecipeData>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    fallbackLiveData.postValue(response.body().getData());
                } else {
                    errorLiveData.postValue("Erro ao carregar fallback.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponseWithFilled<List<RecipeData>>> call, @NonNull Throwable t) {
                errorLiveData.postValue("Erro de rede no fallback: " + t.getMessage());
            }
        });
    }
}
