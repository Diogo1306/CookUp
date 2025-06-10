package com.diogo.cookup.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.diogo.cookup.data.model.IngredientData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientRepository {
    private final ApiService apiService;

    public IngredientRepository() {
        apiService = ApiRetrofit.getApiService();
    }


    public void fetchSuggestions(String q,
                                 MutableLiveData<List<IngredientData>> liveData,
                                 MutableLiveData<String> errorLiveData) {
        apiService.autocompleteIngredients("autocomplete_ingredient", q).enqueue(new Callback<List<IngredientData>>() {
            @Override
            public void onResponse(Call<List<IngredientData>> call, Response<List<IngredientData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                } else {
                    errorLiveData.postValue("Erro ao buscar ingredientes.");
                }
            }

            @Override
            public void onFailure(Call<List<IngredientData>> call, Throwable t) {
                errorLiveData.postValue("Falha: " + t.getMessage());
            }
        });
    }
}


