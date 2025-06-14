package com.diogo.cookup.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.ApiResponseWithFilled;
import com.diogo.cookup.data.model.CommentData;
import com.diogo.cookup.data.model.CommentRequest;
import com.diogo.cookup.data.model.IngredientData;
import com.diogo.cookup.data.model.RatingRequest;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository {
    private final ApiService apiService;

    public RecipeRepository() {
        apiService = ApiRetrofit.getApiService();
    }

    public Call<ApiResponse<RecipeData>> saveOrUpdateRecipe(
            Integer recipeId,
            Integer authorId,
            String title,
            String description,
            String instructions,
            String difficulty,
            Integer preparationTime,
            Integer servings,
            List<Integer> categories,
            List<IngredientData> ingredients,
            List<File> imagensNovas,
            List<String> imagensAntigas
    ) {
        RequestBody rbAuthorId = RequestBody.create(MediaType.parse("text/plain"), authorId.toString());
        RequestBody rbTitle = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody rbDescription = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody rbInstructions = RequestBody.create(MediaType.parse("text/plain"), instructions);
        RequestBody rbDifficulty = RequestBody.create(MediaType.parse("text/plain"), difficulty);
        RequestBody rbPrepTime = RequestBody.create(MediaType.parse("text/plain"), preparationTime.toString());
        RequestBody rbServings = RequestBody.create(MediaType.parse("text/plain"), servings.toString());
        RequestBody rbCategories = RequestBody.create(
                MediaType.parse("text/plain"),
                new Gson().toJson(categories)
        );
        RequestBody rbIngredients = RequestBody.create(
                MediaType.parse("text/plain"),
                new Gson().toJson(ingredients)
        );
        RequestBody rbOldGallery = RequestBody.create(
                MediaType.parse("text/plain"),
                new Gson().toJson(imagensAntigas)
        );

        RequestBody rbRecipeId = recipeId != null
                ? RequestBody.create(MediaType.parse("text/plain"), recipeId.toString())
                : RequestBody.create(MediaType.parse("text/plain"), "");

        List<MultipartBody.Part> galleryParts = new ArrayList<>();
        for (File file : imagensNovas) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
            galleryParts.add(MultipartBody.Part.createFormData("gallery[]", file.getName(), fileBody));
        }

        return apiService.saveOrUpdateRecipe(
                rbRecipeId,
                rbAuthorId,
                rbTitle,
                rbDescription,
                rbInstructions,
                rbDifficulty,
                rbPrepTime,
                rbServings,
                rbCategories,
                rbIngredients,
                galleryParts,
                rbOldGallery
        );
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

    public void getRecipeDetail(int recipeId, MutableLiveData<RecipeData> recipeData, MutableLiveData<String> errorMessage) {
        apiService.getRecipeById("recipe", recipeId)
                .enqueue(new Callback<ApiResponse<RecipeData>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<RecipeData>> call, Response<ApiResponse<RecipeData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().isSuccess()) {
                                recipeData.postValue(response.body().getData());
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
