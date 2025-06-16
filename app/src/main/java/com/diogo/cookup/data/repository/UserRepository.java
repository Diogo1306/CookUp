package com.diogo.cookup.data.repository;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;
import com.diogo.cookup.utils.FileUtils;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public void updateProfile(int userId, String username, File imageFileOrNull, UserCallback callback) {
        RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId));
        RequestBody usernameBody = RequestBody.create(MediaType.parse("text/plain"), username);

        MultipartBody.Part imagePart = null;
        if (imageFileOrNull != null && imageFileOrNull.exists()) {
            RequestBody fileBody = RequestBody.create(imageFileOrNull, MediaType.parse("image/*"));
            imagePart = MultipartBody.Part.createFormData("profile_picture", imageFileOrNull.getName(), fileBody);
        }

        apiService.updateProfile(userIdBody, usernameBody, imagePart).enqueue(new Callback<ApiResponse<UserData>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserData>> call, Response<ApiResponse<UserData>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData(), response.body().getMessage());
                } else {
                    callback.onError("Erro ao atualizar perfil.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserData>> call, Throwable t) {
                callback.onError("Erro na conexão: " + t.getMessage());
            }
        });
    }

    public void deleteUser(int userId, UserCallback callback) {
        apiService.deleteUser("delete_user", userId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(null, response.body().getMessage());
                } else {
                    callback.onError("Erro ao deletar usuário.");
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                callback.onError("Erro de conexão: " + t.getMessage());
            }
        });
    }


    public interface UserCallback {
        void onSuccess(UserData user, String message);
        void onError(String message);
    }

}
