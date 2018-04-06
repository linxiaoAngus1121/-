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
                    case 2:
                        mIntent = new Intent(ChoiseActivity.this, ExamActivity.class);
                        String stuNo = getIntent().getStringExtra("stu_no");
                        mIntent.putExtra("stu_no", stuNo);
                        break;
                    default:
                        break;
                }

                if (mIntent != null) {
                    startActivity(mIntent);
                }
            }
        });

        initAdapter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIntent = null;
    }


    private void initAdapter() {
        MyAdapter adapter = new MyAdapter(ChoiseActivity.this);
        mGridView.setAdapter(adapter);
    }

}
