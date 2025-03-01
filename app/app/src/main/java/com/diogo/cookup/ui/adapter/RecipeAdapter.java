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
import com.diogo.cookup.data.model.RecipeData;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private List<RecipeData> recipeList;
    private OnItemClickListener onItemClickListener;

    public RecipeAdapter(List<RecipeData> recipes) {
        this.recipeList = recipes;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeData recipe = recipeList.get(position);
        holder.bind(recipe, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return (recipeList != null) ? recipeList.size() : 0;
    }

    public void updateData(List<RecipeData> newRecipes) {
        this.recipeList = newRecipes;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, preparationTime, servings;
        private final ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
            preparationTime = itemView.findViewById(R.id.preparation_time);
            servings = itemView.findViewById(R.id.servings);
            image = itemView.findViewById(R.id.recipe_image);
        }

        public void bind(RecipeData recipe, OnItemClickListener listener) {
            title.setText(recipe.getTitle());

            int prepTime = recipe.getPreparationTime();
            preparationTime.setText(prepTime > 0 ? prepTime + " min." : "Tempo não disponível");

            int servingsCount = recipe.getServings();
            servings.setText(servingsCount > 0 ? servingsCount + " doses" : "Quantidade não disponível");

            Glide.with(itemView.getContext())
                    .load(recipe.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(image);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(recipe);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecipeData recipe);
    }
}
