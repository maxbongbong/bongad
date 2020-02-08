package com.bong.splash.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.bong.splash.R;
import com.bong.splash.room.AppDatabase;
import com.bong.splash.room.LottoDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class HistoryActivity extends AppCompatActivity {

    protected CompositeDisposable disposables;
    private ListView m_listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        disposables = new CompositeDisposable();
        result();

    }

    public void result(){
        Intent intent = getIntent();

        ArrayList<String> list = intent.getStringArrayListExtra("list");
        ArrayList<String> str = intent.getStringArrayListExtra("str");
        List<LottoNum> data = new ArrayList<>();

        for(int i = 50; i >= 0; i--){
            try{
                LottoNum item = new LottoNum();
                item.LottoNum = list.get(i);
                item.WinNum = str.get(i);
                data.add(item);
            }catch(Exception e){

            }

        }

        m_listView = (ListView)findViewById(R.id.list_view);
        ListAdapter adapter = new LottoAdapter(data);
        m_listView.setAdapter(adapter);

    }

}

