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
    private final List<RecipeData> recipeList;

    public RecipeAdapter(List<RecipeData> recipes) {
        this.recipeList = recipes;
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
        holder.title.setText(recipe.getTitle());

        int prepTime = recipe.getPreparation_time();
        String preparationText = (prepTime > 0) ? prepTime + " min." : "Tempo não disponível";
        holder.preparationTime.setText(preparationText);

        int servingsCount = recipe.getServings();
        String servingsText = (servingsCount > 0) ? servingsCount + " doses" : "Quantidade não disponível";
        holder.servings.setText(servingsText);

        Glide.with(holder.itemView.getContext())
                .load(recipe.getImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, preparationTime, servings;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
            preparationTime = itemView.findViewById(R.id.preparation_time);
            servings = itemView.findViewById(R.id.servings);
            image = itemView.findViewById(R.id.recipe_image);
        }
    }
}
