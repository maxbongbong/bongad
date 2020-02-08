package com.bong.splash.ui.trend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.bong.splash.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;

public class TrendActivity extends AppCompatActivity {

    protected  CompositeDisposable disposables;
    private ListView m_listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);
        disposables = new CompositeDisposable();
        trend();
    }

    public void trend(){
        Intent intent = getIntent();

        ArrayList<Integer> pre = intent.getIntegerArrayListExtra("pre");
        Map<String, Integer>cntMap = new HashMap<>();

        for(int i = 0; i < pre.size(); i++){

            Integer cnt = (Integer)cntMap.get(pre.get(i).toString());

            int baseVal = (cnt != null) ? cnt.intValue() : 0 ;
            cntMap.put(pre.get(i).toString() , baseVal + 1);
        }

        convert(cntMap);
    }

    private void convert(Map<String, Integer> cntMap){

        // value 내림차순으로 정렬하고, value가 같으면 key 오름차순으로 정렬
        List<Map.Entry<String, Integer>> list = new LinkedList<>(cntMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                int comparision = (o1.getValue() - o2.getValue()) * -1;
                return comparision == 0? o1.getKey().compareTo(o2.getKey()) : comparision;
            }
        });

        //순서 유지를 위해 LinkedHashMap을 사용
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for(Iterator<Map.Entry<String, Integer>> iter = list.iterator(); iter.hasNext();){
            Map.Entry<String, Integer> entry = iter.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        Log.e("sot", "sortedMap = " + sortedMap);

        addlist(sortedMap);
    }

    private void addlist(Map<String, Integer> sortedMap){
        List<LottoNum> data = new ArrayList<>();
        Iterator<String> iter = sortedMap.keySet().iterator();
        while (iter.hasNext()) {

            try{

                String key = iter.next();
                int value = sortedMap.get(key);
                String val = Integer.toString(value);
                LottoNum item = new LottoNum();
                item.LottoNum = "No."+ key;
                item.WinNum = val + " times";

                data.add(item);

                Log.e("fureun", "key = " + key + " value = " + value);

            }catch(Exception e){

            }
        }

        m_listView = (ListView)findViewById(R.id.list_view);
        ListAdapter adapter = new LottoAdapter(data);
        m_listView.setAdapter(adapter);
    }
}
