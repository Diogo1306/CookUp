package com.diogo.cookup.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.data.repository.UserRepository;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<UserData> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    public MutableLiveData<UserData> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessageLiveData;
    }

    public void loadUser(String firebaseUid) {
        userRepository.getUser(firebaseUid, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserData user) {
                userLiveData.setValue(user);
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue("Erro ao carregar usuário: " + message);
            }
        });
    }

    public void updateUser(UserData userData) {
        userRepository.createOrUpdateUser(userData, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserData user) {
                userLiveData.setValue(user);
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue("Erro ao atualizar usuário: " + message);
            }
        });
    }
}