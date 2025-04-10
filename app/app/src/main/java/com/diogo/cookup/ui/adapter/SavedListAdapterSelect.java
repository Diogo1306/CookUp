package com.diogo.cookup.ui.adapter;

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

public class SavedListAdapterSelect extends RecyclerView.Adapter<SavedListAdapterSelect.ViewHolder> {

    private final List<SavedListData> listData = new ArrayList<>();
    private final List<Integer> listIdsWithRecipe = new ArrayList<>();
    private final int recipeId;
    private final OnAddClickListener onAddClick;
    private final OnRemoveClickListener onRemoveClick;

    public SavedListAdapterSelect(int recipeId, List<Integer> listIdsWithRecipe, OnAddClickListener onAddClick, OnRemoveClickListener onRemoveClick) {
        this.recipeId = recipeId;
        this.listIdsWithRecipe.addAll(listIdsWithRecipe);
        this.onAddClick = onAddClick;
        this.onRemoveClick = onRemoveClick;
    }

    public void submitList(List<SavedListData> newLists) {
        listData.clear();
        listData.addAll(newLists);
        notifyDataSetChanged();
    }

    public void updateRecipeListIds(List<Integer> newIds) {
        listIdsWithRecipe.clear();
        listIdsWithRecipe.addAll(newIds);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_list_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SavedListData list = listData.get(position);
        holder.bind(list);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView listName;
        LinearLayout container;
        ImageButton removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.text_list_name);
            container = itemView.findViewById(R.id.container_color);
            removeButton = itemView.findViewById(R.id.button_remove_recipe);
        }

        public void bind(SavedListData list) {
            listName.setText(list.list_name);

            try {
                container.setBackgroundColor(android.graphics.Color.parseColor(list.color));
            } catch (Exception e) {
                container.setBackgroundColor(android.graphics.Color.GRAY);
            }

            itemView.setOnClickListener(null);
            removeButton.setOnClickListener(null);

            boolean isRecipeInThisList = listIdsWithRecipe.contains(list.list_id);
            int position = getAdapterPosition();

            if (isRecipeInThisList) {
                removeButton.setVisibility(View.VISIBLE);
                removeButton.setOnClickListener(v -> {
                    onRemoveClick.onRemoveClick(list.list_id, recipeId);
                    listIdsWithRecipe.remove((Integer) list.list_id);
                    notifyItemChanged(position);
                });
                itemView.setOnClickListener(null);
            } else {
                removeButton.setVisibility(View.GONE);
                itemView.setOnClickListener(v -> {
                    onAddClick.onAddClick(list.list_id, recipeId);
                    listIdsWithRecipe.add(list.list_id);
                    notifyItemChanged(position);
                });
            }
        }
    }

    public interface OnAddClickListener {
        void onAddClick(int listId, int recipeId);
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(int listId, int recipeId);
    }
}
