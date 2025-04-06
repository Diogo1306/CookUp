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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.ui.adapter.RecipeAdapter;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.viewmodel.RecipeViewModel;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecipeViewModel recipeViewModel;
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setupRecyclerView(view);
        setupViewModel(view);

        return view;
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new RecipeAdapter(null);

        adapter.setOnSaveClickListener(recipeId -> {
            SaveRecipeBottomSheet bottomSheet = SaveRecipeBottomSheet.newInstance(recipeId);
            bottomSheet.show(getParentFragmentManager(), "SaveRecipeBottomSheet");
        });

        recyclerView.setAdapter(adapter);
    }


    private void setupViewModel(View view) {
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        recipeViewModel.getRecipesLiveData().observe(getViewLifecycleOwner(), this::updateUI);
        recipeViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error ->
                MessageUtils.showSnackbar(view, error, Color.RED)
        );

        recipeViewModel.loadRecipes();
    }

    private void updateUI(List<RecipeData> recipes) {
        if (recipes != null && !recipes.isEmpty()) {
            adapter.updateData(recipes);
        } else {
            MessageUtils.showSnackbar(requireView(), "Nenhuma receita encontrada.", Color.YELLOW);
        }
    }
}