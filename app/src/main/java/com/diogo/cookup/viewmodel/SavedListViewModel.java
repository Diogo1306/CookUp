package com.diogo.cookup.viewmodel;

import android.app.Application;

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
    private final MutableLiveData<List<Integer>> savedRecipeIds = new MutableLiveData<>(new ArrayList<>());

    public SavedListViewModel(Application application) {
        super(application);
    }

    public LiveData<List<SavedListData>> getSavedLists() {
        return savedLists;
    }

    public LiveData<List<RecipeData>> getRecipesFromList() {
        return recipesFromList;
    }

    public LiveData<List<Integer>> getRecipeListIds() {
        return recipeListIds;
    }

    public LiveData<List<Integer>> getSavedRecipeIds() {
        return savedRecipeIds;
    }

    public LiveData<UserData> getUserLiveData() {
        return userLiveData;
    }

    public void setUser(UserData user) {
        userLiveData.setValue(user);
    }

    public void loadLists(int userId) {
        repository.getUserLists(userId, new Callback<ApiResponse<List<SavedListData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<SavedListData>>> call, Response<ApiResponse<List<SavedListData>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    savedLists.postValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<SavedListData>>> call, Throwable t) {
            }
        });
    }

    public void createList(int userId, String name, String color) {
        repository.createList(userId, name, color, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    loadLists(userId);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
            }
        });
    }

    public void updateList(int listId, String name, String color) {
        repository.updateList(listId, name, color, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    if (userLiveData.getValue() != null) {
                        loadLists(userLiveData.getValue().getUserId());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
            }
        });
    }

    public void deleteList(int listId) {
        repository.deleteList(listId, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    if (userLiveData.getValue() != null) {
                        loadLists(userLiveData.getValue().getUserId());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
            }
        });
    }

    public void loadRecipesFromList(int listId) {
        repository.getRecipesFromList(listId, new Callback<ApiResponse<List<RecipeData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<RecipeData>>> call, Response<ApiResponse<List<RecipeData>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipesFromList.postValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<RecipeData>>> call, Throwable t) {
            }
        });
    }

    public void addRecipeToList(int listId, int recipeId) {
        repository.addRecipeToList(listId, recipeId, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                reloadSavedRecipeData();
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
            }
        });
    }

    public void removeRecipeFromList(int listId, int recipeId) {
        repository.removeRecipeFromList(listId, recipeId, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                loadRecipesFromList(listId);
                reloadSavedRecipeData();
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
            }
        });
    }

    public void loadRecipeListIds(int recipeId) {
        repository.getRecipeListIds(recipeId, new Callback<ApiResponse<List<Integer>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Integer>>> call, Response<ApiResponse<List<Integer>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    recipeListIds.postValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Integer>>> call, Throwable t) {
                recipeListIds.postValue(new ArrayList<>());
            }
        });
    }

    public void loadUserSavedRecipeIds(int userId) {
        repository.getUserSavedRecipeIds(userId, new Callback<ApiResponse<List<Integer>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Integer>>> call, Response<ApiResponse<List<Integer>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    savedRecipeIds.postValue(response.body().getData());
                } else {
                    savedRecipeIds.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Integer>>> call, Throwable t) {
                savedRecipeIds.postValue(new ArrayList<>());
            }
        });
    }

    private void reloadSavedRecipeData() {
        if (userLiveData.getValue() != null) {
            loadUserSavedRecipeIds(userLiveData.getValue().getUserId());
        }
    }
}
