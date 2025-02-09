package com.diogo.cookup.data.repository;

import androidx.annotation.NonNull;

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

    public void getUser(String firebase_uid, UserCallback callback) {
        apiService.getUser(firebase_uid).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess((UserData) response.body());
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
        void onError (String message);
    }

}
