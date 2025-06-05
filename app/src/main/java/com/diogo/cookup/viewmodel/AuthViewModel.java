package com.diogo.cookup.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.data.repository.AuthRepository;
import com.diogo.cookup.data.repository.UserRepository;
import com.diogo.cookup.utils.AuthUtils;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> resetEmailSent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> passwordUpdated = new MutableLiveData<>();
    private final MutableLiveData<String> loginProvider = new MutableLiveData<>();
    private final SharedPrefHelper sharedPrefHelper;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository();
        authRepository = new AuthRepository(userRepository);
        sharedPrefHelper = SharedPrefHelper.getInstance(application.getApplicationContext());
    }

    public LiveData<FirebaseUser> getUserLiveData() {return userLiveData;}
    public LiveData<String> getSuccessMessage() {return successMessage;}
    public LiveData<String> getErrorMessage() {return errorMessage;}
    public LiveData<Boolean> getResetEmailSent() {return resetEmailSent;}
    public LiveData<Boolean> getPasswordUpdated() {return passwordUpdated;}
    public LiveData<String> getLoginProvider() {return loginProvider;}

    public void login(String email, String password) {
        authRepository.login(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser firebaseUser) {
                userLiveData.postValue(firebaseUser);

                String uid = firebaseUser.getUid();
                if (uid == null || uid.isEmpty()) {
                    return;
                }

                userRepository.getUser(uid, new UserRepository.UserCallback() {
                    @Override
                    public void onSuccess(UserData user, String message) {
                        sharedPrefHelper.saveUser(user);
                    }

                    @Override
                    public void onError(String msg) {

                        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (fbUser != null) {
                            UserData fallback = new UserData(
                                    fbUser.getUid(),
                                    fbUser.getDisplayName() != null ? fbUser.getDisplayName() : "Utilizador",
                                    fbUser.getEmail(),
                                    fbUser.getPhotoUrl() != null ? fbUser.getPhotoUrl().toString() : ""
                            );
                            sharedPrefHelper.saveUser(fallback);
                        }
                    }
                });
            }

            @Override
            public void onSuccess(FirebaseUser user, String message) {
                successMessage.postValue(message);
            }

            @Override
            public void onError(String msg) {
                errorMessage.postValue(msg);
            }
        });
    }

    public void signup(String email, String password, String username) {
        authRepository.signup(email, password, username, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser firebaseUser) {}

            @Override
            public void onSuccess(FirebaseUser firebaseUser, String message) {
                userLiveData.postValue(firebaseUser);
                successMessage.postValue(message);

                userRepository.getUser(firebaseUser.getUid(), new UserRepository.UserCallback() {
                    @Override
                    public void onSuccess(UserData user, String msg) {
                        sharedPrefHelper.saveUser(user);
                    }

                    @Override
                    public void onError(String msg) {
                        errorMessage.postValue(msg);
                    }
                });
            }

            @Override
            public void onError(String message) {
                errorMessage.postValue(message);
            }
        });
    }

    public void saveGoogleUser(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            String firebaseUid = firebaseUser.getUid();
            String username = firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "Usuário Google";
            String email = firebaseUser.getEmail();
            String profilePicture = firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : "";

            UserData userData = new UserData(firebaseUid, username, email, profilePicture);

            userRepository.createOrUpdateUser(userData, new UserRepository.UserCallback() {
                @Override
                public void onSuccess(UserData user, String message) {
                    sharedPrefHelper.saveUser(user);
                    userLiveData.postValue(firebaseUser);

                    if ("Conta atualizada".equals(message)) {
                        successMessage.postValue("Login bem-sucedido!");
                    } else {
                        successMessage.postValue(message);
                    }
                }

                @Override
                public void onError(String message) {
                    errorMessage.postValue(message);
                }
            });
        }
    }

    public void sendPasswordResetEmail(String email) {
        authRepository.sendPasswordResetEmail(email, task -> resetEmailSent.postValue(task.isSuccessful()));
    }

    public void updatePassword(String newPassword) {
        authRepository.updatePassword(newPassword, task -> passwordUpdated.postValue(task.isSuccessful()));
    }

    public void checkLoginProvider() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String provider = AuthUtils.getProvider(user);
        loginProvider.setValue(provider);
    }

    public void logout() {
        authRepository.logout();
        sharedPrefHelper.clearUser();
        userLiveData.postValue(null);
    }
}