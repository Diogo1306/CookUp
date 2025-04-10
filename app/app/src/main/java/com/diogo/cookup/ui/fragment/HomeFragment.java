package com.diogo.cookup.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.ui.adapter.RecipeAdapterDefault;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.RecipeViewModel;
import com.diogo.cookup.viewmodel.SavedListViewModel;
import com.diogo.cookup.data.model.UserData;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecipeViewModel recipeViewModel;
    private SavedListViewModel savedListViewModel;
    private RecipeAdapterDefault adapter;

    private List<RecipeData> allRecipes = null;
    private List<Integer> savedRecipeIds = null;

    private RecyclerView recyclerView;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        setupViewModels(view);

        return view;
    }

    private void setupViewModels(View view) {
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        savedListViewModel = new ViewModelProvider(requireActivity()).get(SavedListViewModel.class);

        recipeViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error ->
                MessageUtils.showSnackbar(view, error, Color.RED)
        );

        recipeViewModel.getRecipesLiveData().observe(getViewLifecycleOwner(), recipes -> {
            allRecipes = recipes;
            tryUpdateAdapter();
        });

        savedListViewModel.getSavedRecipeIds().observe(getViewLifecycleOwner(), ids -> {
            savedRecipeIds = ids;
            tryUpdateAdapter();
        });

        recipeViewModel.loadRecipes();

        UserData user = SharedPrefHelper.getInstance(requireContext()).getUser();
        if (user != null) {
            savedListViewModel.loadUserSavedRecipeIds(user.getUserId());
        }
    }

    private void tryUpdateAdapter() {
        if (allRecipes != null && savedRecipeIds != null) {
            if (adapter == null) {
                adapter = new RecipeAdapterDefault(
                        new ArrayList<>(allRecipes),
                        new ArrayList<>(savedRecipeIds),
                        recipe -> {
                            // clique na receita (opcional)
                        },
                        recipeId -> {
                            SaveRecipeBottomSheet bottomSheet = SaveRecipeBottomSheet.newInstance(recipeId);
                            bottomSheet.show(getParentFragmentManager(), "SaveRecipeBottomSheet");
                        }
                );
                recyclerView.setAdapter(adapter);
            } else {
                adapter.updateData(allRecipes, savedRecipeIds);
            }
        }
    }
}
