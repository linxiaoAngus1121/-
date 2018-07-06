package cn.my.forward.mvp.sourcequery.mvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.my.forward.R;
import cn.my.forward.mvp.sourcequery.mvp.bean.QuestionBean;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;

/**
 * Created by 123456 on 2018/5/17.
 * 问卷调查的适配器（未实现）
 */

public class MyRecyleViewAdapterForQuesion extends RecyclerView
        .Adapter<MyRecyleViewAdapterForQuesion.MyHolder> {
    private List<QuestionBean> list;
    private Context mContext;
    private List<String> mlist; //选中的选项


    public MyRecyleViewAdapterForQuesion(Context context, List<QuestionBean> list) {
        this.list = list;
        mContext = context;
        mlist = new ArrayList<>();
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_question_rvitem,
                parent, false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        holder.title.setText(list.get(position).getQuestion());
        holder.rg.removeAllViews(); //这里要清除rg里面已经存在的view，不然由于复用的机制，这里会出现错误
        MyLog.i("当前的postion" + position);
        final int answersize = list.get(position).getAnswer().size();
        for (int i = 0; i < answersize; i++) {
            final RadioButton mbt = new RadioButton(mContext);
            mbt.setText(list.get(position).getAnswer().get(i));
            if (list.get(position).getLocation() == i) {
                boolean flag = list.get(position).isFlag();
                if (flag) {
                    mbt.setChecked(true);
                } else {
                    mbt.setChecked(false);
                }
            }
            final int finalI = i;
            holder.rg.addView(mbt);
            mbt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  /*  for (int t = 0; t < holder.rg.getChildCount(); t++) {
                        RadioButton radioButton = (RadioButton) holder.rg.getChildAt(t);
                        if (radioButton.isChecked()) {
                            MyLog.i("走了continue");
                            break;
                        }
                        radioButton.setChecked(false);
                    }*/
                    if (isChecked) {
                        MyLog.i("走了ischcked....");
                        list.get(position).setLocationAndFlag(finalI, true);
                        mlist.add(mbt.getText().toString());
                    } else {
                        MyLog.i("走了ischcked.  false...");
                        list.get(position).setLocationAndFlag(finalI, false);
                    }
                }
            });
        }

    }

    public List<String> getMlist() {
        if (mlist.size() < list.size() || mlist.size() > list.size()) { //出现这中情况，证明用户还没有选择完就提交了,
            // 此时应该拒绝提交,或者用户在里面切换了选项，这一点是bug，后期修复
            return null;
        }
        return mlist;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView title;
        RadioGroup rg;

        private MyHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            rg = itemView.findViewById(R.id.rg);
        }
    }


}
