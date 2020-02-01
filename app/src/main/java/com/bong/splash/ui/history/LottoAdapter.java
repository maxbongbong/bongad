package com.bong.splash.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.bong.splash.R;
import java.util.ArrayList;
import java.util.List;

public class LottoAdapter extends BaseAdapter {
    ViewHolder holder;
    LayoutInflater inflater = null;
    private ArrayList<LottoNum> m_Data = null;
    private int cnt = 0;
    private  Context context;
    private List<LottoNum> numList;


//    public LottoAdapter(Context context, List<LottoNum>numlist){
//        this.context = context;
//        this.numList = numlist;
//    }

    public LottoAdapter(Context context, ArrayList<LottoNum> Data) {
        this.context = context;
        m_Data = Data;
        cnt = m_Data.size();
    }

    @Override
    public int getCount() {
        return cnt;
    }
//    public int getCount() {
//        return numList.size();
//    }

    @Override
    public LottoNum getItem(int position) { return null; }
//    public LottoNum getItem(int i) { return numList.get(i); }

    @Override
    public long getItemId(int position) {
        return position;
    }
//    public long getItemId(int i) {
//        return i;
//    }

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

//    public void additem(String Num, String Win){
//        LottoNum lottoNum = new LottoNum();
//
//        lottoNum.setNum(Num);
//        lottoNum.setWin(Win);
//
//        m_Data.add(lottoNum);
//    }

//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        View v = View.inflate(context, R.layout.item_drwno, null);
//
//        //뷰에 다음 컴포넌트들을 연결시켜줌
//        TextView Num = (TextView)v.findViewById(R.id.Num);
//        TextView WinNum= (TextView)v.findViewById(R.id.WinNum);
//
//        Num.setText(numList.get(i).getNum());
//        WinNum.setText(numList.get(i).getWin());
//
//        //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임
////        v.setTag(numList.get(i).getWin());
//
//        //만든뷰를 반환함
//        return v;
//    }
}
