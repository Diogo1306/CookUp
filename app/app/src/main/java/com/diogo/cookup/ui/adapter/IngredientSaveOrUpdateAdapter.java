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

public class IngredientSaveOrUpdateAdapter extends RecyclerView.Adapter<IngredientSaveOrUpdateAdapter.IngredientViewHolder> {

    private final List<IngredientData> ingredientList;
    private final OnRemoveClickListener onRemoveClickListener;
    private final OnNameTypedListener onNameTypedListener;
    private final Handler handler = new Handler(Looper.getMainLooper());

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
        this.ingredientList = list;
        this.onRemoveClickListener = listener;
        this.onNameTypedListener = nameListener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient_save_update, parent, false);
        return new IngredientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientData ingredient = ingredientList.get(position);

        if (holder.currentWatcherName != null)
            holder.etName.removeTextChangedListener(holder.currentWatcherName);
        if (holder.currentWatcherQty != null)
            holder.etQuantity.removeTextChangedListener(holder.currentWatcherQty);


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
            holder.etName.setSelection(holder.etName.getText().length());
        });

        // Watcher nome
        holder.currentWatcherName = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                ingredient.setName(s.toString());
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && onNameTypedListener != null) {
                    onNameTypedListener.onNameTyped(pos, s.toString(), holder.etName);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        };
        holder.etName.addTextChangedListener(holder.currentWatcherName);

        holder.currentWatcherQty = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                ingredient.setQuantity(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        };
        holder.etQuantity.addTextChangedListener(holder.currentWatcherQty);

        holder.btnRemove.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (onRemoveClickListener != null && pos != RecyclerView.NO_POSITION) {
                onRemoveClickListener.onRemove(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public void updateSuggestions(List<String> newSuggestions) {
        List<String> filtered = new ArrayList<>();
        List<String> currentNames = new ArrayList<>();
        for (IngredientData i : ingredientList) {
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
        for (int i = 0; i < ingredientList.size(); i++) {
            if (i < newList.size()) {
                IngredientData current = ingredientList.get(i);
                newList.get(i).setName(current.getName());
                newList.get(i).setQuantity(current.getQuantity());
            }
        }

        ingredientList.clear();
        ingredientList.addAll(newList);

        handler.post(this::notifyDataSetChanged);
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        AutoCompleteTextView etName;
        EditText etQuantity;
        ImageButton btnRemove;
        TextWatcher currentWatcherName;
        TextWatcher currentWatcherQty;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            etName = itemView.findViewById(R.id.et_ingredient_name);
            etQuantity = itemView.findViewById(R.id.et_ingredient_quantity);
            btnRemove = itemView.findViewById(R.id.btn_remove_ingredient);
        }
    }
}
