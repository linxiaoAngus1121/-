package cn.my.forward.mvp.sourcequery.mvp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.my.forward.R;

/**
 * Created by 123456 on 2018/4/30.
 */

public class MyListViewAdapter extends BaseAdapter {
    private List<String> mlist;
    private LayoutInflater inflater;

    public MyListViewAdapter(Context mContext, List<String> mlist) {
        this.mlist = mlist;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position).split("空");

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_scoure_showitem, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String[] item = (String[]) getItem(position);
        holder.tv_n.setText(item[0]);
        if (Float.valueOf(item[1]) < 60) {
            holder.tv_s.setTextColor(Color.RED);
            holder.tv_s.setText(item[1]);
        } else {
            holder.tv_s.setText(item[1]);
            //这里如果不设置为黑色的话，因为convertview的复用机制，会导致由于某一科小于60分，复用到其他位置，导致其他也比变成红色字体
            holder.tv_s.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tv_n;  //课程名
        TextView tv_s;  //课程成绩

        ViewHolder(View inflate) {
            tv_n = inflate.findViewById(R.id.name);
            tv_s = inflate.findViewById(R.id.score);
        }
    }
}
