package cn.my.forward.mvp.sourcequery.mvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.my.forward.R;
import cn.my.forward.mvp.sourcequery.mvp.bean.LevelBean;

/**
 * Created by 123456 on 2018/4/3.
 * 等级考试查询页面的Recycleview的adapter
 */

public class MyRecycleViewAdapterForLevel extends RecyclerView
        .Adapter<MyRecycleViewAdapterForLevel.ViewHolder> {

    private List<LevelBean> mList;
    private Context mContext;

    public MyRecycleViewAdapterForLevel(Context mContext, List<LevelBean> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_exam_recycle_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mtv_subject.setText(mList.get(position).getL_name());
        holder.mtv_time.setText(mList.get(position).getAdmissionTicket());
        holder.mtv_address.setText(mList.get(position).getGrade());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mtv_subject;
        TextView mtv_time;
        TextView mtv_address;

        ViewHolder(View itemView) {
            super(itemView);
            mtv_subject = itemView.findViewById(R.id.recycle_item_tv_subject);
            mtv_time = itemView.findViewById(R.id.recycle_item_tv_time);
            mtv_address = itemView.findViewById(R.id.recycle_item_tv_address);
        }
    }
}
