package com.diogo.cookup.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.SavedListData;

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

        EditText nameInput = view.findViewById(R.id.input_list_name);
        GridLayout colorGrid = view.findViewById(R.id.color_grid);
        Button createButton = view.findViewById(R.id.button_create);
        ImageButton backButton = view.findViewById(R.id.arrow_back);

        List<String> colorOptions = getColorOptions();
        final String[] selectedColor = {colorOptions.get(0)};

        populateColorGrid(context, colorGrid, colorOptions, selectedColor[0], color -> selectedColor[0] = color);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();

        createButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (!name.isEmpty()) {
                listener.onListCreate(name, selectedColor[0]);
                dialog.dismiss();
            }
        });

        backButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_rounded);
    }

    public static void show(Context context, SavedListData list, OnListEditListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_create_list, null);

        EditText nameInput = view.findViewById(R.id.input_list_name);
        GridLayout colorGrid = view.findViewById(R.id.color_grid);
        Button createButton = view.findViewById(R.id.button_create);
        ImageButton backButton = view.findViewById(R.id.arrow_back);

        nameInput.setText(list.list_name);

        List<String> colorOptions = getColorOptions();
        final String[] selectedColor = {list.color};

        populateColorGrid(context, colorGrid, colorOptions, selectedColor[0], color -> selectedColor[0] = color);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();

        createButton.setText("Guardar");
        createButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (!name.isEmpty()) {
                listener.onEdit(list.list_id, name, selectedColor[0]);
                dialog.dismiss();
            }
        });

        backButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(context, R.color.background));
    }

    private static List<String> getColorOptions() {
        return Arrays.asList(
                "#FFD1DC", // morango com creme
                "#FFECB3", // baunilha
                "#C1E1C1", // hortelã doce
                "#FAD6A5", // pêssego
                "#B2EBF2", // gelo de coco
                "#E0BBE4", // uva clara
                "#FFF0F5", // marshmallow
                "#F0EAD6", // farinha
                "#E6FFE9" // limão suave
        );
    }

    public interface OnColorSelected {
        void onColorSelected(String color);
    }

    private static void populateColorGrid(Context context, GridLayout grid, List<String> colors, String selected, OnColorSelected listener) {
        grid.removeAllViews();

        int size = (int) context.getResources().getDimension(R.dimen.icon_size_medium);
        int margin = (int) context.getResources().getDimension(R.dimen.margin_very_small);

        for (String hex : colors) {
            View box = new View(context);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = size;
            params.height = size;
            params.setMargins(margin, margin, margin, margin);
            box.setLayoutParams(params);

            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(15f);
            drawable.setColor(Color.parseColor(hex));
            drawable.setStroke(4, hex.equals(selected) ? ContextCompat.getColor(context, R.color.text_primary) : Color.TRANSPARENT);

            box.setBackground(drawable);

            box.setOnClickListener(v -> {
                populateColorGrid(context, grid, colors, hex, listener);
                listener.onColorSelected(hex);
            });

            grid.addView(box);
        }
    }
}
