package cn.my.forward;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import cn.my.forward.adapter.MyAdapter;

public class ChoiseActivity extends AppCompatActivity {

    private Intent mIntent;
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise);
        mGridView = (GridView) findViewById(R.id.choise_grid);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("000", "当前的postion" + position);
                switch (position) {
                    case 0:
                        mIntent = new Intent(ChoiseActivity.this, SourceQueryActivity.class);
                        break;
                    case 1:
                        mIntent = new Intent(ChoiseActivity.this, TimeTableActivity.class);
                        break;
                    default:
                        break;
                }
                startActivity(mIntent);
            }
        });
        initAdapter();
    }

    private void initAdapter() {

        MyAdapter adapter = new MyAdapter(ChoiseActivity.this);
        mGridView.setAdapter(adapter);
    }


    /**
     * 课表查询
     *
     * @param view 视图
     */
    public void toTable(View view) {
        //跳转到另外一个Intent
        mIntent = new Intent(ChoiseActivity.this, TimeTableActivity.class);
        startActivity(mIntent);
    }

    /**
     * 成绩查询
     *
     * @param view 视图
     */
    public void toScore(View view) {
        mIntent = new Intent(ChoiseActivity.this, SourceQueryActivity.class);
        startActivity(mIntent);
    }
}
