package cn.my.forward;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.adapter.MyListViewAdapter;
import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.view.ISourceView;

public class SourceQueryActivity extends AppCompatActivity implements ISourceView {


    private SourcePresenter presenter = new SourcePresenter(this);

    private ProgressBar bar;
    private boolean isFirst = true;
    private ListView mLv;
    private List<String> mlist;
    private MyListViewAdapter adapter;

    /**
     * 生成spinner适配器所需要的数据
     *
     * @param stuNo 根据学号
     */
    private ArrayAdapter<String> initDataForAdapter(String stuNo) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, getList(stuNo.substring(0, 2)));
        adapter.setDropDownViewResource(R.layout.activity_exam_sp_dropdown);
        return adapter;
    }


    /**
     * 返回数据源
     *
     * @param stuNo 学好
     * @return 返回数据源list
     */
    private ArrayList<String> getList(String stuNo) {
        ArrayList<String> list = new ArrayList<>();

        int j = Integer.valueOf(stuNo) + 2000;    //2015
        for (int i = j + 2; i >= j; i--) { //2018开始
            for (int h = 2; h > 0; h--) {
                String line = String.valueOf(i) + "-" + String.valueOf(i + 1) + "-" + String
                        .valueOf(h);
                list.add(line);
            }
        }
        list.add("历年成绩");
        return list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorequery);
        mlist = new ArrayList<>();
        Spinner mSpinner = (Spinner) findViewById(R.id.scoure_query_sp);
        mLv = (ListView) findViewById(R.id.scoure_data_lv);
        bar = (ProgressBar) findViewById(R.id.scpure_bar);
        String stuNo = getIntent().getStringExtra("stu_no");
        mSpinner.setAdapter(initDataForAdapter(stuNo));
        presenter.scoureQuery("");//历年成绩查询
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //第一次进入的时候，这里会重复请求，所以要设置一个标志位，防止第一次请求出现问题
                if (isFirst && position == 0) {
                    isFirst = false;
                    return;
                }

                if (bar != null) {
                    bar.setVisibility(View.VISIBLE);
                }
                String parentItemAtPosition = (String) parent.getItemAtPosition(position);
                presenter.scoureQuery(parentItemAtPosition);//学期成绩查询

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = new MyListViewAdapter(this, mlist);
        mLv.setAdapter(adapter);
    }

    @Override
    public void showSource(ArrayList<String> list) {
        bar.setVisibility(View.GONE);
        mlist.clear();
        mlist.addAll(list);
        adapter.notifyDataSetChanged(); //更新数据源，提示listview刷新
        if (list.size() == 0) {
            Toast.makeText(this, "这学期还没有数据哟", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSourceError(String s) {
        bar.setVisibility(View.GONE);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}