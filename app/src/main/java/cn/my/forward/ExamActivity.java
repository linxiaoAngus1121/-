package cn.my.forward;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.adapter.MyRecycleViewAdapter;
import cn.my.forward.mvp.sourcequery.mvp.bean.ExamBean;
import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.view.IExamView;

public class ExamActivity extends AppCompatActivity implements IExamView {

    private SourcePresenter presenter = new SourcePresenter(this);
    private RecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
        String stuNo = getIntent().getStringExtra("stu_no");
        mSpinner.setAdapter(initDataForAdapter(stuNo));
        mRv = (RecyclerView) findViewById(R.id.exam_show_recycleView);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //从0开始
                Log.i("000", position + "选择的位置");
                String parentItemAtPosition = (String) parent.getItemAtPosition(position);
                presenter.examQuery(parentItemAtPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

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
        return list;
    }

    @Override
    public void showExam(List<ExamBean> list) {
        if (list.size() == 0) {
            this.showError("嗷了个嗷，出错了");
            return;
        }
        Toast.makeText(this, "查询成功" + list.size(), Toast.LENGTH_SHORT).show();
        MyRecycleViewAdapter adapter = new MyRecycleViewAdapter(this, list);
        mRv.setAdapter(adapter);
    }

    @Override
    public void showError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}