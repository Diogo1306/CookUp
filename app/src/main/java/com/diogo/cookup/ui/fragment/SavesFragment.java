package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.fragment.NavHostFragment;

import com.diogo.cookup.R;
import com.diogo.cookup.ui.adapter.SavedListAdapterManage;
import com.diogo.cookup.ui.dialog.CreateListDialog;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.SavedListViewModel;

import java.util.ArrayList;

public class SavesFragment extends Fragment {

    private SavedListViewModel viewModel;
    private SavedListAdapterManage adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saves, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_saved_lists);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        viewModel = new ViewModelProvider(requireActivity()).get(SavedListViewModel.class);

        if (viewModel.getUserLiveData().getValue() == null && SharedPrefHelper.getInstance(requireContext()).getUser() != null) {
            viewModel.setUser(SharedPrefHelper.getInstance(requireContext()).getUser());
        }

        adapter = new SavedListAdapterManage(list -> {
            Bundle args = new Bundle();
            args.putInt("list_id", list.list_id);
            args.putString("list_name", list.list_name);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_savesFragment_to_savedRecipesFragment, args);
        });

        adapter.setOnAddClickListener(() -> {
            CreateListDialog.show(requireContext(), (name, color) -> {
                if (viewModel.getUserLiveData().getValue() != null) {
                    int userId = viewModel.getUserLiveData().getValue().getUserId();
                    viewModel.createList(userId, name, color);
                    viewModel.loadLists(userId);
                }
            });
        });

        adapter.setOnEditClickListener(list -> {
            CreateListDialog.show(requireContext(), list, (listId, name, color) -> {
                viewModel.updateList(listId, name, color);
            });
        });

        adapter.setOnDeleteClickListener(list -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar lista")
                    .setMessage("Tem a certeza que deseja eliminar esta lista?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        viewModel.deleteList(list.list_id);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        recyclerView.setAdapter(adapter);

        viewModel.getSavedLists().observe(getViewLifecycleOwner(), lists -> {
            if (lists != null && !lists.isEmpty()) {
                adapter.submitList(lists);
            } else {
                adapter.submitList(new ArrayList<>());
            }
        });

        if (viewModel.getUserLiveData().getValue() != null) {
            viewModel.loadLists(viewModel.getUserLiveData().getValue().getUserId());
        }
    }
}