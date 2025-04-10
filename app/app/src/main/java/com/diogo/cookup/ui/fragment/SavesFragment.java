package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.SavedListData;
import com.diogo.cookup.ui.adapter.SavedListAdapterManage;
import com.diogo.cookup.ui.dialog.CreateListDialog;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.SavedListViewModel;

import java.util.ArrayList;
import java.util.List;

public class SavesFragment extends Fragment {

    private SavedListViewModel viewModel;
    private SavedListAdapterManage adapter;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saves, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_saved_lists);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new SavedListAdapterManage(list -> {
            if (list.list_id == -1) return;

            SavedRecipesFragment fragment = new SavedRecipesFragment();
            Bundle args = new Bundle();
            args.putInt("list_id", list.list_id);
            args.putString("list_name", list.list_name);
            fragment.setArguments(args);

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
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
                    .setPositiveButton("Eliminar", (d, i) -> viewModel.deleteList(list.list_id))
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(SavedListViewModel.class);

        viewModel.getSavedLists().observe(getViewLifecycleOwner(), lists -> {
            List<SavedListData> newList = new ArrayList<>(lists);

            SavedListData addNew = new SavedListData();
            addNew.list_id = -1;
            addNew.list_name = "Criar nova lista";
            addNew.color = "#DDDDDD";

            newList.add(addNew);
            adapter.submitList(newList);
        });

        viewModel.getMessage().observe(getViewLifecycleOwner(), msg -> {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
        });

        userId = SharedPrefHelper.getInstance(requireContext()).getUser().getUserId();
        viewModel.setUser(SharedPrefHelper.getInstance(requireContext()).getUser());
        viewModel.loadLists(userId);
    }
}
