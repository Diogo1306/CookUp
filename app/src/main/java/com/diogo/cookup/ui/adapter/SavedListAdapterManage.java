package com.diogo.cookup.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.SavedListData;

import java.util.ArrayList;
import java.util.List;

public class SavedListAdapterManage extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ADD = 0;
    private static final int TYPE_LIST = 1;

    private final List<SavedListData> listData = new ArrayList<>();
    private final OnListClickListener onListClickListener;
    private OnAddClickListener onAddClickListener;
    private OnEditClickListener onEditClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public SavedListAdapterManage(OnListClickListener onListClickListener) {
        this.onListClickListener = onListClickListener;
    }

    public interface OnListClickListener {
        void onListClick(SavedListData list);
    }

    public interface OnAddClickListener {
        void onAddClick();
    }

    public interface OnEditClickListener {
        void onEditClick(SavedListData list);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(SavedListData list);
    }

    public void setOnAddClickListener(OnAddClickListener listener) {
        this.onAddClickListener = listener;
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void submitList(List<SavedListData> newLists) {
        listData.clear();
        if (newLists != null) {
            listData.addAll(newLists);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_ADD : TYPE_LIST;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ADD) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_saved_list_add, parent, false);
            return new AddViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_saved_list_manage, parent, false);
            return new ManageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ManageViewHolder) {
            ((ManageViewHolder) holder).bind(listData.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size() + 1;
    }

    class AddViewHolder extends RecyclerView.ViewHolder {
        public AddViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                if (onAddClickListener != null) {
                    onAddClickListener.onAddClick();
                }
            });
        }
    }

    class ManageViewHolder extends RecyclerView.ViewHolder {
        TextView listName;
        LinearLayout container;
        ImageButton editButton, deleteButton;

        public ManageViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_name);
            container = itemView.findViewById(R.id.container_color);
            editButton = itemView.findViewById(R.id.button_edit_list);
            deleteButton = itemView.findViewById(R.id.button_delete_list);
        }

        public void bind(SavedListData list) {
            listName.setText(list.list_name != null ? list.list_name : "Lista sem nome");

            try {
                if (list.color != null && !list.color.isEmpty()) {
                    int colorParsed = Color.parseColor(list.color);
                    container.setBackgroundColor(colorParsed);
                } else {
                    container.setBackgroundColor(Color.LTGRAY);
                }
            } catch (Exception e) {
                container.setBackgroundColor(Color.LTGRAY);
            }

            itemView.setOnClickListener(v -> {
                if (onListClickListener != null) onListClickListener.onListClick(list);
            });

            editButton.setOnClickListener(v -> {
                if (onEditClickListener != null) onEditClickListener.onEditClick(list);
            });

            deleteButton.setOnClickListener(v -> {
                if (onDeleteClickListener != null) onDeleteClickListener.onDeleteClick(list);
            });
        }
    }
}
