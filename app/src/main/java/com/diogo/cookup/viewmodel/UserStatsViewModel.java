package com.diogo.cookup.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.data.repository.FallbackRepository;
import com.diogo.cookup.data.repository.UserStatsRepository;

import java.util.List;

public class UserStatsViewModel extends ViewModel {

    private final UserStatsRepository userStatsRepository;
    private final FallbackRepository fallbackRepository;

    private final MutableLiveData<List<RecipeData>> recommendedLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public UserStatsViewModel() {
        userStatsRepository = new UserStatsRepository();
        fallbackRepository = new FallbackRepository();
    }

    public LiveData<List<RecipeData>> getRecommendedLiveData() {
        return recommendedLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void loadUserRecommendations(int userId) {
        userStatsRepository.loadUserRecommendedRecipes(userId, new MutableLiveData<List<RecipeData>>() {
            @Override
            public void postValue(List<RecipeData> list) {
                if (list != null && !list.isEmpty()) {
                    recommendedLiveData.postValue(list);
                    Log.d("USER_STATS", "Recomendações personalizadas carregadas: " + list.size());
                } else {
                    Log.d("USER_STATS", "Sem recomendações. Buscando fallback.");
                    loadFallbackRecommendations();
                }
            }
        }, errorLiveData);
    }

    private void loadFallbackRecommendations() {
        fallbackRepository.getFallbackRecipes(recommendedLiveData, errorLiveData);
    }
}
