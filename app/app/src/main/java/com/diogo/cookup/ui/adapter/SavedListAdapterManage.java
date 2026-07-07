package com.diogo.cookup.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.SavedListData;

import java.util.ArrayList;
import java.util.List;

public class SavedListAdapterManage extends RecyclerView.Adapter<SavedListAdapterManage.ManageViewHolder> {

    private final List<SavedListData> listData = new ArrayList<>();
    private final OnListClickListener onListClickListener;
    private OnEditClickListener onEditClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public SavedListAdapterManage(OnListClickListener onListClickListener) {
        this.onListClickListener = onListClickListener;
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

    @NonNull
    @Override
    public ManageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_list_manage, parent, false);
        return new ManageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageViewHolder holder, int position) {
        holder.bind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ManageViewHolder extends RecyclerView.ViewHolder {

        TextView listName, recipeCount;
        ImageButton editButton, deleteButton;
        ImageView image1, image2, image3;
        ImageView image4_1, image4_2, image4_3, image4_4;
        ImageView image3_1, image3_2, image3_3;
        View imageRow2, imageGrid2x2, imageGrid3Custom;
        View viewColorDot;

        public ManageViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_name);
            recipeCount = itemView.findViewById(R.id.text_recipe_count);
            editButton = itemView.findViewById(R.id.button_edit_list);
            deleteButton = itemView.findViewById(R.id.button_delete_list);

            image1 = itemView.findViewById(R.id.image_1);
            image2 = itemView.findViewById(R.id.image_2);
            image3 = itemView.findViewById(R.id.image_3);

            image4_1 = itemView.findViewById(R.id.image_4_1);
            image4_2 = itemView.findViewById(R.id.image_4_2);
            image4_3 = itemView.findViewById(R.id.image_4_3);
            image4_4 = itemView.findViewById(R.id.image_4_4);

            image3_1 = itemView.findViewById(R.id.image_3_1);
            image3_2 = itemView.findViewById(R.id.image_3_2);
            image3_3 = itemView.findViewById(R.id.image_3_3);

            imageRow2 = itemView.findViewById(R.id.image_row_2);
            imageGrid2x2 = itemView.findViewById(R.id.image_grid_2x2);
            imageGrid3Custom = itemView.findViewById(R.id.image_grid_3_custom);

            viewColorDot = itemView.findViewById(R.id.view_color_dot);
        }

        public void bind(SavedListData list) {
            listName.setText(list.list_name != null ? list.list_name : "Lista sem nome");
            recipeCount.setText(list.recipes != null ? list.recipes.size() + " receitas" : "0 receitas");

            image1.setVisibility(View.GONE);
            imageRow2.setVisibility(View.GONE);
            imageGrid2x2.setVisibility(View.GONE);
            imageGrid3Custom.setVisibility(View.GONE);

            if (list.color != null && viewColorDot != null) {
                try {
                    int color = Color.parseColor(list.color);
                    GradientDrawable dot = (GradientDrawable) viewColorDot.getBackground().mutate();
                    dot.setColor(color);
                } catch (Exception ignored) {}
            }

            if (list.recipes == null || list.recipes.isEmpty()) {
                image1.setVisibility(View.VISIBLE);
                image1.setImageDrawable(null);
            } else {
                int count = list.recipes.size();
                if (count == 1) {
                    image1.setVisibility(View.VISIBLE);
                    loadOrColor(image1, list.recipes.get(0).getImage(), list.color);
                } else if (count == 2) {
                    imageRow2.setVisibility(View.VISIBLE);
                    loadOrColor(image2, list.recipes.get(0).getImage(), list.color);
                    loadOrColor(image3, list.recipes.get(1).getImage(), list.color);
                } else if (count == 3) {
                    imageGrid3Custom.setVisibility(View.VISIBLE);
                    loadOrColor(image3_1, list.recipes.get(0).getImage(), list.color);
                    loadOrColor(image3_2, list.recipes.get(1).getImage(), list.color);
                    loadOrColor(image3_3, list.recipes.get(2).getImage(), list.color);
                } else {
                    imageGrid2x2.setVisibility(View.VISIBLE);
                    loadOrColor(image4_1, list.recipes.get(0).getImage(), list.color);
                    loadOrColor(image4_2, list.recipes.get(1).getImage(), list.color);
                    loadOrColor(image4_3, list.recipes.get(2).getImage(), list.color);
                    loadOrColor(image4_4, list.recipes.get(3).getImage(), list.color);
                }
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

        private void loadOrColor(ImageView view, String url, String fallbackColor) {
            if (url != null && !url.isEmpty()) {
                Glide.with(itemView.getContext()).load(url).placeholder(R.drawable.placeholder).into(view);
            } else {
                view.setImageDrawable(null);
                applyFallbackColor(view, fallbackColor);
            }
        }

        private void applyFallbackColor(View view, String colorHex) {
            try {
                int color = Color.parseColor(colorHex);
                GradientDrawable shape = new GradientDrawable();
                shape.setColor(color);
                shape.setCornerRadius(16);
                view.setBackground(shape);
            } catch (Exception ignored) {}
        }
    }
}