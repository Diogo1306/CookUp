package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.ui.adapter.SavedListAdapterSelect;
import com.diogo.cookup.ui.dialog.CreateListDialog;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.SavedListViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class SaveRecipeBottomSheet extends BottomSheetDialogFragment {

    private int recipeId;
    private RecyclerView recyclerView;
    private SavedListViewModel viewModel;
    private int userId;

    public static SaveRecipeBottomSheet newInstance(int recipeId) {
        SaveRecipeBottomSheet sheet = new SaveRecipeBottomSheet();
        Bundle args = new Bundle();
        args.putInt("recipe_id", recipeId);
        sheet.setArguments(args);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_save_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipeId = getArguments().getInt("recipe_id");
        recyclerView = view.findViewById(R.id.recycler_lists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel = new ViewModelProvider(requireActivity()).get(SavedListViewModel.class);

        UserData currentUser = SharedPrefHelper.getInstance(requireContext()).getUser();
        if (currentUser == null || currentUser.getUserId() == 0) {
            Toast.makeText(requireContext(), "Erro: utilizador não encontrado", Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        userId = currentUser.getUserId();

        SavedListAdapterSelect adapter = new SavedListAdapterSelect(
                recipeId,
                new ArrayList<>(),
                (listId, recipeId1) -> {
                    viewModel.addRecipeToList(listId, recipeId1);

                    viewModel.loadUserSavedRecipeIds(userId);

                    Toast toast = Toast.makeText(requireContext(), "Receita adicionada com sucesso", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
                    toast.show();
                    dismiss();
                },
                (listId, recipeId1) -> {
                    viewModel.removeRecipeFromList(listId, recipeId1);

                    viewModel.loadUserSavedRecipeIds(userId);

                    Toast toast = Toast.makeText(requireContext(), "Receita removida da lista", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
                    toast.show();
                }
        );

        recyclerView.setAdapter(adapter);

        viewModel.getRecipeListIds().observe(getViewLifecycleOwner(), adapter::updateRecipeListIds);
        viewModel.getSavedLists().observe(getViewLifecycleOwner(), adapter::submitList);

        viewModel.loadRecipeListIds(recipeId);
        viewModel.loadLists(userId);

        view.findViewById(R.id.button_create_list).setOnClickListener(v -> {
            CreateListDialog.show(requireContext(), (name, color) -> {
                viewModel.createList(userId, name, color);

                viewModel.loadLists(userId);
            });
        });
    }
}
