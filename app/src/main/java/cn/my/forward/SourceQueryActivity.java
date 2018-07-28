package cn.my.forward;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.User;
import cn.my.forward.mvp.sourcequery.mvp.adapter.MyScoureViewAdapter;
import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.utils.DynamicallyGenerateTitleUtil;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;
import cn.my.forward.mvp.sourcequery.mvp.view.ISourceView;

/**
 * 成绩查询
 */
public class SourceQueryActivity extends AppCompatActivity implements ISourceView {


    private SourcePresenter presenter = new SourcePresenter(this);

    private ProgressBar bar;
    private List<String> mlist;
    private MyScoureViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorequery);
        mlist = new ArrayList<>();
        Spinner mSpinner = (Spinner) findViewById(R.id.scoure_query_sp);
        RecyclerView mLv = (RecyclerView) findViewById(R.id.scoure_data_lv);
        bar = (ProgressBar) findViewById(R.id.scpure_bar);
        String stuNo = User.getInstance().getStuNo();
        mSpinner.setAdapter(DynamicallyGenerateTitleUtil.initDataForAdapter(this, stuNo, true));
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //进度条
                if (bar != null) {
                    bar.setVisibility(View.VISIBLE);
                }
                //默认会选第一个，所以在这里自动发起请求
                String parentItemAtPosition = (String) parent.getItemAtPosition(position);
                presenter.scoureQuery(parentItemAtPosition, position);//学期成绩查询
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mLv.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线颜色
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycle_divider));
        //适配器这里
        adapter = new MyScoureViewAdapter(this, mlist);
        mLv.addItemDecoration(divider);
        mLv.setAdapter(adapter);
    }

    @Override
    public void showSource(ArrayList<String> list) {
        MyLog.i("到这里了");
        bar.setVisibility(View.GONE);
        mlist.clear();
        mlist.addAll(list);
        adapter.notifyDataSetChanged(); //更新数据源，提示listview刷新
        if (list.size() == 0) {
            Toast.makeText(this, R.string.sourcenodata, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSourceError() {
        bar.setVisibility(View.GONE);
        Toast.makeText(this, R.string.query_error, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.i("走了成绩查询的destroy");
        presenter.clearAll();
        presenter = null;
        mlist = null;
        adapter = null;
    }
}