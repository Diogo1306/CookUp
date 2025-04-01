package com.diogo.cookup.data.repository;

import android.media.MediaPlayer;

import com.diogo.cookup.data.model.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private final FirebaseAuth auth;
    private final UserRepository userRepository;

    public AuthRepository(UserRepository userRepository) {
        this.auth = FirebaseAuth.getInstance();
        this.userRepository = userRepository;
    }

    public void login(String email, String password, AuthCallback callback) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            callback.onError("Email e senha não podem estar vazios.");
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = auth.getCurrentUser();
                        if (currentUser != null) {
                            callback.onSuccess(currentUser);
                        } else {
                            callback.onError("Erro ao obter dados do utilizador após login.");
                        }
                    } else {
                        String errorMsg = (task.getException() != null)
                                ? task.getException().getMessage()
                                : "Erro desconhecido";
                        callback.onError("Erro ao fazer login: " + errorMsg);
                    }
                });
    }

    public void signup(String email, String password, String username, AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            UserData userData = new UserData(
                                    firebaseUser.getUid(),
                                    username,
                                    email,
                                    ""
                            );

                            userRepository.createOrUpdateUser(userData, new UserRepository.UserCallback() {
                                @Override
                                public void onSuccess(UserData user, String message) {
                                    callback.onSuccess(firebaseUser, message);
                                }

                                @Override
                                public void onError(String message) {
                                    deleteFirebaseUser(firebaseUser, callback, message);
                                }
                            });
                        } else {
                            callback.onError("Erro inesperado: Usuário Firebase é nulo após signup!");
                        }
                    } else {
                        String errorMsg;
                        if (task.getException() != null && task.getException().getMessage().contains("email address is already in use")) {
                            errorMsg = "O e-mail já está em uso.";
                        } else {
                            errorMsg = (task.getException() != null)
                                    ? task.getException().getMessage()
                                    : "Erro desconhecido ao criar conta.";
                        }
                        callback.onError(errorMsg);
                    }
                });
    }

    private void deleteFirebaseUser(FirebaseUser firebaseUser, AuthCallback callback, String originalErrorMessage) {
        firebaseUser.delete()
                .addOnCompleteListener(deleteTask -> {
                    if (deleteTask.isSuccessful()) {
                        callback.onError(originalErrorMessage);
                    } else {
                        String errorMsg = (deleteTask.getException() != null)
                                ? deleteTask.getException().getMessage()
                                : "Erro desconhecido ao excluir conta do Firebase.";
                        callback.onError(originalErrorMessage + " | Firebase Delete Error: " + errorMsg);
                    }
                });
    }

    public void sendPasswordResetEmail(String email, OnCompleteListener<Void> listener) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(listener);
    }

    public void updatePassword(String newPassword, OnCompleteListener<Void> listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword).addOnCompleteListener(listener);
        }
    }

    public void logout() {
        auth.signOut();
    }

    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onSuccess(FirebaseUser user, String message);
        void onError(String message);
    }
}