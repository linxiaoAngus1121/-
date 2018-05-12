package cn.my.forward;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import cn.my.forward.adapter.MyAdapter;

public class ChoiseActivity extends AppCompatActivity {

    private Intent mIntent;
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise);
        mGridView = (GridView) findViewById(R.id.choise_grid);
        TextView tv = (TextView) findViewById(R.id.choise_showName_tv);
        String stu_name = getIntent().getStringExtra("stu_name");
        tv.append(stu_name);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s_no = getIntent().getStringExtra("stu_no");
                Log.i("000", "当前的postion" + position);
                switch (position) {
                    case 0:
                        mIntent = new Intent(ChoiseActivity.this, SourceQueryActivity.class);
                        mIntent.putExtra("stu_no", s_no);
                        break;
                    case 1:
                        mIntent = new Intent(ChoiseActivity.this, TimeTableActivity.class);
                        break;
                    case 2:
                        mIntent = new Intent(ChoiseActivity.this, ExamActivity.class);
                        String stuNo = getIntent().getStringExtra("stu_no");
                        mIntent.putExtra("stu_no", stuNo);
                        break;
                    case 3:
                        mIntent = new Intent(ChoiseActivity.this, LevelActivity.class);
                        break;
                    case 4:
                        mIntent = new Intent(ChoiseActivity.this, PersonInformationActivity.class);
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
