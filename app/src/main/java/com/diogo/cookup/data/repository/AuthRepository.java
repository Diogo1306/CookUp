package com.diogo.cookup.data.repository;

import com.diogo.cookup.data.model.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.diogo.cookup.network.ApiRetrofit;
import com.diogo.cookup.network.ApiService;
import com.diogo.cookup.data.model.ApiResponse;

public class AuthRepository {
    private final FirebaseAuth auth;
    private final ApiService apiService;

    public AuthRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.apiService = ApiRetrofit.getClient().create(ApiService.class);
    }

    public void login(String email, String password, AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(auth.getCurrentUser());
                    } else {
                        callback.onError("Erro ao fazer login: " + task.getException().getMessage());
                    }
                });
    }

    public void signup(String email, String password, String username, AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            sendUserToDatabase(user, username, callback);
                        }
                    } else {
                        callback.onError("Erro ao criar conta: " + task.getException().getMessage());
                    }
                });
    }

    private void sendUserToDatabase(FirebaseUser firebaseUser, String username, AuthCallback callback) {
        String firebase_uid = firebaseUser.getUid();
        String email = firebaseUser.getEmail();
        String profile_picture = "";

        UserData userData = new UserData(firebase_uid, username , email, profile_picture);

        Call<ApiResponse> call = apiService.createOrUpdateUser(userData);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(firebaseUser);
                } else {
                    deleteFirebaseUser(firebaseUser, callback);
                    callback.onError("Erro ao salvar usuário no banco de dados.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                deleteFirebaseUser(firebaseUser, callback);
                callback.onError("Erro na conexão com o servidor: " + throwable.getMessage());
            }
        });
    }

    private void deleteFirebaseUser(FirebaseUser firebaseUser, AuthCallback callback) {
        firebaseUser.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        callback.onError("Erro ao excluir conta: " + task.getException().getMessage());
                    }
                });
    }

    public void logout() {
        auth.signOut();
    }

    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onError(String message);
    }
}
