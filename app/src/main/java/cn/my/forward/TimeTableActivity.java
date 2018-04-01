package cn.my.forward;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.adapter.MyBaseAdapter;
import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.view.ITimeTableView;

public class TimeTableActivity extends AppCompatActivity implements ITimeTableView {

    private SourcePresenter presenter = new SourcePresenter(this);
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        mGridView = (GridView) findViewById(R.id.courceDetail);
        presenter.timetable();
    }


    @Override
    public void showTimeTble(List<String> nodes) {
        Toast.makeText(this, "查询成功" + nodes.size(), Toast.LENGTH_SHORT).show();
        for (String the : nodes) {
            Log.i("000", the);
        }
        MyBaseAdapter baseAdapter = new MyBaseAdapter(this, nodes);
        mGridView.setAdapter(baseAdapter);
    }


    @Override
    public void showTimeTbleError(String s) {
        Toast.makeText(this, "惨了，查询失败", Toast.LENGTH_SHORT).show();
    }
}
