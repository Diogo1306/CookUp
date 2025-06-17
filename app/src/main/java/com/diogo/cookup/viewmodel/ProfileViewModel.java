package com.diogo.cookup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.ProfileData;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.repository.ProfileRepository;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends ViewModel {

    private final ProfileRepository repository = new ProfileRepository();
    private final MutableLiveData<ProfileData> profileSummary = new MutableLiveData<>();
    private final MutableLiveData<List<RecipeData>> userRecipes = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    private final MutableLiveData<Boolean> deleteSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> deleteError = new MutableLiveData<>();

    public LiveData<ProfileData> getProfileSummary() { return profileSummary; }
    public LiveData<List<RecipeData>> getUserRecipes() { return userRecipes; }
    public LiveData<String> getError() { return error; }

    public LiveData<Boolean> getDeleteSuccess() { return deleteSuccess; }
    public LiveData<String> getDeleteError() { return deleteError; }

    public void loadProfileSummary(int userId) {
        repository.getProfileSummary(userId, profileSummary, error);
    }

    public void loadProfileRecipes(int userId) {
        repository.getProfileRecipes(userId, userRecipes, error);
    }

    public void deleteRecipe(int recipeId) {
        repository.deleteRecipe(recipeId, new ProfileRepository.DeleteCallback() {
            @Override
            public void onSuccess() {
                deleteSuccess.postValue(true);
                if (userRecipes.getValue() != null) {
                    List<RecipeData> updatedList = new ArrayList<>(userRecipes.getValue());
                    for (int i = 0; i < updatedList.size(); i++) {
                        if (updatedList.get(i).getRecipeId() == recipeId) {
                            updatedList.remove(i);
                            break;
                        }
                    }
                    userRecipes.postValue(updatedList);
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                deleteError.postValue(errorMsg);
            }
        });
    }

}
