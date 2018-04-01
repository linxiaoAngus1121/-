package cn.my.forward;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_s;
import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.view.ISourceView;

public class SourceQueryActivity extends AppCompatActivity implements ISourceView {

    private TextView mtv;
    private TableLayout mLayout;

    private SourcePresenter presenter = new SourcePresenter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorequery);
        mtv = (TextView) findViewById(R.id.scoure_show);
        mLayout = (TableLayout) findViewById(R.id.table);
        presenter.scoureQuery();
    }


    @Override
    public String getstudNo() {
        return null;
    }

    @Override
    public String getstuPs() {
        return null;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public void showSource(ArrayList<Bean_s> list) {
        if (list.size() == 0) {
            mtv.setText("查询成绩出错");
            return;
        }
        mtv.setText("查询成绩成功");


        for (int i = 0; i < list.size(); i++) {     //数据源
            TableRow tableRow = new TableRow(getApplicationContext());
            TextView textView = new TextView(getApplicationContext());
            TextView textView01 = new TextView(getApplicationContext());
            textView01.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setText(list.get(i).getClassName());
            textView01.setText(list.get(i).getScore());
            tableRow.addView(textView);
            tableRow.addView(textView01);
            mLayout.addView(tableRow);

        }

        mtv.setText("课表查询成功");


    }

    @Override
    public void showSourceError(String s) {
        //  mtv.setText(String.format("课表查询失败%s", s));
    }

    @Override
    public void showCode(InputStream inputStream) {

    }

    @Override
    public void showCodeError(String s) {

    }

    @Override
    public void showViewStateError(String s) {

    }

    @Override
    public void showLoginSuccess() {
    }

    @Override
    public void showLoginError() {
    }

    @Override
    public void closekeyboard() {

    }


}