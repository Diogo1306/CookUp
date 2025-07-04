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

public class SavedListAdapter extends RecyclerView.Adapter<SavedListAdapter.SavedListViewHolder> {

    private int recipeId = -1;
    private List<Integer> recipeListIds = new ArrayList<>();
    private OnRemoveClickListener removeClickListener;
    private OnEditClickListener editClickListener;
    private final List<SavedListData> savedLists = new ArrayList<>();
    private final OnListClickListener clickListener;

    public interface OnListClickListener {
        void onListClick(SavedListData list);
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(int listId, int recipeId);
    }

    public interface OnEditClickListener {
        void onEditClick(SavedListData list);
    }

    public void setOnRemoveClickListener(OnRemoveClickListener listener) {
        this.removeClickListener = listener;
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }

    public SavedListAdapter(OnListClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setRecipeContext(int recipeId, List<Integer> recipeListIds) {
        this.recipeId = recipeId;
        this.recipeListIds = recipeListIds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SavedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_saved, parent, false);
        return new SavedListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedListViewHolder holder, int position) {
        SavedListData list = savedLists.get(position);
        holder.bind(list, position);
    }

    @Override
    public int getItemCount() {
        return savedLists.size();
    }

    public void submitList(List<SavedListData> newLists) {
        savedLists.clear();
        savedLists.addAll(newLists);
        notifyDataSetChanged();
    }

    class SavedListViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        LinearLayout container;
        ImageButton buttonRemove, buttonEdit;

        public SavedListViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_list_name);
            container = itemView.findViewById(R.id.container_color);
            buttonRemove = itemView.findViewById(R.id.button_remove_recipe);
            buttonEdit = itemView.findViewById(R.id.button_edit_list);
        }

        public void bind(SavedListData list, int position) {
            textName.setText(list.list_name);

            try {
                container.setBackgroundColor(Color.parseColor(list.color));
            } catch (Exception e) {
                container.setBackgroundColor(Color.GRAY);
            }

            boolean isCreateNew = list.list_id == -1;

            if (isCreateNew) {
                textName.setText("Criar nova lista");
                buttonRemove.setVisibility(View.GONE);
                buttonEdit.setVisibility(View.GONE);
                itemView.setOnClickListener(v -> clickListener.onListClick(list));
                return;
            }

            itemView.setOnClickListener(v -> clickListener.onListClick(list));

            boolean isRecipeInList = recipeListIds.contains(list.list_id);

            if (isRecipeInList) {
                buttonRemove.setVisibility(View.VISIBLE);
                buttonRemove.setOnClickListener(v -> {
                    if (removeClickListener != null) {
                        removeClickListener.onRemoveClick(list.list_id, recipeId);
                        recipeListIds.remove(Integer.valueOf(list.list_id));
                        notifyItemChanged(position);
                    }
                });
            } else {
                buttonRemove.setVisibility(View.GONE);
            }

            buttonEdit.setVisibility(View.VISIBLE);
            buttonEdit.setOnClickListener(v -> {
                if (editClickListener != null) {
                    editClickListener.onEditClick(list);
                    notifyItemChanged(position);
                }
            });
        }
    }
}
