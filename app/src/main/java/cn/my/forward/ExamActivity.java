package cn.my.forward;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.User;
import cn.my.forward.mvp.sourcequery.mvp.adapter.MyRecycleViewAdapter;
import cn.my.forward.mvp.sourcequery.mvp.bean.ExamBean;
import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.utils.DynamicallyGenerateTitleUtil;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;
import cn.my.forward.mvp.sourcequery.mvp.view.IExamView;

/**
 * 考试查询
 */
public class ExamActivity extends AppCompatActivity implements IExamView {

    private SourcePresenter presenter = new SourcePresenter(this);
    private RecyclerView mRv;
    private MyRecycleViewAdapter adapter;
    private List<ExamBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
        String stuNo = User.getInstance().getStuNo();
        mSpinner.setAdapter(DynamicallyGenerateTitleUtil.initDataForAdapter(this,stuNo,false));
        mRv = (RecyclerView) findViewById(R.id.exam_show_recycleView);
        mRv.setLayoutManager(new LinearLayoutManager(this));

        //上面是个spinner，这是点击事件
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //从0开始
                MyLog.i(position + "选择的位置");
                String parentItemAtPosition = (String) parent.getItemAtPosition(position);
                //开始请求
                presenter.examQuery(parentItemAtPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    //查询考试的数据成功了，在这里传给adapter
    @Override
    public void showExam(List<ExamBean> list) {
        MyLog.i(list.size() + "返回的list长度");
        Toast.makeText(this, "查询成功", Toast.LENGTH_SHORT).show();
        if (adapter != null) {
            MyLog.i("adapter不为空");
            mList.clear();
            mList.addAll(list);
            adapter.notifyDataSetChanged();
        } else {
            mList = new ArrayList<>();
            mList.addAll(list);
            //适配器
            adapter = new MyRecycleViewAdapter(this, mList);
            mRv.setAdapter(adapter);
        }
    }

    @Override
    public void showError(String s) {
        if (adapter != null) {
            mList.clear();
            adapter.notifyDataSetChanged();
        }
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.clearAll();
        presenter = null;
    }
}
