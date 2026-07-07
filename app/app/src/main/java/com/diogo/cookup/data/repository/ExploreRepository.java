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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreRepository {

    private final ApiService apiService;

    public ExploreRepository() {
        this.apiService = ApiRetrofit.getApiService();
    }

    public LiveData<ApiResponse<List<RecipeData>>> getPopularRecipes(int page) {
        MutableLiveData<ApiResponse<List<RecipeData>>> liveData = new MutableLiveData<>();

        Call<ApiResponse<List<RecipeData>>> call = apiService.getPopularRecipesWithPage("popular_recipes_pagination", page);

        call.enqueue(new Callback<ApiResponse<List<RecipeData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<RecipeData>>> call, Response<ApiResponse<List<RecipeData>>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(new ApiResponse<>(false, "Lista vazia", new ArrayList<>()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<RecipeData>>> call, Throwable t) {
                liveData.setValue(new ApiResponse<>(false, t.getMessage(), new ArrayList<>()));
            }
        });

        return liveData;
    }

    public LiveData<ApiResponse<List<CategoryData>>> getPopularCategories() {
        MutableLiveData<ApiResponse<List<CategoryData>>> liveData = new MutableLiveData<>();

        apiService.getCategories("categories").enqueue(new Callback<ApiResponse<List<CategoryData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CategoryData>>> call, Response<ApiResponse<List<CategoryData>>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(new ApiResponse<>(false, "Sem categorias", new ArrayList<>()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CategoryData>>> call, Throwable t) {
                liveData.setValue(new ApiResponse<>(false, t.getMessage(), new ArrayList<>()));
            }
        });

        return liveData;
    }

    public void fetchSearchResults(String query, MutableLiveData<ApiResponse<SearchData>> result, MutableLiveData<String> error) {
        apiService.searchAll("search_all", query).enqueue(new Callback<ApiResponse<SearchData>>() {
            @Override
            public void onResponse(Call<ApiResponse<SearchData>> call, Response<ApiResponse<SearchData>> response) {
                if (response.body() != null) {
                    result.setValue(response.body());
                } else {
                    error.setValue("Resposta vazia");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SearchData>> call, Throwable t) {
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
                    result.setValue(response.body());
                } else {
                    result.setValue(new ApiResponse<>(false, "Resposta vazia", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SearchData>> call, Throwable t) {
                result.setValue(new ApiResponse<>(false, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<List<RecipeData>>> searchRecipesWithFilters(
            String query, String filter, Integer userId,
            String difficulty, Integer minTime, Integer maxTime, Integer maxIngredients,
            int limit, int offset
    ) {
        MutableLiveData<ApiResponse<List<RecipeData>>> liveData = new MutableLiveData<>();

        apiService.searchFilteredRecipes(
                "search_recipes_filtered", query, filter, userId, difficulty,
                minTime, maxTime, maxIngredients, limit, offset
        ).enqueue(new retrofit2.Callback<ApiResponse<List<RecipeData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<RecipeData>>> call, Response<ApiResponse<List<RecipeData>>> response) {
                if (response.body() != null) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(new ApiResponse<>(false, "Resposta vazia", new ArrayList<>()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<RecipeData>>> call, Throwable t) {
                liveData.setValue(new ApiResponse<>(false, t.getMessage(), new ArrayList<>()));
            }
        });

        return liveData;
    }
}
