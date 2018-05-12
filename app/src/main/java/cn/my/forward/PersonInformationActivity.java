package cn.my.forward;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.my.forward.mvp.sourcequery.mvp.bean.BeanPerson;
import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.utils.CharUtils;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;
import cn.my.forward.mvp.sourcequery.mvp.view.IPersonView;
import lecho.lib.hellocharts.view.ColumnChartView;

public class PersonInformationActivity extends AppCompatActivity implements IPersonView {

    private SourcePresenter presenter = new SourcePresenter(this);
    private ColumnChartView mColumnChartView;
    private ProgressBar mpb;
    private TextView mtv1, mtv2, mtv3, mtv5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        init();
    }

    private void init() {
        mColumnChartView = (ColumnChartView) findViewById(R.id.column_chart);
        mpb = (ProgressBar) findViewById(R.id.activity_information_pb);
        mtv1 = (TextView) findViewById(R.id.name);
        mtv2 = (TextView) findViewById(R.id.number);
        mtv3 = (TextView) findViewById(R.id.fdjh);
        mtv5 = (TextView) findViewById(R.id.orientation);
        presenter.person();
    }


    @Override
    public void showData(BeanPerson person) {
        MyLog.i(person.toString());
        generateData(person);
    }
    //BeanPerson{name='姓名：林晓', id='学号：1508030250', major='软件工程', college='学院：计算机科学与技术系',
    // orientation='专业方向:移动应用开发方向', theClass='行政班：15软件工程（2）班',
    // credit='所选学分118；获得学分118；重修学分0；正考未通过学分 0。', gradePoint='所有课程平均学分绩点：3.52'}

    /**
     * 生成图表
     *
     * @param person 实体信息
     */
    private void generateData(BeanPerson person) {
        CharUtils.getData(getApplicationContext(), person, mColumnChartView);
        mtv1.setText(person.getName());
        mtv2.setText(person.getId());
        mtv3.setText(person.getMajor());
        mtv5.setText(person.getOrientation());
        mtv5.setSingleLine(true);
        mtv5.setSelected(true);
        mtv5.setFocusable(true);
        mtv5.setFocusableInTouchMode(true);
        if (mpb != null) {
            mpb.setVisibility(View.GONE);
        }
    }


    @Override
    public void showDataError() {
        if (mpb != null) {
            mpb.setVisibility(View.GONE);
        }
        MyLog.i("展示数据失败");
    }

    @Override
    public void loading() {
        if (mpb != null) {
            mpb.setVisibility(View.VISIBLE);
        }
        MyLog.i("请等待");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.clearAll();
        presenter = null;

    }
}
