package cn.my.forward;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import cn.my.forward.mvp.sourcequery.mvp.adapter.MyRecycleViewAdapterForLevel;
import cn.my.forward.mvp.sourcequery.mvp.bean.LevelBean;
import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.view.ILevealView;

/**
 * 等级考试的成绩（4.6级，AB 级）
 */

public class LevelActivity extends AppCompatActivity implements ILevealView {

    private ProgressBar mPb;
    private RecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        mPb = (ProgressBar) findViewById(R.id.level_pb);
        mRv = (RecyclerView) findViewById(R.id.level_show_recycle_view);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        SourcePresenter presenter = new SourcePresenter(this);
        presenter.LevelQuery();
        mPb.setVisibility(View.VISIBLE);
    }


    @Override
    public void showLevelData(ArrayList<LevelBean> been) {
        if (mPb != null) {
            mPb.setVisibility(View.GONE);
        }
        Toast.makeText(this, "请求成功", Toast.LENGTH_SHORT).show();
        MyRecycleViewAdapterForLevel adapter = new MyRecycleViewAdapterForLevel(this, been);
        mRv.setAdapter(adapter);
    }

    @Override
    public void showLevelDataError(String s) {
        if (mPb != null) {
            mPb.setVisibility(View.GONE);
        }
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}