package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.data.repository.UserRepository;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;

    private final MutableLiveData<UserData> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<UserData>> userApiResponseLiveData = new MutableLiveData<>();

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    public LiveData<UserData> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getSuccessMessageLiveData() {
        return successMessage;
    }

    public LiveData<String> getErrorMessageLiveData() {
        return errorMessage;
    }

    public LiveData<ApiResponse<UserData>> getApiResponseLiveData() {
        return userApiResponseLiveData;
    }

    public void loadUser(String firebaseUid) {
        userRepository.getUser(firebaseUid, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserData user, String message) {
                userLiveData.postValue(user);
                successMessage.postValue(message);
            }

            @Override
            public void onError(String message) {
                errorMessage.postValue("Erro ao carregar usuário: " + message);
            }
        });
    }

    public void updateUser(UserData userData) {
        userRepository.createOrUpdateUser(userData, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserData user, String message) {
                userLiveData.postValue(user);
                successMessage.postValue(message);
            }

            @Override
            public void onError(String message) {
                errorMessage.postValue("Erro ao atualizar usuário: " + message);
            }
        });
    }
}
