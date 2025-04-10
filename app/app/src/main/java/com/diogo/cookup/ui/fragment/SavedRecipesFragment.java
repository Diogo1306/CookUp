package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.ui.adapter.RecipeAdapterSaved;
import com.diogo.cookup.viewmodel.SavedListViewModel;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class SavedRecipesFragment extends Fragment {

    private SavedListViewModel viewModel;
    private RecipeAdapterSaved adapter;
    private int listId;
    private String listName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listId = getArguments().getInt("list_id");
        listName = getArguments().getString("list_name");

        TextView listNameText = view.findViewById(R.id.text_list_name);
        listNameText.setText(listName);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_saved_recipes);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new RecipeAdapterSaved(new ArrayList<>(), recipe -> {
            viewModel.removeRecipeFromList(listId, recipe.getRecipeId());
        });

        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(SavedListViewModel.class);

        viewModel.getRecipesFromList().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null) {
                adapter.updateData(recipes);
            }
        });

        viewModel.loadRecipesFromList(listId);
    }
}
