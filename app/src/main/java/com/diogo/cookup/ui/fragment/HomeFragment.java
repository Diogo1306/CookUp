package com.diogo.cookup.ui.fragment;

import static com.diogo.cookup.utils.MessageUtils.showSnackbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipeViewModel.getRecipesLiveData().observe(getViewLifecycleOwner(), this::updateUI);
        recipeViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            MessageUtils.showSnackbar(view, error, Color.RED);

        });

        recipeViewModel.loadRecipes();
        return view;
    }

    private void updateUI(List<RecipeData> recipes) {
        adapter = new RecipeAdapter(recipes);
        recyclerView.setAdapter(adapter);
    }
}
