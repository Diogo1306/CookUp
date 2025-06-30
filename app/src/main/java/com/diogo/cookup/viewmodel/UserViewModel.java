package com.diogo.cookup.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.data.repository.UserRepository;
import com.diogo.cookup.utils.SharedPrefHelper;

import java.io.File;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final SharedPrefHelper sharedPrefHelper;

    private final MutableLiveData<UserData> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<UserData>> userApiResponseLiveData = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository();
        sharedPrefHelper = SharedPrefHelper.getInstance(application.getApplicationContext());
    }

    public LiveData<UserData> getUserLiveData() {return userLiveData;}
    public LiveData<String> getSuccessMessageLiveData() {return successMessage;}
    public LiveData<String> getErrorMessageLiveData() {return errorMessage;}
    public LiveData<ApiResponse<UserData>> getApiResponseLiveData() {return userApiResponseLiveData;}

    public void loadUser(String firebaseUid) {
        userRepository.getUser(firebaseUid, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserData user, String message) {
                userLiveData.postValue(user);
                sharedPrefHelper.saveUser(user);
                successMessage.postValue(message);
            }

            @Override
            public void onError(String message) {
                errorMessage.postValue("Erro ao carregar utilizador: " + message);
            }
        });
    }

    public void updateUser(int userId, String username, File imageFileOrNull) {
        userRepository.updateProfile(userId, username, imageFileOrNull, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserData user, String message) {
                userLiveData.postValue(user);
                sharedPrefHelper.saveUser(user);
                successMessage.postValue(message);
            }

            @Override
            public void onError(String message) {
                errorMessage.postValue(message);
            }
        });
    }

    public void updateUser(UserData userData) {
        userRepository.createOrUpdateUser(userData, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserData user, String message) {
                userLiveData.postValue(user);
                sharedPrefHelper.saveUser(user);
                successMessage.postValue(message);
            }

            @Override
            public void onError(String message) {
                errorMessage.postValue("Erro ao atualizar utilizador: " + message);
            }
        });
    }

    public void updateUserWithImageFile(int userId, String username, File imageFile) {
        userRepository.updateProfile(userId, username, imageFile, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserData user, String message) {
                userLiveData.postValue(user);
                if (user != null) {
                    sharedPrefHelper.saveUser(user);
                }
                successMessage.postValue(message);
            }

            @Override
            public void onError(String message) {
                errorMessage.postValue(message);
            }
        });
    }

    public void deleteUser(int userId, UserRepository.UserCallback callback) {
        userRepository.deleteUser(userId, callback);
    }

}