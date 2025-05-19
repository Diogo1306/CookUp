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
import com.diogo.cookup.data.model.RecipeCategoryData;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<RecipeCategoryData> categoryList;
    private final OnCategoryClickListener listener;
    private boolean skeletonMode = true;

    private static final int VIEW_TYPE_SKELETON = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    public interface OnCategoryClickListener {
        void onCategoryClick(RecipeCategoryData category);
    }

    public CategoryAdapter(List<RecipeCategoryData> categoryList, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    public void setSkeletonMode(boolean skeletonMode) {
        this.skeletonMode = skeletonMode;
        notifyDataSetChanged();
    }

    public boolean isSkeletonMode() {
        return skeletonMode;
    }

    public void setData(List<RecipeCategoryData> list) {
        categoryList.clear();
        categoryList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return skeletonMode ? VIEW_TYPE_SKELETON : VIEW_TYPE_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_SKELETON) {
            View view = inflater.inflate(R.layout.item_category_skeleton, parent, false);
            return new SkeletonViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_category_default, parent, false);
            return new CategoryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!skeletonMode && holder instanceof CategoryViewHolder) {
            ((CategoryViewHolder) holder).bind(categoryList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return skeletonMode ? 6 : categoryList.size();
    }

    static class SkeletonViewHolder extends RecyclerView.ViewHolder {
        public SkeletonViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView label;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.imgCategoryIcon);
            label = itemView.findViewById(R.id.tvCategoryLabel);
        }

        public void bind(RecipeCategoryData category) {
            String name = category.getCategoryName();
            label.setText(name != null && !name.trim().isEmpty() ? name : "Sem Nome");

            String imageUrl = category.getImageUrl();
            Glide.with(itemView.getContext())
                    .load(imageUrl != null && !imageUrl.trim().isEmpty() ? imageUrl : R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(icon);

            itemView.setOnClickListener(v -> listener.onCategoryClick(category));
        }
    }
}
