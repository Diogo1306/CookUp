package com.diogo.cookup.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogo.cookup.R;
import com.diogo.cookup.data.model.CategoryData;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<CategoryData> categoryList;
    private final OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(CategoryData category);
    }

    public CategoryAdapter(List<CategoryData> categoryList, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    public void setData(List<CategoryData> newList) {
        categoryList.clear();
        categoryList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_default, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView label;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.imgCategoryIcon);
            label = itemView.findViewById(R.id.tvCategoryLabel);
        }

        public void bind(CategoryData category) {
            label.setText(category.getCategoryName() != null && !category.getCategoryName().trim().isEmpty()
                    ? category.getCategoryName()
                    : "Sem Nome");

            Glide.with(itemView.getContext())
                    .load(category.getImageUrl() != null && !category.getImageUrl().trim().isEmpty()
                            ? category.getImageUrl()
                            : R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(icon);

            itemView.setOnClickListener(v -> listener.onCategoryClick(category));
        }
    }
}
