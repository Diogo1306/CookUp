package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.ui.adapter.RecipeAdapterSavedInList;
import com.diogo.cookup.viewmodel.SavedListViewModel;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class SavedRecipesFragment extends Fragment {

    private SavedListViewModel viewModel;
    private RecipeAdapterSavedInList adapter;
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

        view.findViewById(R.id.arrow_back).setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        RecyclerView recyclerView = view.findViewById(R.id.recycler_saved_recipes);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new RecipeAdapterSavedInList(
                recipe -> {
                    viewModel.removeRecipeFromList(listId, recipe.getRecipeId());
                    adapter.removeRecipe(recipe);
                    Toast.makeText(requireContext(), getString(R.string.recipe_removed_from_list), Toast.LENGTH_SHORT).show();
                },
                recipe -> {
                    NavHostFragment.findNavController(this)
                            .navigate(SavedRecipesFragmentDirections
                                    .actionSavedRecipesFragmentToRecipeDetailFragment(recipe.getRecipeId()));
                }
        );

        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(SavedListViewModel.class);

        viewModel.getRecipesFromList().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null) {
                adapter.submitList(new ArrayList<>(recipes));
            }
        });

        viewModel.loadRecipesFromList(listId);
    }
}
