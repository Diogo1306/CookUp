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
        apiService = ApiRetrofit.getApiService();
    }

    public void getAllRecipes(MutableLiveData<List<RecipeData>> recipesLiveData, MutableLiveData<String> errorMessage) {
        apiService.getAllRecipes("recipe").enqueue(new Callback<ApiResponse<List<RecipeData>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<RecipeData>>> call, @NonNull Response<ApiResponse<List<RecipeData>>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isSuccess()) {
                        List<RecipeData> recipes = response.body().getData();

                        if (recipes != null && !recipes.isEmpty()) {
                            recipesLiveData.postValue(recipes);
                        } else {
                            recipesLiveData.postValue(recipes);
                        }

                    } else {
                        String msg = response.body().getMessage();
                        errorMessage.postValue(msg);
                    }

                } else {
                    errorMessage.postValue("Erro ao buscar receitas. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<RecipeData>>> call, @NonNull Throwable t) {
                errorMessage.postValue("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void getRecipeDetail(int recipeId, MutableLiveData<RecipeData> recipeData, MutableLiveData<String> errorMessage) {
        Log.d("RECIPE_API", "Buscando detalhes da receita com ID: " + recipeId);

        apiService.getRecipeDetail("recipe_detail", "getRecipeDetail", recipeId)
                .enqueue(new Callback<ApiResponse<RecipeData>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<RecipeData>> call, Response<ApiResponse<RecipeData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("RECIPE_API", "Resposta crua: " + new Gson().toJson(response.body()));

                            if (response.body().isSuccess()) {
                                recipeData.postValue(response.body().getData());
                                Log.d("RECIPE_API", "Dados da receita: " + new Gson().toJson(response.body().getData()));
                            } else {
                                Log.e("RECIPE_API", "API retornou success=false: " + response.body().getMessage());
                                errorMessage.postValue(response.body().getMessage());
                            }
                        } else {
                            Log.e("RECIPE_API", "Falha na resposta. Código: " + response.code());
                            errorMessage.postValue("Erro ao buscar detalhes da receita.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<RecipeData>> call, Throwable t) {
                        Log.e("RECIPE_API", "Erro de rede: " + t.getMessage());
                        errorMessage.postValue("Erro de rede: " + t.getMessage());
                    }
                });
    }

}
