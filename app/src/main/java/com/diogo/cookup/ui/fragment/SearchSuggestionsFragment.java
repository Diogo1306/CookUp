package com.diogo.cookup.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogo.cookup.R;
import com.diogo.cookup.ui.adapter.ExploreSearchAdapter;
import com.diogo.cookup.viewmodel.SearchViewModel;

public class SearchSuggestionsFragment extends Fragment {

    private EditText editTextSearch;
    private RecyclerView recyclerSuggestions;
    private ExploreSearchAdapter suggestionAdapter;
    private SearchViewModel viewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextSearch.requestFocus();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 250);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_suggestions, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        recyclerSuggestions = view.findViewById(R.id.recyclerSuggestions);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        setupSuggestions();

        editTextSearch.requestFocus();

        editTextSearch.setFocusableInTouchMode(true);
        editTextSearch.requestFocus();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    Log.d("Suggestions", "🔎 Digitando: " + query);
                    viewModel.fetchSuggestions(query);
                }
            }
        });

        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = editTextSearch.getText().toString().trim();
                if (!query.isEmpty()) openSearchResultFragment(query);
                return true;
            }
            return false;
        });

        viewModel.getSearchResult().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getData() != null) {
                Log.d("Suggestions", "✅ Sugestões carregadas");
                suggestionAdapter.setSuggestions(response.getData());
            } else {
                Log.d("Suggestions", "⚠️ Nenhuma sugestão encontrada");
            }
        });

        return view;
    }

    private void setupSuggestions() {
        suggestionAdapter = new ExploreSearchAdapter(requireContext(), (selectedText, recipe) -> {
            openSearchResultFragment(selectedText);
        });
        recyclerSuggestions.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerSuggestions.setAdapter(suggestionAdapter);
    }

    private void openSearchResultFragment(String query) {
        NavController navController = NavHostFragment.findNavController(this);
        SearchSuggestionsFragmentDirections.ActionSearchSuggestionsFragmentToSearchResultFragment action =
                SearchSuggestionsFragmentDirections.actionSearchSuggestionsFragmentToSearchResultFragment(query);
        navController.navigate(action);
    }
}
