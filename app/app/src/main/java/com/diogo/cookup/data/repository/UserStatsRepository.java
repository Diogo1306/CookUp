package com.diogo.cookup.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserStatsRepository {

    private final ApiService apiService;

    public UserStatsRepository() {
        apiService = ApiRetrofit.getApiService();
    }

    public void loadUserRecommendedRecipes(int userId, MutableLiveData<List<RecipeData>> liveData, MutableLiveData<String> errorLiveData) {
        apiService.getUserRecommendedRecipes("recommended_user", userId).enqueue(new Callback<ApiResponse<List<RecipeData>>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<List<RecipeData>>> call, @NonNull Response<ApiResponse<List<RecipeData>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            liveData.postValue(response.body().getData());
                        } else {
                            errorLiveData.postValue("Erro ao buscar recomendações personalizadas.");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<List<RecipeData>>> call, @NonNull Throwable t) {
                        errorLiveData.postValue("Erro de rede: " + t.getMessage());
                    }
                });
    }
}
