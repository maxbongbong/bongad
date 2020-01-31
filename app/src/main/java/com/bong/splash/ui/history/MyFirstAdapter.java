package com.bong.splash.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.bong.splash.R;
import com.bong.splash.data.Lotto;
import com.bong.splash.ui.main.MainPageActivity;
import com.bong.splash.ui.splash.SplashActivity;

import java.util.ArrayList;
import java.util.List;


public class MyFirstAdapter extends BaseAdapter {

    private  final List mData;

    //List를 구현한 모든 것(Arraylist 등)을 받는 생성자
    public MyFirstAdapter(List<LottoNum> data){
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_drwno, parent, false);

            //로또 회차 번호
            TextView Num = (TextView)convertView.findViewById(R.id.Num);

            //로또 추첨 번호
            TextView Win = (TextView)convertView.findViewById(R.id.WinNum);

            holder.Num = Num;
            holder.Win = Win;

        }else{
            holder = (ViewHolder)convertView.getTag();
        }

//        LottoView lottoView = (LottoView) mData;
//        String str = String.valueOf(lottoView);
        holder.Num.setText("1");

        holder.Win.setText("3");

        return convertView;

    }
    static class ViewHolder {
        TextView Num;
        TextView Win;
    }

    //String 형식으로 형변환
    private String convertIntoString(List<Integer> change) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < change.size(); i++) {
            if (sb.length() > 0) {
                if (i == change.size() - 1) {
                    sb.append(" + ");
                } else {
                    sb.append(", ");
                }
            }
            sb.append(change.get(i));
        }
        return sb.toString();
    }
}
