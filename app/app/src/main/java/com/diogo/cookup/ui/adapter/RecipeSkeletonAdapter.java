package com.diogo.cookup.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;

import org.checkerframework.checker.nullness.qual.NonNull;

public class RecipeSkeletonAdapter extends RecyclerView.Adapter<RecipeSkeletonAdapter.SkeletonViewHolder> {
    private final int itemCount;

    public RecipeSkeletonAdapter(int itemCount) {
        this.itemCount = itemCount;
    }

    @NonNull
    @Override
    public SkeletonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_skeleton, parent, false);
        return new SkeletonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkeletonViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    static class SkeletonViewHolder extends RecyclerView.ViewHolder {
        public SkeletonViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

