package com.diogo.cookup.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diogo.cookup.R;
import com.diogo.cookup.ui.adapter.RecipeAdapter;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.viewmodel.SavedListViewModel;

import org.checkerframework.checker.nullness.qual.NonNull;

public class SavedRecipesFragment extends Fragment {

    private SavedListViewModel viewModel;
    private RecipeAdapter adapter;
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
        listNameText.setText("Lista: " + listName);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_saved_recipes);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new RecipeAdapter(null);
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
