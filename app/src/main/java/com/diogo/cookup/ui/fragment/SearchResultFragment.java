package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.RecipeData;
import com.diogo.cookup.ui.adapter.RecipeAdapterDefault;
import com.diogo.cookup.viewmodel.ExploreViewModel;

import java.util.ArrayList;

public class SearchResultFragment extends Fragment {

    private ExploreViewModel exploreViewModel;
    private RecipeAdapterDefault adapter;
    private RecyclerView recyclerView;

    private String query;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        recyclerView = view.findViewById(R.id.recycler_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RecipeAdapterDefault(
                new ArrayList<>(),
                new ArrayList<>(),
                this::openRecipeDetail,
                recipeId -> {
                }
        );

        recyclerView.setAdapter(adapter);

        exploreViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);

        if (getArguments() != null) {
            query = getArguments().getString("query", "");
            if (!query.isEmpty()) {
                performSearch(query);
            }
        }

        return view;
    }

    private void performSearch(String query) {
        exploreViewModel.searchAll(query).observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getData() != null && response.getData().getRecipes() != null) {
                adapter.setData(response.getData().getRecipes());
                adapter.setSkeletonMode(false);
            } else {
                adapter.setData(new ArrayList<>());
                Toast.makeText(getContext(), "Nenhuma receita encontrada.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openRecipeDetail(RecipeData recipe) {
        Bundle bundle = new Bundle();
        bundle.putInt("recipe_id", recipe.getRecipeId());

        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
