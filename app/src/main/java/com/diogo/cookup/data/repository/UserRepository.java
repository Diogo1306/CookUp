package com.diogo.cookup.data.repository;

import androidx.annotation.NonNull;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final ApiService apiService;

    public UserRepository() {
        this.apiService = ApiRetrofit.getClient().create(ApiService.class);
    }

    public void createOrUpdateUser(UserData userData, UserCallback callback) {
        apiService.createOrUpdateUser(userData).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(userData);
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Nome de usuário em uso: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void getUser(String firebaseUid, UserCallback callback) {
        apiService.getUser(firebaseUid).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Usuário não encontrado (HTTP " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public interface UserCallback {
        void onSuccess(UserData user);
        void onError(String message);
    }
}
