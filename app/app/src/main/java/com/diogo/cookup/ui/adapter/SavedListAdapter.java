package com.diogo.cookup.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.SavedListData;

import java.util.ArrayList;
import java.util.List;

public class SavedListAdapter extends RecyclerView.Adapter<SavedListAdapter.SavedListViewHolder> {

    public interface OnListClickListener {
        void onListClick(SavedListData list);
    }

    private final List<SavedListData> savedLists = new ArrayList<>();
    private final OnListClickListener clickListener;

    public SavedListAdapter(OnListClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SavedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_list, parent, false);
        return new SavedListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedListViewHolder holder, int position) {
        SavedListData list = savedLists.get(position);
        holder.textName.setText(list.list_name);

        try {
            holder.container.setBackgroundColor(Color.parseColor(list.color));
        } catch (Exception e) {
            holder.container.setBackgroundColor(Color.GRAY);
        }

        holder.itemView.setOnClickListener(v -> clickListener.onListClick(list));
    }

    @Override
    public int getItemCount() {
        return savedLists.size();
    }

    public void submitList(List<SavedListData> newLists) {
        savedLists.clear();
        savedLists.addAll(newLists);
        notifyDataSetChanged();
    }

    static class SavedListViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        LinearLayout container;

        public SavedListViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_list_name);
            container = itemView.findViewById(R.id.container_color);
        }
    }
}
