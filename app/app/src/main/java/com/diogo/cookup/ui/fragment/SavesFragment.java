package com.diogo.cookup.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.SavedListData;
import com.diogo.cookup.ui.adapter.SavedListAdapter;
import com.diogo.cookup.utils.MessageUtils;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.SavedListViewModel;

import java.util.List;

public class SavesFragment extends Fragment {

    private SavedListViewModel viewModel;
    private SavedListAdapter adapter;

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

        adapter = new SavedListAdapter(list -> {
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

        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(SavedListViewModel.class);

        viewModel.getSavedLists().observe(getViewLifecycleOwner(), this::updateUI);

        int userId = SharedPrefHelper.getInstance(requireContext()).getUser().getUserId();
        viewModel.loadLists(userId);
    }

    private void updateUI(List<SavedListData> lists) {
        adapter.submitList(lists);
    }
}
