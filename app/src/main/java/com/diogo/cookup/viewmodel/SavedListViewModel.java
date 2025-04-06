package com.diogo.cookup.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.SavedListData;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.model.ApiResponse;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.data.repository.SavedListRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedListViewModel extends ViewModel {

    private final SavedListRepository repository = new SavedListRepository();

    private final MutableLiveData<List<SavedListData>> savedLists = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<List<RecipeData>> recipesFromList = new MutableLiveData<>();
    private final MutableLiveData<UserData> userLiveData = new MutableLiveData<>();

    public LiveData<List<SavedListData>> getSavedLists() {
        return savedLists;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public LiveData<List<RecipeData>> getRecipesFromList() {
        return recipesFromList;
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
                } else {
                    message.postValue("Erro ao carregar listas");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<SavedListData>>> call, Throwable t) {
                message.postValue("Erro de conexão: " + t.getMessage());
            }
        });
    }


    public void addRecipeToList(int listId, int recipeId) {
        repository.addRecipeToList(listId, recipeId, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    message.postValue(response.body().getMessage());
                } else {
                    message.postValue("Erro ao guardar receita");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                message.postValue("Erro de conexão ao guardar: " + t.getMessage());
            }
        });
    }

    public void createList(int userId, String name, String color) {
        repository.createList(userId, name, color, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    message.postValue("Lista criada com sucesso");
                    loadLists(userId);
                } else {
                    message.postValue("Erro ao criar lista");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                message.postValue("Erro de conexão ao criar lista: " + t.getMessage());
            }
        });
    }

    public void loadRecipesFromList(int listId) {
        repository.getRecipesFromList(listId, new Callback<ApiResponse<List<RecipeData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<RecipeData>>> call, Response<ApiResponse<List<RecipeData>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipesFromList.postValue(response.body().getData());
                } else {
                    message.postValue("Erro ao carregar receitas da lista");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<RecipeData>>> call, Throwable t) {
                message.postValue("Erro de conexão ao carregar receitas: " + t.getMessage());
            }
        });
    }
}
