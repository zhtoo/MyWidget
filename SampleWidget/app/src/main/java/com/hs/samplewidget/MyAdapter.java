package com.hs.samplewidget;

/**
 * @author： Tom on 2017/11/5 15:36
 * @email： 820159571@qq.com
 *  
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<String> list = new ArrayList<>();

    public MyAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//           View view = LayoutInflater
//                   .from(MainActivity.this)
//                   .inflate(R.layout.item_home, parent, false);
        TextView view = new TextView(context);
        view.setHeight(150);
        view.setWidth(DensityUtils.getScreenWidth(context));
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setPadding(50, 0, 0, 0);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView view;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = (TextView) itemView;
        }

        public void setData(final int position) {
            view.setText(list.get(position));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public static onItemClickListener listener;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(int postion);
    }
}