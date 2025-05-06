package com.diogo.cookup.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;

import org.checkerframework.checker.nullness.qual.NonNull;

public class CategorySkeletonAdapter extends RecyclerView.Adapter<CategorySkeletonAdapter.SkeletonViewHolder> {

    private static final int SKELETON_COUNT = 8;

    @NonNull
    @Override
    public SkeletonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_skeleton, parent, false);
        return new SkeletonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkeletonViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return SKELETON_COUNT;
    }

    static class SkeletonViewHolder extends RecyclerView.ViewHolder {
        public SkeletonViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

