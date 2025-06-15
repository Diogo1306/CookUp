package com.diogo.cookup.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
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
import java.util.Iterator;
import java.util.List;

public class SavedListAdapterSelect extends RecyclerView.Adapter<SavedListAdapterSelect.ViewHolder> {

    private final List<SavedListData> listData = new ArrayList<>();
    private final List<Integer> listIdsWithRecipe = new ArrayList<>();
    private final int recipeId;
    private final OnAddClickListener onAddClick;
    private final OnRemoveClickListener onRemoveClick;

    public SavedListAdapterSelect(int recipeId, List<Integer> listIdsWithRecipe,
                                  OnAddClickListener onAddClick, OnRemoveClickListener onRemoveClick) {
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

    public void removeRecipeFromVisual(int listId) {
        listIdsWithRecipe.remove(Integer.valueOf(listId));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_list_select, parent, false);
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

    public interface OnAddClickListener {
        void onAddClick(int listId, int recipeId);
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(int listId, int recipeId);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView listName;
        LinearLayout container;
        ImageButton removeButton;
        View colorCircle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.text_list_name);
            container = itemView.findViewById(R.id.container_color);
            removeButton = itemView.findViewById(R.id.button_remove_recipe);
            colorCircle = itemView.findViewById(R.id.color_circle);
        }

        public void bind(SavedListData list) {
            listName.setText(list.list_name);

            try {
                int colorParsed = Color.parseColor(list.color);
                container.setBackgroundColor(colorParsed);

                GradientDrawable circleGradient = new GradientDrawable(
                        GradientDrawable.Orientation.TL_BR,
                        new int[]{colorParsed, Color.WHITE}
                );
                circleGradient.setShape(GradientDrawable.OVAL);
                colorCircle.setBackground(circleGradient);
            } catch (Exception e) {
                container.setBackgroundColor(Color.GRAY);
                colorCircle.setBackgroundColor(Color.GRAY);
            }

            boolean isRecipeInList = listIdsWithRecipe.contains(list.list_id);

            if (isRecipeInList) {
                removeButton.setVisibility(View.VISIBLE);
                removeButton.setOnClickListener(v -> {
                    onRemoveClick.onRemoveClick(list.list_id, recipeId);
                    removeRecipeFromVisual(list.list_id);
                });
                itemView.setOnClickListener(null);
            } else {
                removeButton.setVisibility(View.GONE);
                itemView.setOnClickListener(v -> onAddClick.onAddClick(list.list_id, recipeId));
            }
        }
    }
}
