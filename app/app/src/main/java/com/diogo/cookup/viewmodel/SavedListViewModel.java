package com.diogo.cookup.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.model.SavedListData;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.data.repository.SavedListRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedListViewModel extends AndroidViewModel {

    private final SavedListRepository repository = new SavedListRepository();

    private final MutableLiveData<List<SavedListData>> savedLists = new MutableLiveData<>();
    private final MutableLiveData<List<RecipeData>> recipesFromList = new MutableLiveData<>();
    private final MutableLiveData<UserData> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Integer>> recipeListIds = new MutableLiveData<>();
    private final MutableLiveData<List<Integer>> savedRecipeIds = new MutableLiveData<>();
    private final MutableLiveData<Integer> changedRecipeId = new MutableLiveData<>();

    public SavedListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<SavedListData>> getSavedLists() { return savedLists; }
    public LiveData<List<RecipeData>> getRecipesFromList() { return recipesFromList; }
    public LiveData<UserData> getUserLiveData() { return userLiveData; }
    public LiveData<List<Integer>> getRecipeListIds() { return recipeListIds; }
    public LiveData<List<Integer>> getSavedRecipeIds() { return savedRecipeIds; }
    public LiveData<Integer> getChangedRecipeId() { return changedRecipeId; }

    public void setUser(UserData user) {
        userLiveData.setValue(user);
        if (user != null) reloadAll(user.getUserId());
    }

    public void notifyRecipeChanged(int recipeId) {
        changedRecipeId.postValue(recipeId);
    }

    public void loadLists(int userId) {
        repository.getUserLists(userId, new SimpleCallback<>(data -> savedLists.postValue(data)));
    }

    public void createList(int userId, String name, String color) {
        repository.createList(userId, name, color, new SimpleCallback<>(ignored -> reloadAll(userId)));
    }

    public void updateList(int listId, String name, String color) {
        repository.updateList(listId, name, color, new SimpleCallback<>(ignored -> reloadAllIfUserExists()));
    }

    public void deleteList(int listId) {
        repository.deleteList(listId, new SimpleCallback<>(ignored -> reloadAllIfUserExists()));
    }

    public void loadRecipesFromList(int listId) {
        repository.getRecipesFromList(listId, new SimpleCallback<>(recipesFromList::postValue));
    }

    public void addRecipeToList(int listId, int recipeId) {
        repository.addRecipeToList(listId, recipeId, new SimpleCallback<>(ignored -> reloadAllIfUserExists()));
    }

    public void removeRecipeFromList(int listId, int recipeId) {
        repository.removeRecipeFromList(listId, recipeId, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                reloadAllIfUserExists();
                loadRecipesFromList(listId);
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {}
        });
    }

    public void loadRecipeListIds(int recipeId) {
        repository.getRecipeListIds(recipeId, new Callback<ApiResponse<List<Integer>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Integer>>> call, Response<ApiResponse<List<Integer>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    recipeListIds.postValue(response.body().getData());
                } else {
                    recipeListIds.postValue(new ArrayList<>());
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Integer>>> call, Throwable t) {
                recipeListIds.postValue(new ArrayList<>());
            }
        });
    }

    public void loadUserSavedRecipeIds(int userId) {
        repository.getUserSavedRecipeIds(userId, new SimpleCallback<>(savedRecipeIds::setValue));
    }

    public void loadListsWithRecipes(int userId) {
        repository.getListsWithRecipes(userId, new SimpleCallback<>(data -> {
            if (data != null) savedLists.postValue(data);
            else savedLists.postValue(new ArrayList<>());
        }));
    }

    public void reloadAllIfUserExists() {
        if (userLiveData.getValue() != null) {
            reloadAll(userLiveData.getValue().getUserId());
        }
    }

    public void reloadAll(int userId) {
        loadUserSavedRecipeIds(userId);
        loadListsWithRecipes(userId);
    }

    public static class SimpleCallback<T> implements Callback<ApiResponse<T>> {
        private final java.util.function.Consumer<T> onSuccess;

        SimpleCallback(java.util.function.Consumer<T> onSuccess) {
            this.onSuccess = onSuccess;
        }

        @Override
        public void onResponse(Call<ApiResponse<T>> call, Response<ApiResponse<T>> response) {
            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                onSuccess.accept(response.body().getData());
            }
        }

        @Override
        public void onFailure(Call<ApiResponse<T>> call, Throwable t) {
        }
    }
}