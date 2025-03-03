package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.data.repository.AuthRepository;
import com.diogo.cookup.data.repository.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public AuthViewModel() {
        userRepository = new UserRepository();
        authRepository = new AuthRepository(userRepository);
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void login(String email, String password) {
        authRepository.login(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                userLiveData.postValue(user);
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
            public void onSuccess(FirebaseUser user) {
            }

            @Override
            public void onSuccess(FirebaseUser user, String message) {
                userLiveData.postValue(user);
                successMessage.postValue(message);
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
                    userLiveData.postValue(firebaseUser);
                    successMessage.postValue(message);
                }

                @Override
                public void onError(String message) {
                    errorMessage.postValue(message);
                }
            });
        }
    }

    public void logout() {
        authRepository.logout();
        userLiveData.postValue(null);
    }
}
