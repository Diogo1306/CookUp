package com.diogo.cookup.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.TrackRequest;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingRepository {

    private final ApiService apiService;

    public TrackingRepository() {
        apiService = ApiRetrofit.getApiService();
    }

    public void sendTracking(TrackRequest request, MutableLiveData<Boolean> successLiveData) {
        apiService.trackInteraction("track_interaction", request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call, @NonNull Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    successLiveData.postValue(true);
                } else {
                    successLiveData.postValue(false);
                    Log.e("TRACKING", "Erro ao registar interação.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                successLiveData.postValue(false);
                Log.e("TRACKING", "Erro de rede: " + t.getMessage());
            }
        });
    }
}
