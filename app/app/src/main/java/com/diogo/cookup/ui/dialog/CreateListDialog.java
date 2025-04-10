package com.diogo.cookup.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.SavedListData;
import com.diogo.cookup.ui.adapter.ColorAdapter;

import java.util.Arrays;
import java.util.List;

public class CreateListDialog {

    public interface OnListCreateListener {
        void onListCreate(String name, String color);
    }

    public interface OnListEditListener {
        void onEdit(int listId, String name, String color);
    }

    public static void show(Context context, OnListCreateListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_create_list, null);

        EditText nameInput = view.findViewById(R.id.edit_list_name);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_colors);

        List<String> colorOptions = getColorOptions();
        final String[] selectedColor = {colorOptions.get(0)};

        ColorAdapter adapter = new ColorAdapter(colorOptions, color -> selectedColor[0] = color);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 5));
        recyclerView.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Criar nova lista")
                .setView(view)
                .setPositiveButton("Criar", (d, i) -> {
                    String name = nameInput.getText().toString().trim();
                    if (!name.isEmpty()) {
                        listener.onListCreate(name, selectedColor[0]);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.show();
        dialog.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(context, R.color.background));
    }

    public static void show(Context context, SavedListData list, OnListEditListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_create_list, null);

        EditText nameInput = view.findViewById(R.id.edit_list_name);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_colors);

        nameInput.setText(list.list_name);

        List<String> colorOptions = getColorOptions();
        final String[] selectedColor = {list.color};

        ColorAdapter adapter = new ColorAdapter(colorOptions, selected -> selectedColor[0] = selected);
        adapter.setSelectedColor(list.color);

        recyclerView.setLayoutManager(new GridLayoutManager(context, 5));
        recyclerView.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Editar lista")
                .setView(view)
                .setPositiveButton("Guardar", (d, i) -> {
                    String name = nameInput.getText().toString().trim();
                    if (!name.isEmpty()) {
                        listener.onEdit(list.list_id, name, selectedColor[0]);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.show();
        dialog.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(context, R.color.background));
    }

    private static List<String> getColorOptions() {
        return Arrays.asList(
                "#FFA07A", "#F4A460", "#FFB347", "#B0E57C", "#98E2B7",
                "#C397D8", "#FF69B4", "#87CEFA", "#FFDEAD", "#BC8F8F"
        );
    }
}
