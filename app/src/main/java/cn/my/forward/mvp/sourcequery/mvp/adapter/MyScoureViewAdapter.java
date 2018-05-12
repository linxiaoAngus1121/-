package cn.my.forward.mvp.sourcequery.mvp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.my.forward.R;

/**
 * Created by 123456 on 2018/4/30.
 * 成绩查询的适配器
 */

public class MyScoureViewAdapter extends RecyclerView.Adapter<MyScoureViewAdapter.ViewHolder> {
    private List<String> mlist;
    private LayoutInflater inflater;

    public MyScoureViewAdapter(Context mContext, List<String> mlist) {
        this.mlist = mlist;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_scoure_showitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String[] item = mlist.get(position).split("空");
        holder.tv_n.setText(item[0]);
        if (Float.valueOf(item[1]) < 60) {
            holder.tv_s.setTextColor(Color.RED);
            holder.tv_s.setText(item[1]);
        } else {
            holder.tv_s.setText(item[1]);
            //这里如果不设置为黑色的话，因为convertview的复用机制，会导致由于某一科小于60分，复用到其他位置，导致其他也比变成红色字体
            holder.tv_s.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_n;  //课程名
        TextView tv_s;  //课程成绩

        ViewHolder(View itemView) {
            super(itemView);
            tv_n = itemView.findViewById(R.id.name);
            tv_s = itemView.findViewById(R.id.score);
        }
    }

}
