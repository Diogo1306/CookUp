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
import com.diogo.cookup.ui.adapter.ColorAdapter;

import java.util.Arrays;
import java.util.List;

public class CreateListDialog {

    public interface OnListCreateListener {
        void onListCreate(String name, String color);
    }

    public static void show(Context context, OnListCreateListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_create_list, null);

        EditText nameInput = view.findViewById(R.id.edit_list_name);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_colors);

        List<String> colorOptions = Arrays.asList(
                "#FFA07A", // Salmão vivo
                "#F4A460", // Caramelo
                "#FFB347", // Laranja damasco
                "#B0E57C", // Verde kiwi
                "#98E2B7", // Verde menta mais vivo
                "#C397D8", // Roxo uva suave
                "#FF69B4", // Rosa chiclete
                "#87CEFA", // Azul céu claro
                "#FFDEAD", // Amendoim
                "#BC8F8F"  // Chocolate com leite
        );

        final String[] backgroundColor = {colorOptions.get(0)};

        ColorAdapter adapter = new ColorAdapter(colorOptions, color -> backgroundColor[0] = color);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 5));
        recyclerView.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Criar nova lista")
                .setView(view)
                .setPositiveButton("Criar", (dialogInterface, which) -> {
                    String name = nameInput.getText().toString().trim();

                    if (!name.isEmpty()) {
                        listener.onListCreate(name, backgroundColor[0]);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.show();

        View window = dialog.getWindow().getDecorView();
        int color = ContextCompat.getColor(context, R.color.background);
        window.setBackgroundColor(color);

    }
}
