package cn.my.forward.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.my.forward.R;

/**
 * Created by 123456 on 2018/3/19.
 * 主页的adapter
 */

public class MyAdapter extends BaseAdapter {

    private String[] title = {"成绩查询", "课表查询", "考试查询", "4，6级", "个人信息", "密码修改", "校园招聘", "火车汽车票",
            "景点门票"
    };
    private List<Drawable> picture = new ArrayList<>();
    private LayoutInflater from;


    public MyAdapter(Context context) {
        from = LayoutInflater.from(context);
        picture.add(context.getResources().getDrawable(R.drawable.picture));
        picture.add(context.getResources().getDrawable(R.drawable.pencil));
        picture.add(context.getResources().getDrawable(R.drawable.calendar));
        picture.add(context.getResources().getDrawable(R.drawable.four_six));
        picture.add(context.getResources().getDrawable(R.drawable.activity));
        picture.add(context.getResources().getDrawable(R.drawable.key));
        picture.add(context.getResources().getDrawable(R.drawable.job));
        picture.add(context.getResources().getDrawable(R.drawable.train));
        picture.add(context.getResources().getDrawable(R.drawable.ticket));

    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return picture.get(position);
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
            convertView = from.inflate(R.layout.adapter_grid_item, parent, false);
            holder.mTextView = convertView.findViewById(R.id.grid_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTextView.setText(title[position]);
        holder.mTextView.setCompoundDrawablesWithIntrinsicBounds(null, picture.get(position),
                null, null);

        return convertView;
    }

    private static class ViewHolder {
        TextView mTextView;
    }
}
