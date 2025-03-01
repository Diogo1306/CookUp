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
        apiService = ApiRetrofit.getClient().create(ApiService.class);
    }

    public void getUser(String firebaseUid, UserCallback callback) {
        apiService.getUser("user", firebaseUid).enqueue(new Callback<ApiResponse<UserData>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<UserData>> call, @NonNull Response<ApiResponse<UserData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<UserData> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Erro ao buscar usuário (HTTP " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<UserData>> call, @NonNull Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void createOrUpdateUser(UserData userData, UserCallback callback) {
        apiService.createOrUpdateUser("user", userData).enqueue(new Callback<ApiResponse<UserData>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<UserData>> call, @NonNull Response<ApiResponse<UserData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<UserData> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    String errorMessage = "Erro ao criar/atualizar usuário (HTTP " + response.code() + ")";
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<UserData>> call, @NonNull Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }

    public interface UserCallback {
        void onSuccess(UserData user);
        void onError(String message);
    }
}
