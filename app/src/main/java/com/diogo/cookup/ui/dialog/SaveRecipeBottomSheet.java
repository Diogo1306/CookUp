package com.diogo.cookup.ui.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.TrackRequest;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.data.repository.TrackingRepository;
import com.diogo.cookup.ui.adapter.SavedListAdapterSelect;
import com.diogo.cookup.utils.UserManager;
import com.diogo.cookup.viewmodel.SavedListViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class SaveRecipeBottomSheet extends BottomSheetDialogFragment {

    private int recipeId;
    private RecyclerView recyclerView;
    private SavedListViewModel viewModel;
    private SavedListAdapterSelect adapter;
    private Fragment parentFragment;


    public static SaveRecipeBottomSheet newInstance(int recipeId, Fragment parent) {
        SaveRecipeBottomSheet sheet = new SaveRecipeBottomSheet();
        sheet.parentFragment = parent;
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
        Log.d("SAVE_SHEET", "BottomSheet criado. Recipe ID: " + recipeId);

        recyclerView = view.findViewById(R.id.recycler_lists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(requireActivity()).get(SavedListViewModel.class);

        UserData user = viewModel.getUserLiveData().getValue();
        if (user == null || user.getUserId() == 0) {
            user = UserManager.getCurrentUser(requireContext());
            if (user != null) {
                viewModel.setUser(user);
            } else {
                Toast.makeText(requireContext(), "Erro: utilizador não encontrado", Toast.LENGTH_SHORT).show();
                dismiss();
                return;
            }
        }

        int userId = user.getUserId();

        adapter = new SavedListAdapterSelect(
                recipeId,
                new ArrayList<>(),
                (listId, recipeId1) -> {
                    viewModel.addRecipeToList(listId, recipeId1);
                    TrackingRepository trackingRepository = new TrackingRepository();
                    trackingRepository.sendTracking(
                            new TrackRequest(userId, recipeId, "favorite"),
                            new MutableLiveData<>()
                    );

                    new Handler().postDelayed(() -> {
                        viewModel.reloadAll(userId);
                        viewModel.notifyRecipeChanged(recipeId1);
                    }, 500);

                    Toast toast = Toast.makeText(requireContext(), "Receita adicionada com sucesso", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
                    toast.show();
                    dismiss();
                },
                (listId, recipeId1) -> {
                    viewModel.removeRecipeFromList(listId, recipeId1);
                    adapter.removeRecipeFromVisual(listId);
                    viewModel.notifyRecipeChanged(recipeId1);

                    Toast toast = Toast.makeText(requireContext(), "Receita removida da lista", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
                    toast.show();
                }
        );

        recyclerView.setAdapter(adapter);

        viewModel.getRecipeListIds().observe(getViewLifecycleOwner(), ids -> {
            adapter.updateRecipeListIds(ids);
        });

        viewModel.getSavedLists().observe(getViewLifecycleOwner(), lists -> {
            adapter.submitList(lists != null ? lists : new ArrayList<>());
        });

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
