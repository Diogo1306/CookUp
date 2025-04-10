package com.diogo.cookup.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.SavedListData;
import com.diogo.cookup.data.model.UserData;
import com.diogo.cookup.ui.dialog.CreateListDialog;
import com.diogo.cookup.utils.SharedPrefHelper;
import com.diogo.cookup.viewmodel.SavedListViewModel;

import java.util.ArrayList;
import java.util.List;

public class SavedListAdapterManage extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<SavedListData> listData = new ArrayList<>();
    private final OnListClickListener clickListener;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;

    private static final int TYPE_LIST = 0;
    private static final int TYPE_ADD = 1;

    public SavedListAdapterManage(OnListClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnListClickListener {
        void onListClick(SavedListData list);
    }

    public interface OnEditClickListener {
        void onEditClick(SavedListData list);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(SavedListData list);
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public void submitList(List<SavedListData> newLists) {
        listData.clear();
        listData.addAll(newLists);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == listData.size() ? TYPE_ADD : TYPE_LIST;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ADD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_list_add, parent, false);
            return new AddViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_list_manage, parent, false);
            return new ListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListViewHolder) {
            ((ListViewHolder) holder).bind(listData.get(position));
        } else if (holder instanceof AddViewHolder) {
            ((AddViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return listData.size() + 1;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView listName;
        LinearLayout container;
        ImageButton editButton, deleteButton;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.text_list_name);
            container = itemView.findViewById(R.id.container_color);
            editButton = itemView.findViewById(R.id.button_edit_list);
            deleteButton = itemView.findViewById(R.id.button_remove_list);
        }

        public void bind(SavedListData list) {
            listName.setText(list.list_name);
            try {
                container.setBackgroundColor(Color.parseColor(list.color));
            } catch (Exception e) {
                container.setBackgroundColor(Color.GRAY);
            }

            itemView.setOnClickListener(v -> clickListener.onListClick(list));
            editButton.setOnClickListener(v -> {
                if (editClickListener != null) editClickListener.onEditClick(list);
            });
            deleteButton.setOnClickListener(v -> {
                if (deleteClickListener != null) deleteClickListener.onDeleteClick(list);
            });
        }
    }

    class AddViewHolder extends RecyclerView.ViewHolder {
        public AddViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind() {
            itemView.setOnClickListener(v -> {
                Context context = itemView.getContext();
                CreateListDialog.show(context, (name, color) -> {
                    if (context instanceof FragmentActivity) {
                        FragmentActivity activity = (FragmentActivity) context;
                        SavedListViewModel viewModel = new ViewModelProvider(activity).get(SavedListViewModel.class);
                        UserData user = SharedPrefHelper.getInstance(context).getUser();
                        if (user != null) {
                            viewModel.createList(user.getUserId(), name, color);
                        }
                    }
                });
            });
        }
    }
}
