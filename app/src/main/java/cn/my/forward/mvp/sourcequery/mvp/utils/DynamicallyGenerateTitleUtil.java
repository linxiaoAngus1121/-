package cn.my.forward.mvp.sourcequery.mvp.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.my.forward.R;
import cn.my.forward.customview.MyTextView;

/**
 * Created by 123456 on 2018/7/20.
 * 动态生成头部数据
 */

public class DynamicallyGenerateTitleUtil {


    /**
     * 生成历年成绩spinner适配器所需要的数据
     *
     * @param context 上下文
     * @param stuNo   根据学号
     * @param flag    是否要添加历年成绩几个字，用于区分是历年成绩的还是考试查询的,true则添加，false则不添加
     */
    public static ArrayAdapter<String> initDataForAdapter(Context context, String stuNo, boolean flag) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout
                .simple_spinner_item, getList(stuNo.substring(0, 2), flag));
        adapter.setDropDownViewResource(R.layout.activity_exam_sp_dropdown);
        return adapter;
    }


    /**
     * 返回历年成绩的头部数据源
     *
     * @param stuNo 学号
     * @param flag  是否要添加历年成绩几个字，用于区分是历年成绩的还是考试查询的,true则添加，false则不添加
     * @return 返回数据源list
     */
    private static ArrayList<String> getList(String stuNo, boolean flag) {
        ArrayList<String> list = new ArrayList<>();
        if (flag) {
            list.add("历年成绩");
        }
        int j = Integer.valueOf(stuNo) + 2000;    //2015
        for (int i = j + 2; i >= j; i--) { //2018开始
            for (int h = 2; h > 0; h--) {
                String line = String.valueOf(i) + "-" + String.valueOf(i + 1) + "-" + String
                        .valueOf(h);
                list.add(line);
            }
        }
        return list;
    }





    /**
     * 动态生成头部周几的代码，自定义一个view，圈圈
     */
    public static void createTextView(Context context, LinearLayout layout) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams
                .MATCH_PARENT, 1.0f);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int i1 = calendar.get(Calendar.DAY_OF_WEEK);    //获取当前是周几
        for (int i = 1; i <= 6; i++) {
            MyTextView mTv = new MyTextView(context);
            if (i == i1 - 1) {
                mTv.isToday = true;         //自定义view中的属性
            }
            mTv.setLayoutParams(lp);
            mTv.setGravity(Gravity.CENTER);
            mTv.setText(String.valueOf(i));
            mTv.setTextSize(18);
            layout.addView(mTv);
        }
    }

}
