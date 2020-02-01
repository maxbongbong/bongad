package com.bong.splash.ui.history;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bong.splash.R;

public class LottoA extends AppCompatActivity {
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.recycleview);
        setTitle("로또");

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycle_view)
    }
}
