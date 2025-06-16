package com.diogo.cookup.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.diogo.cookup.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FiltersBottomSheet extends BottomSheetDialogFragment {

    public interface OnFiltersAppliedListener {
        void onFiltersApplied(String filter, String difficulty, int maxTime, int maxIngredients);
    }

    private OnFiltersAppliedListener listener;

    public void setOnFiltersAppliedListener(OnFiltersAppliedListener listener) {
        this.listener = listener;
    }

    private RadioGroup radioGroupOrder;
    private CheckBox checkEasy, checkMedium, checkHard;
    private SeekBar seekMaxTime, seekMaxIngredients;
    private TextView textMaxTimeValue, textMaxIngredientsValue;
    private Button buttonApply, buttonCancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_filters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        radioGroupOrder = view.findViewById(R.id.radioGroupOrder);
        checkEasy = view.findViewById(R.id.checkEasy);
        checkMedium = view.findViewById(R.id.checkMedium);
        checkHard = view.findViewById(R.id.checkHard);
        seekMaxTime = view.findViewById(R.id.seekMaxTime);
        seekMaxIngredients = view.findViewById(R.id.seekMaxIngredients);
        textMaxTimeValue = view.findViewById(R.id.textMaxTimeValue);
        textMaxIngredientsValue = view.findViewById(R.id.textMaxIngredientsValue);
        buttonApply = view.findViewById(R.id.buttonApply);
        buttonCancel = view.findViewById(R.id.buttonCancel);

        Bundle args = getArguments();
        if (args != null) {
            String filter = args.getString("filter", "");
            String difficulty = args.getString("difficulty", "");
            int maxTime = args.getInt("maxTime", 0);
            int maxIngredients = args.getInt("maxIngredients", 0);

            if (filter.equals("recommended")) radioGroupOrder.check(R.id.radioRecommended);
            else if (filter.equals("popular")) radioGroupOrder.check(R.id.radioPopular);
            else if (filter.equals("recent")) radioGroupOrder.check(R.id.radioRecent);
            else if (filter.equals("week")) radioGroupOrder.check(R.id.radioWeek);

            if (difficulty.equals("Fácil")) checkEasy.setChecked(true);
            else if (difficulty.equals("Médio")) checkMedium.setChecked(true);
            else if (difficulty.equals("Difícil")) checkHard.setChecked(true);

            seekMaxTime.setProgress(maxTime);
            seekMaxIngredients.setProgress(maxIngredients);
        }

        textMaxTimeValue.setText(seekMaxTime.getProgress() + " min");
        textMaxIngredientsValue.setText(String.valueOf(seekMaxIngredients.getProgress()));

        seekMaxTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textMaxTimeValue.setText(progress + " min");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekMaxIngredients.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textMaxIngredientsValue.setText(String.valueOf(progress));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        buttonApply.setOnClickListener(v -> {
            String selectedFilter = getSelectedFilter();
            String selectedDifficulty = getSelectedDifficulty();
            int maxTime = seekMaxTime.getProgress();
            int maxIngredients = seekMaxIngredients.getProgress();

            if (listener != null) {
                listener.onFiltersApplied(selectedFilter, selectedDifficulty, maxTime, maxIngredients);
            }
            dismiss();
        });

        buttonCancel.setOnClickListener(v -> dismiss());
    }

    private String getSelectedFilter() {
        int selectedId = radioGroupOrder.getCheckedRadioButtonId();
        if (selectedId == R.id.radioRecommended) return "recommended";
        if (selectedId == R.id.radioPopular) return "popular";
        if (selectedId == R.id.radioRecent) return "recent";
        if (selectedId == R.id.radioWeek) return "week";
        return "";
    }

    private String getSelectedDifficulty() {
        if (checkEasy.isChecked()) return "Fácil";
        if (checkMedium.isChecked()) return "Médio";
        if (checkHard.isChecked()) return "Difícil";
        return "";
    }
}
