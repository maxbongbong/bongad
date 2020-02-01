package com.bong.splash.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bong.splash.R;

import java.util.ArrayList;

public class LottoAdapter extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<LottoNum> m_oData = null;
    private int cnt = 0;

    public LottoAdapter(ArrayList<LottoNum> _oData) {
        m_oData = _oData;
        cnt = m_oData.size();
    }

    @Override
    public int getCount() {
        return cnt;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            final Context context = parent.getContext();
            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.item_drwno, parent, false);

        }
        LottoNum entry = m_oData.get(position);
        if(entry != null){

        }
        TextView TextTitle = (TextView) convertView.findViewById(R.id.Num);
        TextView TextData = (TextView) convertView.findViewById(R.id.WinNum);

        TextTitle.setText(m_oData.get(position).LottoNum);
        TextData.setText(m_oData.get(position).WinNum);
        return convertView;
    }
}
