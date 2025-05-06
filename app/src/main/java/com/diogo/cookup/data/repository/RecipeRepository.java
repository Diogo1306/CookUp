package com.diogo.cookup.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.ApiResponseWithFilled;
import com.diogo.cookup.data.model.CommentData;
import com.diogo.cookup.data.model.CommentRequest;
import com.diogo.cookup.data.model.RatingRequest;
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
            public void onResponse(Call<ApiResponse<List<RecipeData>>> call, Response<ApiResponse<List<RecipeData>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    recipesLiveData.postValue(response.body().getData());
                } else {
                    errorMessage.postValue(response.body() != null ? response.body().getMessage() : "Erro ao buscar receitas.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<RecipeData>>> call, Throwable t) {
                errorMessage.postValue("Erro de rede: " + t.getMessage());
            }
        });
    }

    public void getRecipesByCategory(int categoryId, MutableLiveData<List<RecipeData>> recipesLiveData, MutableLiveData<String> errorMessage) {
        apiService.getRecipesByCategory("recipes_by_category", categoryId)
                .enqueue(new Callback<ApiResponseWithFilled<List<RecipeData>>>() {
                    @Override
                    public void onResponse(Call<ApiResponseWithFilled<List<RecipeData>>> call, Response<ApiResponseWithFilled<List<RecipeData>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            recipesLiveData.postValue(response.body().getData());
                        } else {
                            errorMessage.postValue("Erro ao carregar receitas da categoria.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponseWithFilled<List<RecipeData>>> call, Throwable t) {
                        errorMessage.postValue("Erro de rede: " + t.getMessage());
                    }
                });
    }

    public void loadWeeklyRecipes(MutableLiveData<List<RecipeData>> liveData, MutableLiveData<String> error) {
        Log.d("HOME_FEED", "Chamando API: weekly");

        apiService.getWeeklyRecipes("weekly").enqueue(new Callback<ApiResponseWithFilled<List<RecipeData>>>() {
            @Override
            public void onResponse(Call<ApiResponseWithFilled<List<RecipeData>>> call, Response<ApiResponseWithFilled<List<RecipeData>>> response) {
                Log.d("HOME_FEED", "Resposta weekly recebida: " + (response.body() != null ? response.body().getData().size() : "null"));

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    liveData.postValue(response.body().getData());
                } else {
                    error.postValue("Erro ao buscar receitas da semana.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponseWithFilled<List<RecipeData>>> call, Throwable t) {
                error.postValue("Falha: " + t.getMessage());
                Log.e("HOME_FEED", "Falha weekly: " + t.getMessage());
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
                                errorMessage.postValue(response.body().getMessage());
                            }
                        } else {
                            errorMessage.postValue("Erro ao buscar detalhes da receita.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<RecipeData>> call, Throwable t) {
                        errorMessage.postValue("Erro de rede: " + t.getMessage());
                    }
                });
    }

    public void getRecommendedRecipes(MutableLiveData<List<RecipeData>> recipesLiveData, MutableLiveData<String> errorMessage) {
        apiService.getRecommendedRecipes("recommended")
                .enqueue(new Callback<ApiResponse<List<RecipeData>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<RecipeData>>> call,
                                           Response<ApiResponse<List<RecipeData>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            recipesLiveData.postValue(response.body().getData());
                        } else {
                            errorMessage.postValue("Erro ao carregar recomendados.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<RecipeData>>> call, Throwable t) {
                        errorMessage.postValue("Falha: " + t.getMessage());
                    }
                });
    }

    public void getComments(int recipeId, MutableLiveData<List<CommentData>> commentsLiveData, MutableLiveData<String> errorLiveData) {
        apiService.getComments("comment", recipeId).enqueue(new Callback<ApiResponse<List<CommentData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CommentData>>> call, Response<ApiResponse<List<CommentData>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    commentsLiveData.postValue(response.body().getData());
                } else {
                    errorLiveData.postValue("Erro ao buscar comentários.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CommentData>>> call, Throwable t) {
                errorLiveData.postValue("Erro de rede: " + t.getMessage());
            }
        });
    }

    public void submitComment(CommentRequest request, MutableLiveData<Boolean> successLiveData, MutableLiveData<String> errorLiveData) {
        apiService.submitComment("comment", request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    successLiveData.postValue(true);
                } else {
                    errorLiveData.postValue("Erro ao enviar comentário.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                errorLiveData.postValue("Erro de rede: " + t.getMessage());
            }
        });
    }

    public void submitRating(RatingRequest request, MutableLiveData<Boolean> successLiveData, MutableLiveData<String> errorLiveData) {
        apiService.submitRating("rating", request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    successLiveData.postValue(true);
                } else {
                    errorLiveData.postValue("Erro ao enviar avaliação.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                errorLiveData.postValue("Erro de rede: " + t.getMessage());
            }
        });
    }

    public void getAverageRating(int recipeId, MutableLiveData<Float> averageLiveData, MutableLiveData<String> errorLiveData) {
        apiService.getAverageRating("rating", recipeId).enqueue(new Callback<ApiResponse<Float>>() {
            @Override
            public void onResponse(Call<ApiResponse<Float>> call, Response<ApiResponse<Float>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    averageLiveData.postValue(response.body().getData());
                } else {
                    errorLiveData.postValue("Erro ao buscar média.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Float>> call, Throwable t) {
                errorLiveData.postValue("Erro de rede: " + t.getMessage());
            }
        });
    }
}
