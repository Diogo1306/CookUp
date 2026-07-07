package com.diogo.cookup.ui.adapter;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.IngredientData;

import java.util.ArrayList;
import java.util.List;

public class IngredientSaveOrUpdateAdapter extends RecyclerView.Adapter<IngredientSaveOrUpdateAdapter.ViewHolder> {

    private final List<IngredientData> ingredients;
    private final OnRemoveClickListener onRemoveClickListener;
    private final OnNameTypedListener onNameTypedListener;
    private List<String> suggestions = new ArrayList<>();
    private AutoCompleteTextView lastFocused = null;

    public interface OnRemoveClickListener {
        void onRemove(int position);
    }

    public interface OnNameTypedListener {
        void onNameTyped(int position, String name, AutoCompleteTextView view);
    }

    public IngredientSaveOrUpdateAdapter(List<IngredientData> list,
                                         OnRemoveClickListener listener,
                                         OnNameTypedListener nameListener) {
        this.ingredients = list;
        this.onRemoveClickListener = listener;
        this.onNameTypedListener = nameListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient_save_update, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IngredientData ingredient = ingredients.get(position);

        if (holder.nameWatcher != null)
            holder.etName.removeTextChangedListener(holder.nameWatcher);
        if (holder.qtyWatcher != null)
            holder.etQuantity.removeTextChangedListener(holder.qtyWatcher);

        holder.etName.setText(ingredient.getName());
        holder.etQuantity.setText(ingredient.getQuantity());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.etName.getContext(),
                android.R.layout.simple_dropdown_item_1line, suggestions);
        holder.etName.setAdapter(adapter);

        holder.etName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) lastFocused = holder.etName;
        });

        holder.etName.setOnClickListener(v -> {
            holder.etName.requestFocus();
            holder.etName.showDropDown();
        });

        holder.nameWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                ingredient.setName(s.toString().trim());
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && onNameTypedListener != null) {
                    onNameTypedListener.onNameTyped(pos, s.toString(), holder.etName);
                }
            }
            @Override public void afterTextChanged(Editable s) { }
        };
        holder.etName.addTextChangedListener(holder.nameWatcher);

        holder.qtyWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                ingredient.setQuantity(s.toString().trim());
            }
            @Override public void afterTextChanged(Editable s) { }
        };
        holder.etQuantity.addTextChangedListener(holder.qtyWatcher);

        holder.btnRemove.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (onRemoveClickListener != null && pos != RecyclerView.NO_POSITION) {
                onRemoveClickListener.onRemove(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void updateSuggestions(List<String> newSuggestions) {
        List<String> filtered = new ArrayList<>();
        List<String> currentNames = new ArrayList<>();
        for (IngredientData i : ingredients) {
            if (i.getName() != null) currentNames.add(i.getName().toLowerCase());
        }
        for (String s : newSuggestions) {
            if (!currentNames.contains(s.toLowerCase())) {
                filtered.add(s);
            }
        }

        suggestions.clear();
        suggestions.addAll(filtered);

        if (lastFocused != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    lastFocused.getContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    suggestions
            );
            lastFocused.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void updateList(List<IngredientData> newList) {
        ingredients.clear();
        ingredients.addAll(newList);
        new Handler(Looper.getMainLooper()).post(this::notifyDataSetChanged);
    }

    public List<IngredientData> getIngredients() {
        return ingredients;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AutoCompleteTextView etName;
        EditText etQuantity;
        ImageButton btnRemove;
        TextWatcher nameWatcher;
        TextWatcher qtyWatcher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etName = itemView.findViewById(R.id.et_ingredient_name);
            etQuantity = itemView.findViewById(R.id.et_ingredient_quantity);
            btnRemove = itemView.findViewById(R.id.btn_remove_ingredient);
        }
    }
}
