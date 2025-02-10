package com.diogo.cookup.viewmodel;

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
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
    private final MutableLiveData<UserData> userDataLiveData = new MutableLiveData<>();

    public AuthViewModel() {
        authRepository = new AuthRepository();
        userRepository = new UserRepository();
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessageLiveData;
    }

    public MutableLiveData<UserData> getUserDataLiveData() {
        return userDataLiveData;
    }

    public void login(String email, String password) {
        authRepository.login(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                if (user != null) {
                    userLiveData.setValue(user);
                    loadUserData(user.getUid());
                }
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue(message);
            }
        });
    }

    public void signup(String email, String password, String username) {
        authRepository.signup(email, password, username, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                if (user != null) {
                    userLiveData.setValue(user);
                    loadUserData(user.getUid());
                }
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue(message);
            }
        });
    }

    private void loadUserData(String firebaseUid) {
        userRepository.getUser(firebaseUid, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserData user) {
                userDataLiveData.setValue(user);
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue("Erro ao carregar os dados do utilizador.");
            }
        });
    }

    public void logout() {
        authRepository.logout();
    }
}
