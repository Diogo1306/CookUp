// Novo ExploreRepository.java (sem métodos duplicados e padronizado)
package com.diogo.cookup.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.CategoryData;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.model.SearchData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreRepository {

    private final ApiService apiService;

    public ExploreRepository() {
        this.apiService = ApiRetrofit.getApiService();
    }

    public void fetchSearchResults(String query, MutableLiveData<ApiResponse<SearchData>> result, MutableLiveData<String> error) {
        apiService.searchAll("search_all", query).enqueue(new Callback<ApiResponse<SearchData>>() {
            @Override
            public void onResponse(Call<ApiResponse<SearchData>> call, Response<ApiResponse<SearchData>> response) {
                if (response.body() != null) {
                    Log.d("ExploreRepository", "📦 Resposta da API: " + new Gson().toJson(response.body()));
                    result.setValue(response.body());
                } else {
                    Log.d("ExploreRepository", "⚠️ Resposta da API veio nula");
                    error.setValue("Resposta vazia");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SearchData>> call, Throwable t) {
                Log.e("ExploreRepository", "❌ Erro na chamada", t);
                error.setValue("Erro: " + t.getMessage());
            }
        });
    }

    public LiveData<ApiResponse<SearchData>> fetchSearchResults(String query) {
        MutableLiveData<ApiResponse<SearchData>> result = new MutableLiveData<>();

        apiService.searchAll("search_all", query).enqueue(new Callback<ApiResponse<SearchData>>() {
            @Override
            public void onResponse(Call<ApiResponse<SearchData>> call, Response<ApiResponse<SearchData>> response) {
                if (response.body() != null) {
                    Log.d("ExploreRepository", "📦 LiveData SearchAll: " + new Gson().toJson(response.body()));
                    result.setValue(response.body());
                } else {
                    Log.d("ExploreRepository", "⚠️ LiveData SearchAll veio nula");
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SearchData>> call, Throwable t) {
                Log.e("ExploreRepository", "❌ LiveData SearchAll erro", t);
                result.setValue(null);
            }
        });

        return result;
    }

    public LiveData<ApiResponse<List<RecipeData>>> getPopularRecipes() {
        MutableLiveData<ApiResponse<List<RecipeData>>> liveData = new MutableLiveData<>();
        apiService.getPopularRecipes("popular_recipes").enqueue(new Callback<ApiResponse<List<RecipeData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<RecipeData>>> call, Response<ApiResponse<List<RecipeData>>> response) {
                liveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<ApiResponse<List<RecipeData>>> call, Throwable t) {
                liveData.setValue(null);
            }
        });
        return liveData;
    }

    public LiveData<ApiResponse<List<CategoryData>>> getPopularCategories() {
        MutableLiveData<ApiResponse<List<CategoryData>>> liveData = new MutableLiveData<>();
        apiService.getPopularCategories("popular_categories").enqueue(new Callback<ApiResponse<List<CategoryData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CategoryData>>> call, Response<ApiResponse<List<CategoryData>>> response) {
                liveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<ApiResponse<List<CategoryData>>> call, Throwable t) {
                liveData.setValue(null);
            }
        });
        return liveData;
    }
}
