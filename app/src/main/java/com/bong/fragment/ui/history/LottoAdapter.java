package com.bong.fragment.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bong.fragment.R;

import java.util.ArrayList;

public class LottoAdapter extends RecyclerView.Adapter<LottoAdapter.Holder> {

    Context context;
    private ArrayList<LottoNum> m_Datas;
    private LayoutInflater mInflate;

    public LottoAdapter(Context context, ArrayList<LottoNum> m_Data){
        this.context = context;
        this.mInflate = LayoutInflater.from(context);
        this.m_Datas = m_Data;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.fragment_history, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LottoAdapter.Holder holder, int position) {

        holder.LottoNum.setText(m_Datas.get(position).LottoNum);
        holder.WinNum.setText(m_Datas.get(position).WinNum);
    }

    @Override
    public int getItemCount() {
        return m_Datas.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        public TextView LottoNum;
        public TextView WinNum;

        public Holder(@NonNull View itemView) {
            super(itemView);
            LottoNum = (TextView)itemView.findViewById(R.id.tv_count);
            WinNum = (TextView)itemView.findViewById(R.id.tv_message);
        }
    }
}
