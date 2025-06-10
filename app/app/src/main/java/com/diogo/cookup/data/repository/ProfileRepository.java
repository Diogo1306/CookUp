package com.diogo.cookup.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.ProfileData;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    private final ApiService apiService;

    public ProfileRepository() {
        apiService = ApiRetrofit.getApiService();
    }

    public interface DeleteCallback {
        void onSuccess();

        void onFailure(String errorMsg);
    }

    public void getProfileSummary(int userId, MutableLiveData<ProfileData> data, MutableLiveData<String> error) {
        apiService.getProfileSummary("profile_summary", userId)
                .enqueue(new Callback<ApiResponse<ProfileData>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<ProfileData>> call, @NonNull Response<ApiResponse<ProfileData>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            data.postValue(response.body().getData());
                        } else {
                            error.postValue("Erro ao carregar resumo do perfil.");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<ProfileData>> call, @NonNull Throwable t) {
                        error.postValue("Falha de conexão: " + t.getMessage());
                    }
                });
    }

    public void getProfileRecipes(int userId, MutableLiveData<List<RecipeData>> data, MutableLiveData<String> error) {
        apiService.getProfileRecipes("profile_recipes", userId)
                .enqueue(new Callback<ApiResponse<List<RecipeData>>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<List<RecipeData>>> call, @NonNull Response<ApiResponse<List<RecipeData>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            data.postValue(response.body().getData());
                        } else {
                            Log.e("ProfileRepo", "Erro ao carregar receitas: " + response.code() + ", body: " + response.body());
                            error.postValue("Erro ao carregar receitas do usuário.");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<List<RecipeData>>> call, @NonNull Throwable t) {
                        error.postValue("Falha de conexão: " + t.getMessage());
                    }
                });
    }

    public void deleteRecipe(int recipeId, DeleteCallback callback) {
        apiService.deleteRecipe("delete_recipe", recipeId)
                .enqueue(new Callback<ApiResponse<Void>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure("Erro ao deletar receita.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                        callback.onFailure("Erro de rede: " + t.getMessage());
                    }
                });
    }
}
