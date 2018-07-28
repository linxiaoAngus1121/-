package cn.my.forward;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.my.forward.adapter.MyAdapter;
import cn.my.forward.mvp.sourcequery.mvp.User;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;
import cn.my.forward.service.LoginService;

/**
 * 登陆成功后进入的页面
 */
public class ChoiseActivity extends AppCompatActivity {

    private Intent mIntent;
    private GridView mGridView;
    private long mExitTime;
    private ArrayList<String> mlist; //存放用户进行了那些行为

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise);
        mGridView = (GridView) findViewById(R.id.choise_grid);
        TextView tv = (TextView) findViewById(R.id.choise_showName_tv);
        mlist = new ArrayList<>();
        initAdapter();
        tv.append(User.getInstance().getName());
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLog.i("当前的postion" + position);
                switch (position) {
                    case 0:
                        mIntent = new Intent(ChoiseActivity.this, SourceQueryActivity.class);
                        mlist.add(MyAdapter.title[0]);
                        break;
                    case 1:
                        mIntent = new Intent(ChoiseActivity.this, TimeTableActivity.class);
                        mlist.add(MyAdapter.title[1]);
                        break;
                    case 2:
                        mIntent = new Intent(ChoiseActivity.this, ExamActivity.class);
                        mlist.add(MyAdapter.title[2]);
                        break;
                    case 3:
                        mIntent = new Intent(ChoiseActivity.this, LevelActivity.class);
                        mlist.add(MyAdapter.title[3]);
                        break;
                    case 4:
                        mIntent = new Intent(ChoiseActivity.this, PersonInformationActivity.class);
                        mlist.add(MyAdapter.title[4]);
                        break;
                    case 5:     //问卷调查
                        //   mIntent = new Intent(ChoiseActivity.this, QuestionSurveyActivity
                        // .class);
                        mIntent = new Intent(ChoiseActivity.this, TicketsActivity
                                .class);
                        mlist.add(MyAdapter.title[5]);
                        break;
                    case 6:
                        mIntent = new Intent(ChoiseActivity.this, LePaiActivity
                                .class);
                        mlist.add(MyAdapter.title[6]);
                        break;
                    case 7: //退出系统
                        mIntent = new Intent(ChoiseActivity.this, MainActivity.class);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mIntent);
                        finish();
                        break;
                    case 8: //bug反馈
                        mIntent = new Intent(ChoiseActivity.this, FeedBackActivity
                                .class);
                        mlist.add(MyAdapter.title[8]);
                        break;
                    default:
                        break;
                }

                if (mIntent != null) {
                    startActivity(mIntent);
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mIntent = null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //先试试在这里进行上传
        upload();
    }

    private void upload() {
        Intent intentforward = new Intent(this, LoginService.class);
        intentforward.putStringArrayListExtra("list", mlist);
        startService(intentforward);
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
            Toast.makeText(ChoiseActivity.this, R.string.exit, Toast.LENGTH_SHORT).show();
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
