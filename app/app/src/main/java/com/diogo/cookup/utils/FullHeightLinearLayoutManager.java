package com.diogo.cookup.utils;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;

public class FullHeightLinearLayoutManager extends LinearLayoutManager {
    public FullHeightLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
