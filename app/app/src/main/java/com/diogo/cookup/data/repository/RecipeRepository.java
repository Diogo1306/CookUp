package com.diogo.cookup.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;
import com.google.gson.Gson;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository {
    private final ApiService apiService;

    public RecipeRepository() {
        apiService = ApiRetrofit.getClient().create(ApiService.class);
    }

    public void getAllRecipes(MutableLiveData<List<RecipeData>> recipesLiveData, MutableLiveData<String> errorMessage) {
        apiService.getAllRecipes(true).enqueue(new Callback<ApiResponse<List<RecipeData>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<RecipeData>>> call, @NonNull Response<ApiResponse<List<RecipeData>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<RecipeData> recipes = response.body().getData();
                    Log.d("DEBUG_JSON", "Recebido: " + new Gson().toJson(recipes));
                    if (recipes != null) {
                        recipesLiveData.postValue(recipes);
                    } else {
                        errorMessage.postValue("Nenhuma receita encontrada.");
                    }
                } else {
                    errorMessage.postValue("Erro ao buscar receitas.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<RecipeData>>> call, @NonNull Throwable t) {
                errorMessage.postValue("Erro de conexão: " + t.getMessage());
            }
        });
    }
}