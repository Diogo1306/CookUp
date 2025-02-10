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
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        callback.onSuccess(userData);
                    } else {
                        callback.onError(response.body().getMessage());
                    }
                } else {
                    callback.onError("Erro ao salvar usuário!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                callback.onError("Erro de conexão!");
            }
        });
    }

    public void getUser(String firebase_uid, UserCallback callback) {
        apiService.getUser(firebase_uid).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Usuário não encontrado!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable t) {
                callback.onError("Erro de conexão!");
            }
        });
    }

    public interface UserCallback {
        void onSuccess(UserData user);
        void onError(String message);
    }
}