package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.diogo.cookup.R;
import com.diogo.cookup.utils.ThemeManager;

public class SettingsAppearanceFragment extends Fragment {

    private ImageView iconThemeLight, iconThemeDark, iconThemeSystem;
    private View btnThemeLight, btnThemeDark, btnThemeSystem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_appearance, container, false);

        setupViews(view);
        setupListeners();
        loadSavedTheme();

        return view;
    }

    private void setupViews(View view) {
        btnThemeLight = view.findViewById(R.id.button_theme_light);
        btnThemeDark = view.findViewById(R.id.button_theme_dark);
        btnThemeSystem = view.findViewById(R.id.button_theme_system);

        iconThemeLight = view.findViewById(R.id.icon_theme_light);
        iconThemeDark = view.findViewById(R.id.icon_theme_dark);
        iconThemeSystem = view.findViewById(R.id.icon_theme_system);

        view.findViewById(R.id.arrow_back).setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack()
        );
    }

    private void setupListeners() {
        btnThemeLight.setOnClickListener(v ->
                ThemeManager.setTheme(requireActivity(), AppCompatDelegate.MODE_NIGHT_NO)
        );

        btnThemeDark.setOnClickListener(v ->
                ThemeManager.setTheme(requireActivity(), AppCompatDelegate.MODE_NIGHT_YES)
        );

        btnThemeSystem.setOnClickListener(v ->
                ThemeManager.setTheme(requireActivity(), AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        );
    }

    private void loadSavedTheme() {
        int savedTheme = ThemeManager.getSavedTheme(requireContext());
        updateSelectedIcon(savedTheme);
    }

    private void updateSelectedIcon(int selectedTheme) {
        iconThemeLight.setImageResource(R.drawable.ic_radio_unselected);
        iconThemeDark.setImageResource(R.drawable.ic_radio_unselected);
        iconThemeSystem.setImageResource(R.drawable.ic_radio_unselected);

        if (selectedTheme == AppCompatDelegate.MODE_NIGHT_NO) {
            iconThemeLight.setImageResource(R.drawable.ic_radio_selected);
        } else if (selectedTheme == AppCompatDelegate.MODE_NIGHT_YES) {
            iconThemeDark.setImageResource(R.drawable.ic_radio_selected);
        } else {
            iconThemeSystem.setImageResource(R.drawable.ic_radio_selected);
        }
    }
}
