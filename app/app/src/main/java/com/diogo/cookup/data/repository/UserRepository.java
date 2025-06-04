package com.diogo.cookup.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final ApiService apiService;

    public UserRepository() {
        apiService =  ApiRetrofit.getApiService();
    }

    public void getUser(String firebaseUid, UserCallback callback) {
        apiService.getUser("user", firebaseUid).enqueue(new Callback<ApiResponse<UserData>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<UserData>> call, @NonNull Response<ApiResponse<UserData>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData(), response.body().getMessage());
                } else {
                    callback.onError("Resposta inválida ou falha no corpo");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<UserData>> call, @NonNull Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void createOrUpdateUser(UserData userData, UserCallback callback) {
        apiService.saveUser("user", userData).enqueue(new Callback<ApiResponse<UserData>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<UserData>> call, @NonNull Response<ApiResponse<UserData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<UserData> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData(), apiResponse.getMessage());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonError = new JSONObject(errorBody);
                        String errorMessage = jsonError.optString("message", "Erro desconhecido.");
                        callback.onError(errorMessage);
                    } catch (Exception e) {
                        callback.onError("Erro desconhecido ao processar resposta da API.");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<UserData>> call, @NonNull Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public interface UserCallback {
        void onSuccess(UserData user, String message);
        void onError(String message);
    }

}
