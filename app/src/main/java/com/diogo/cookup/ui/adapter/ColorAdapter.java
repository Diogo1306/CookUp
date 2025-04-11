package com.diogo.cookup.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    private final List<String> colorList;
    private int selectedPosition = -1;
    private String selectedColor = "";
    private final OnColorSelectedListener listener;

    public interface OnColorSelectedListener {
        void onColorSelected(String color);
    }

    public ColorAdapter(List<String> colorList, OnColorSelectedListener listener) {
        this.colorList = colorList;
        this.listener = listener;
    }

    public void setSelectedColor(String color) {
        this.selectedColor = color;
        this.selectedPosition = colorList.indexOf(color);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color_option, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        String colorHex = colorList.get(position);

        GradientDrawable colorDrawable = new GradientDrawable();
        colorDrawable.setColor(Color.parseColor(colorHex));
        colorDrawable.setCornerRadius(20f);
        holder.colorView.setBackground(colorDrawable);

        if (position == selectedPosition) {
            holder.selectionBorder.setBackgroundResource(R.drawable.bg_color_selected_border);
        } else {
            holder.selectionBorder.setBackgroundResource(R.drawable.bg_color_unselected);
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            selectedColor = colorList.get(selectedPosition);
            notifyDataSetChanged();
            listener.onColorSelected(selectedColor);
        });
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    static class ColorViewHolder extends RecyclerView.ViewHolder {
        View colorView;
        View selectionBorder;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.color_view);
            selectionBorder = itemView.findViewById(R.id.selection_border);
        }
    }
}
