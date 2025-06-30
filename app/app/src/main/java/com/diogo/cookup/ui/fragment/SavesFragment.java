package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        recyclerView.setClipToPadding(false);

        TextView greeting = view.findViewById(R.id.text_saved_greeting);
        TextView intro = view.findViewById(R.id.text_saved_intro);

        viewModel = new ViewModelProvider(requireActivity()).get(SavedListViewModel.class);

        if (viewModel.getUserLiveData().getValue() == null &&
                SharedPrefHelper.getInstance(requireContext()).getUser() != null) {
            viewModel.setUser(SharedPrefHelper.getInstance(requireContext()).getUser());
        }

        if (viewModel.getUserLiveData().getValue() != null) {
            String firstName = viewModel.getUserLiveData().getValue().getUsername().split(" ")[0];
            greeting.setText(getString(R.string.hello_name, firstName));
        }

        View fab = view.findViewById(R.id.fab_add_list);
        fab.setOnClickListener(v -> {
            CreateListDialog.show(requireContext(), (name, color) -> {
                if (viewModel.getUserLiveData().getValue() != null) {
                    int userId = viewModel.getUserLiveData().getValue().getUserId();
                    viewModel.createList(userId, name, color);
                    viewModel.loadListsWithRecipes(userId);
                }
            });
        });

        adapter = new SavedListAdapterManage(list -> {
            Bundle args = new Bundle();
            args.putInt("list_id", list.list_id);
            args.putString("list_name", list.list_name);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_savesFragment_to_savedRecipesFragment, args);
        });

        adapter.setOnEditClickListener(list -> {
            CreateListDialog.show(requireContext(), list, (listId, name, color) -> {
                viewModel.updateList(listId, name, color);
            });
        });

        adapter.setOnDeleteClickListener(list -> {
            View dialogView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_confirm_delete, null);

            AlertDialog dialog = new AlertDialog.Builder(requireContext())
                    .setView(dialogView)
                    .create();

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            TextView cancelButton = dialogView.findViewById(R.id.button_cancel);
            TextView deleteButton = dialogView.findViewById(R.id.button_delete);
            TextView title = dialogView.findViewById(R.id.dialog_title);
            TextView message = dialogView.findViewById(R.id.dialog_message);

            title.setText(getString(R.string.delete_list_title));
            message.setText(getString(R.string.delete_list_message));

            cancelButton.setOnClickListener(v -> dialog.dismiss());
            deleteButton.setOnClickListener(v -> {
                viewModel.deleteList(list.list_id);
                dialog.dismiss();
            });

            dialog.show();
        });

        recyclerView.setAdapter(adapter);

        viewModel.getSavedLists().observe(getViewLifecycleOwner(), lists -> {
            if (lists != null && !lists.isEmpty()) {
                intro.setText(getString(R.string.saved_collections_intro, lists.size()));
                adapter.submitList(lists);
            } else {
                intro.setText(getString(R.string.no_saved_lists_intro));
                adapter.submitList(new ArrayList<>());
            }
        });

        if (viewModel.getUserLiveData().getValue() != null) {
            viewModel.loadListsWithRecipes(viewModel.getUserLiveData().getValue().getUserId());
        }
    }
}