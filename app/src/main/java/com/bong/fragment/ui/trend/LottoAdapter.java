package com.bong.fragment.ui.trend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bong.fragment.R;

import java.util.ArrayList;
import java.util.List;

public class LottoAdapter extends RecyclerView.Adapter<LottoAdapter.Holder> {

    private Context context;
    private List<LottoNum> m_Data = new ArrayList<>();
    private LayoutInflater mInflate;

    public LottoAdapter(Context context, ArrayList<LottoNum> m_Data) {
        this.context = context;
        this.mInflate = LayoutInflater.from(context);
        this.m_Data = m_Data;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.fragment_history, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.LottoNum.setText(m_Data.get(position).LottoNum);
        holder.WinNum.setText(m_Data.get(position).WinNum);
    }

    @Override
    public int getItemCount() {
        return m_Data.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        public TextView LottoNum;
        public TextView WinNum;

        public Holder(@NonNull View itemview){
            super(itemview);
            LottoNum = (TextView)itemview.findViewById(R.id.tv_count);
            WinNum = (TextView)itemview.findViewById(R.id.tv_message);
        }
    }
}
