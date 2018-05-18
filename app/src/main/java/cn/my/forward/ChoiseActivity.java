package cn.my.forward;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import cn.my.forward.adapter.MyAdapter;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;
import cn.my.forward.service.LoginService;

public class ChoiseActivity extends AppCompatActivity {

    private Intent mIntent;
    private GridView mGridView;
    private long mExitTime;
    private Bean_l extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise);
        mGridView = (GridView) findViewById(R.id.choise_grid);
        TextView tv = (TextView) findViewById(R.id.choise_showName_tv);
        extra = getIntent().getParcelableExtra("information");
        tv.append(extra.getName());
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String no = extra.getStuNo();
                Log.i("000", "当前的postion" + position);
                switch (position) {
                    case 0:
                        mIntent = new Intent(ChoiseActivity.this, SourceQueryActivity.class);
                        mIntent.putExtra("stu_no", no);
                        break;
                    case 1:
                        mIntent = new Intent(ChoiseActivity.this, TimeTableActivity.class);
                        break;
                    case 2:
                        mIntent = new Intent(ChoiseActivity.this, ExamActivity.class);
                        mIntent.putExtra("stu_no", no);
                        break;
                    case 3:
                        mIntent = new Intent(ChoiseActivity.this, LevelActivity.class);
                        break;
                    case 4:
                        mIntent = new Intent(ChoiseActivity.this, PersonInformationActivity.class);
                        break;
                    case 5:
                        //   mIntent = new Intent(ChoiseActivity.this, QuestionSurveyActivity
                        // .class);
                        Toast.makeText(ChoiseActivity.this, "功能即将开放，请客官稍等", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case 6:
                        Toast.makeText(ChoiseActivity.this, "功能即将开放，请客官稍等", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case 7:
                        Toast.makeText(ChoiseActivity.this, "功能即将开放，请客官稍等", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case 8:
                        Toast.makeText(ChoiseActivity.this, "功能即将开放，请客官稍等", Toast.LENGTH_SHORT)
                                .show();
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


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(ChoiseActivity.this, LoginService.class);
        intent.putExtra("information", extra);
        startService(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(ChoiseActivity.this, "再按一次退出教务系统", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    private void initAdapter() {
        MyAdapter adapter = new MyAdapter(ChoiseActivity.this);
        mGridView.setAdapter(adapter);
    }

}
