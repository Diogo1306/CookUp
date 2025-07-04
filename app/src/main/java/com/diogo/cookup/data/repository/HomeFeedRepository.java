package com.diogo.cookup.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.HomeFeedData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFeedRepository {

    private final ApiService apiService;

    public interface HomeFeedCallback {
        void onSuccess(HomeFeedData data);
        void onError(String error);
    }

    public HomeFeedRepository() {
        apiService = ApiRetrofit.getApiService();
    }

    public void getFullHomeFeed(int userId, HomeFeedCallback callback) {
        apiService.getFullHomeFeed("home_feed", userId).enqueue(new Callback<ApiResponse<HomeFeedData>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<HomeFeedData>> call, @NonNull Response<ApiResponse<HomeFeedData>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Erro ao carregar o feed da Home.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<HomeFeedData>> call, @NonNull Throwable t) {
                callback.onError("Erro: " + t.getMessage());
            }
        });
    }
}
