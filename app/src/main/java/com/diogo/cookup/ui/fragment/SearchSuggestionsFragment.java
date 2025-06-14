package com.diogo.cookup.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.data.model.SearchData;
import com.diogo.cookup.ui.adapter.ExploreSearchAdapter;
import com.diogo.cookup.viewmodel.SearchViewModel;

public class SearchSuggestionsFragment extends Fragment {

    private EditText editTextSearch;
    private ImageView buttonBack;
    private RecyclerView recyclerSuggestions;
    private ExploreSearchAdapter adapter;
    private SearchViewModel viewModel;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_suggestions, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonBack = view.findViewById(R.id.buttonBack);
        recyclerSuggestions = view.findViewById(R.id.recyclerSuggestions);

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        adapter = new ExploreSearchAdapter(requireContext(), (selectedText, recipe) -> openSearchResultFragment(selectedText));

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                if (viewType == ExploreSearchAdapter.TYPE_RECIPE || viewType == ExploreSearchAdapter.TYPE_HEADER) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });

        recyclerSuggestions.setLayoutManager(layoutManager);
        recyclerSuggestions.setAdapter(adapter);

        if (getArguments() != null) {
            String currentQuery = SearchSuggestionsFragmentArgs.fromBundle(getArguments()).getQuery();
            editTextSearch.setText(currentQuery);
            editTextSearch.setSelection(currentQuery.length());
        }

        buttonBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        editTextSearch.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                final int DRAWABLE_END = 2;
                Drawable drawable = editTextSearch.getCompoundDrawables()[DRAWABLE_END];
                if (drawable != null) {
                    int drawableWidth = drawable.getBounds().width();
                    int[] location = new int[2];
                    editTextSearch.getLocationOnScreen(location);
                    int editTextRight = location[0] + editTextSearch.getWidth();
                    int touchX = (int) event.getRawX();
                    if (touchX >= (editTextRight - drawableWidth - editTextSearch.getPaddingEnd())) {
                        editTextSearch.setText("");
                        editTextSearch.requestFocus();
                        return true;
                    }
                }
            }
            return false;
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    viewModel.fetchSuggestions(query);
                } else {
                    adapter.setSuggestions(new SearchData());
                }
            }
        });

        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = editTextSearch.getText().toString().trim();
                if (!query.isEmpty()) openSearchResultFragment(query);
                return true;
            }
            return false;
        });

        editTextSearch.requestFocus();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 250);

        viewModel.getSearchResult().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getData() != null) {
                adapter.setSuggestions(response.getData());
            } else {
                adapter.setSuggestions(new SearchData());
            }
        });

        return view;
    }

    private void openSearchResultFragment(String query) {
        if (query == null || query.trim().isEmpty()) return;

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(
                SearchSuggestionsFragmentDirections.actionSearchSuggestionsFragmentToSearchResultFragment(query),
                new androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.searchSuggestionsFragment, true)
                        .build()
        );
    }
}
