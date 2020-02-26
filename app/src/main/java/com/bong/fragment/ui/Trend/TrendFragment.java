package com.bong.fragment.ui.Trend;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bong.fragment.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TrendFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<LottoNum> data;
    private RecyclerView.LayoutManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.item_list, container, false);
        initDataset();
        Context context = rootView.getContext();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);

        LottoAdapter adapter = new LottoAdapter(context, data);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    private void initDataset(){
        Bundle extra = this.getArguments();
        ArrayList<Integer>pre = extra.getIntegerArrayList("pre");
        Map<String, Integer>cntMap = new HashMap<>();

        for(int i = 0; i < pre.size(); i++) {
            Integer cnt = (Integer) cntMap.get(pre.get(i).toString());

            int baseVal = (cnt != null) ? cnt.intValue() : 0;
            cntMap.put(pre.get(i).toString(), baseVal + 1);
        }
        convert(cntMap);
    }

    private void convert(Map<String, Integer>cntMap){
        // value 내림차순으로 정렬하고, value가 같으면 key 오름차순으로 정렬
        List<Map.Entry<String, Integer>>list = new LinkedList<>(cntMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                int comparision = (o1.getValue() - o2.getValue()) *  -1;

                return comparision == 0? o1.getKey().compareTo(o2.getKey()) : comparision;
            }
        });

        //순서 유지를 위해 LinkedHashMap을 사용
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for(Iterator<Map.Entry<String, Integer>> iter = list.iterator(); iter.hasNext();){
            Map.Entry<String, Integer> entry = iter.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        addlist(sortedMap);
    }

    private void addlist(Map<String, Integer> sortedMap){
        data = new ArrayList<>();
        Iterator<String> iter = sortedMap.keySet().iterator();
        while (iter.hasNext()) {
            try{

                String key = iter.next();
                int value = sortedMap.get(key);
                String val = Integer.toString(value);
                data.add(new LottoNum("No."+ key, val + " times"))  ;

                Log.e("fureun", "key = " + key + " value = " + value);

            }catch(Exception e){

            }
        }

    }
}
