package cn.my.forward;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_s;
import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.view.ISourceView;

public class SourceQueryActivity extends AppCompatActivity implements ISourceView {

    private TableLayout mLayout;

    private SourcePresenter presenter = new SourcePresenter(this);

    private ProgressBar bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorequery);
        mLayout = (TableLayout) findViewById(R.id.table);
        bar = (ProgressBar) findViewById(R.id.scpure_bar);
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
        bar.setVisibility(View.GONE);
        if (list.size() == 0) {
            this.showCodeError("嗷了个嗷，出错了");
            return;
        }
        for (int i = 0; i < list.size(); i++) {     //数据源
            TableRow tableRow = new TableRow(getApplicationContext());
            TextView textView = new TextView(getApplicationContext());  //课程名称
            TextView textView01 = new TextView(getApplicationContext());    //课程成绩
            textView.setTextColor(Color.BLACK);
            textView01.setTextColor(Color.BLACK);
            textView01.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setText(list.get(i).getClassName());
            textView01.setText(list.get(i).getScore());
            tableRow.addView(textView);
            tableRow.addView(textView01);
            mLayout.addView(tableRow);

        }


    }

    @Override
    public void showSourceError(String s) {
        bar.setVisibility(View.GONE);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
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