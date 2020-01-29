package com.bong.splash.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bong.splash.R;

import java.util.List;

public class MyFirstAdapter extends BaseAdapter {
    private final List mData;
    public MyFirstAdapter(List data){
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drwno, parent, false);

    }
}
