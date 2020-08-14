package com.bong.fragment.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bong.fragment.MainActivity;
import com.bong.fragment.R;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<LottoNum> data;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.item_list, container, false);
        initDataset();
        Context context = rootView.getContext();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);


        LottoAdapter adapter = new LottoAdapter(context, data);
        recyclerView.setAdapter(adapter);
        ((MainActivity)getActivity()).Toolbar(2);
        return rootView;
    }

    private void initDataset(){
        Bundle extra = this.getArguments();
        ArrayList<String>list = extra.getStringArrayList("list");
        ArrayList<String>str = extra.getStringArrayList("str");
        data = new ArrayList<>();
        for(int i = 49; i >= 0; i--) {
            data.add(new LottoNum(list.get(i), str.get(i)));
        }
    }
}
