package com.diogo.cookup.data.repository;

import com.diogo.cookup.data.model.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.diogo.cookup.data.repository.UserRepository;

public class AuthRepository {
    private final FirebaseAuth auth;
    private final UserRepository userRepository;

    public AuthRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.userRepository = new UserRepository();
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
                            UserData userData = new UserData(user.getUid(), username, email, "");
                            userRepository.createOrUpdateUser(userData, new UserRepository.UserCallback() {
                                @Override
                                public void onSuccess(UserData user) {
                                    callback.onSuccess(auth.getCurrentUser());
                                }

                                @Override
                                public void onError(String message) {
                                    deleteFirebaseUser(user, callback);
                                    callback.onError("Erro ao salvar usuário na API: " + message);
                                }
                            });
                        }
                    } else {
                        callback.onError("Erro ao criar conta: " + task.getException().getMessage());
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
