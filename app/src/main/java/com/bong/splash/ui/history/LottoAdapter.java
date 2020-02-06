package com.bong.splash.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bong.splash.R;

import java.util.List;

public class LottoAdapter extends BaseAdapter {
    ViewHolder holder;
    LayoutInflater inflater = null;
    private List<LottoNum> m_Data = null;
    private int cnt = 0;


    public LottoAdapter(List<LottoNum> Data) {
        m_Data = Data;
    }

    @Override
    public int getCount() {
        if (m_Data == null) return 0;

        return m_Data.size();
    }

    @Override
    public LottoNum getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //item_drwno Layout을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            final Context context = parent.getContext();
            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.item_drwno, parent, false);
            ViewHolder holder = new ViewHolder();

            holder.textView1 = (TextView)convertView.findViewById(R.id.Num);
            holder.textView2 = (TextView)convertView.findViewById(R.id.WinNum);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        LottoNum entry = m_Data.get(position);
//        LottoNum lottoNum = getItem(position);
        if(entry != null){

        }

        //item_drwno에 정의된 위젯에 대한 참조 획득
        TextView TextTitle = (TextView) convertView.findViewById(R.id.Num);
        TextView TextData = (TextView) convertView.findViewById(R.id.WinNum);

        TextTitle.setText(entry.LottoNum);
        TextData.setText(entry.WinNum);

        return convertView;
    }

    public static class ViewHolder{
        TextView textView1;
        TextView textView2;
    }
}
