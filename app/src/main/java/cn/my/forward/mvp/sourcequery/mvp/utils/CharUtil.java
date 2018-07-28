package cn.my.forward.mvp.sourcequery.mvp.utils;

import android.graphics.Color;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.bean.BeanPerson;
import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by 123456 on 2018/5/10.
 * 图表工具类
 */

public class CharUtil {

    private final static String[] tip = new String[]{"所选学分", "获得学分", "重修学分",
            "未过学分", "平均绩点", "专业人数"};

    /**
     * 生成图表数据
     *
     * @param person           数据类
     * @param mColumnChartView 图表
     */
    public static void getData( BeanPerson person, ColumnChartView
            mColumnChartView) {
        mColumnChartView.setZoomEnabled(false);  //设置可以缩放
        int numSubcolums = 1;   //一个集合显示多少条柱子
        int numColums = tip.length;  //显示6个集合，分别对应所选学分，获得学分，重修学分，正考未通过学分，平均绩点，专业人数
        List<SubcolumnValue> values;    //每个柱子的信息
        List<AxisValue> axisXValues = new ArrayList<>();    //X轴上各个刻度的信息
        List<Column> columns = new ArrayList<>();           //记录所有柱子
        for (int i = 0; i < numColums; i++) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolums; j++) {
                try {
                    Float data = person.getList().get(i);
                    values.add(new SubcolumnValue(data, ChartUtils.pickColor()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            axisXValues.add(new AxisValue(i).setLabel(tip[i]));
            Column column = new Column(values);
            column.setHasLabels(true);                  //true默认会显示在柱子上方显示数据的大小
            //显示小数
            ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(2);
            column.setFormatter(chartValueFormatter);
            column.setHasLabelsOnlyForSelected(false);//设置是否点击后才会显示数据
            columns.add(column);                        //添加到所有数据的list中
        }
        ColumnChartData data = new ColumnChartData(columns);
        //setMaxLabelChars(就是x轴下面的那些字的多少，如果设置为8的话，会显示不全)
        //setHasTiltedLabels(false)不是倾斜的字体，true,字体倾斜
        data.setAxisXBottom(new Axis(axisXValues).setHasLines(true).setTextColor(Color.BLACK)
                .setHasTiltedLabels(false).setMaxLabelChars(10));
        data.setAxisYLeft(new Axis().setHasLines(false).setTextColor(Color.BLACK)
                .setMaxLabelChars(5));
        mColumnChartView.setColumnChartData(data);  //设置数据
        //下面的主要是实现了Y轴上的预留，不然Y轴每次都顶到最上面,
        // 第一个参数负得越大，柱子就会离原点越远（x轴方向），第二个参数如果越大，上面预留的就越多,第三个越大，就越往原点(即左边靠)
        // ，第四个参数越大，就会往Y轴的下面（即Y的负数部分）靠
        Viewport viewport = new Viewport(-0.8f, mColumnChartView.getMaximumViewport().height() *
                1.23f, 11, 0);
        mColumnChartView.setMaximumViewport(viewport);
        Viewport v = new Viewport(mColumnChartView.getMaximumViewport());
       v.left = 0;
       v.right = 7;
        mColumnChartView.setCurrentViewport(v);
        mColumnChartView.setVisibility(View.VISIBLE);
    }

}
