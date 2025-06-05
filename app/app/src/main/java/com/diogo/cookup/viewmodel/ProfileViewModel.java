package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.ProfileData;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.repository.ProfileRepository;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    private final ProfileRepository repository = new ProfileRepository();
    private final MutableLiveData<ProfileData> profileSummary = new MutableLiveData<>();
    private final MutableLiveData<List<RecipeData>> userRecipes = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<ProfileData> getProfileSummary() {
        return profileSummary;
    }

    public LiveData<List<RecipeData>> getUserRecipes() {
        return userRecipes;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadProfileSummary(int userId) {
        repository.getProfileSummary(userId, profileSummary, error);
    }

    public void loadProfileRecipes(int userId) {
        repository.getProfileRecipes(userId, userRecipes, error);
    }
}
