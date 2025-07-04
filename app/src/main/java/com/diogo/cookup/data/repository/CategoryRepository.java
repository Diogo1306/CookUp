package com.diogo.cookup.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.CategoryData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {

    private final ApiService apiService;

    public CategoryRepository() {
        apiService = ApiRetrofit.getApiService();
    }

    public void getCategories(MutableLiveData<List<CategoryData>> categoriesLiveData,
                              MutableLiveData<String> errorMessage) {
        apiService.getCategories("categories").enqueue(new Callback<ApiResponse<List<CategoryData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CategoryData>>> call,
                                   Response<ApiResponse<List<CategoryData>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    categoriesLiveData.postValue(response.body().getData());
                } else {
                    errorMessage.postValue("Erro: " + (response.body() != null ? response.body().getMessage() : "Resposta inválida."));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CategoryData>>> call, Throwable t) {
                errorMessage.postValue("Falha: " + t.getMessage());
            }
        });
    }

    public void getRecommendedCategories(int userId, MutableLiveData<List<CategoryData>> categoriesLiveData, MutableLiveData<String> errorMessage) {
        apiService.getUserCategories("user_categories", userId)
                .enqueue(new Callback<ApiResponse<List<CategoryData>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<CategoryData>>> call,
                                           Response<ApiResponse<List<CategoryData>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            categoriesLiveData.postValue(response.body().getData());
                        } else {
                            errorMessage.postValue("Erro ao buscar categorias personalizadas.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<CategoryData>>> call, Throwable t) {
                        errorMessage.postValue("Falha: " + t.getMessage());
                    }
                });
    }
}
