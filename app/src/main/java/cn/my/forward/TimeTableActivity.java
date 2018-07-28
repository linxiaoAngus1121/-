package cn.my.forward;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.adapter.MyBaseAdapter;
import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.utils.DynamicallyGenerateTitleUtil;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;
import cn.my.forward.mvp.sourcequery.mvp.view.ITimeTableView;

/**
 * 课表查询
 */
public class TimeTableActivity extends AppCompatActivity implements ITimeTableView, View
        .OnClickListener {

    private SourcePresenter presenter = new SourcePresenter(this);
    private GridView mGridView;
    private TextView mTv;
    private View view = null;
    private AlertDialog dialog;
    private static final String[] s = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11",
            "12", "13",
            "14", "15", "16", "17", "18", "19", "20"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        initview();
    }

    private void initview() {
        mTv = (TextView) findViewById(R.id.picker);
        mTv.setOnClickListener(this);
        mGridView = (GridView) findViewById(R.id.courceDetail);
        LinearLayout layout = (LinearLayout) findViewById(R.id.contain);
        DynamicallyGenerateTitleUtil.createTextView(this,layout);
        //课表查询第一周
        presenter.timetable(1);
    }


    //课表查询成功
    @Override
    public void showTimeTble(List nodes) {
        Toast.makeText(this, R.string.query_success, Toast.LENGTH_SHORT).show();
        //适配器
        MyBaseAdapter baseAdapter = new MyBaseAdapter(this, nodes);
        mGridView.setAdapter(baseAdapter);
    }

    @Override
    public void showTimeTbleError(String s) {
        Toast.makeText(this, R.string.query_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (view == null) {
            MyLog.i("view是空");
            view = LayoutInflater.from(this).inflate(R.layout.activity_dialog_picker, null);
            new ViewHolder(view);
        }
        if (dialog == null) {
            MyLog.i("dialog是空");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dialog = builder.setTitle(R.string.choiseweek)
                    .setView(view).create();
        }
        dialog.show();

    }

    private class ViewHolder {
        GridView mGd;

        private ViewHolder(View view) {
            mGd = view.findViewById(R.id.test);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(TimeTableActivity.this,
                    android.R.layout
                            .simple_list_item_1, s);
            mGd.setAdapter(adapter);
            mGd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long
                        id) {
                    MyLog.i("点击了" + s[position]);
                    mTv.setText("第" + s[position] + "周");
                    presenter.timetable(Integer.valueOf(s[position]));
                    dialog.dismiss();
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.clearAll();
        presenter = null;
    }
}
