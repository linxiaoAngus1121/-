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
 * Created by 123456 on 2018/3/5.
 * 课表的adapter
 */

public class MyBaseAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<String> list;

    public MyBaseAdapter(Context context, List<String> list) {
        mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.table_grid_item, parent, false);
            viewHolder.mTv = convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!getItem(position).equals("空")) {
            viewHolder.mTv.setText((String) getItem(position));
            viewHolder.mTv.setTextColor(Color.WHITE);
            //变换颜色
            int rand = position % 7;
            switch (rand) {
                case 0:
                    viewHolder.mTv.setBackground(mContext.getResources().getDrawable(R.drawable
                            .grid_item_bg));
                    break;
                case 1:
                    viewHolder.mTv.setBackground(mContext.getResources().getDrawable(R.drawable
                            .bg_12));
                    break;
                case 2:
                    viewHolder.mTv.setBackground(mContext.getResources().getDrawable(R.drawable
                            .bg_13));
                    break;
                case 3:
                    viewHolder.mTv.setBackground(mContext.getResources().getDrawable(R.drawable
                            .bg_14));
                    break;
                case 4:
                    viewHolder.mTv.setBackground(mContext.getResources().getDrawable(R.drawable
                            .bg_15));
                    break;
                case 5:
                    viewHolder.mTv.setBackground(mContext.getResources().getDrawable(R.drawable
                            .bg_16));
                    break;
                case 6:
                    viewHolder.mTv.setBackground(mContext.getResources().getDrawable(R.drawable
                            .bg_17));
                    break;
                case 7:
                    viewHolder.mTv.setBackground(mContext.getResources().getDrawable(R.drawable
                            .bg_18));
                    break;
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView mTv;
    }
}
