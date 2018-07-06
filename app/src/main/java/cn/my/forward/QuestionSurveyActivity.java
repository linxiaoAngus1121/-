package cn.my.forward;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.adapter.MyRecyleViewAdapterForQuesion;
import cn.my.forward.mvp.sourcequery.mvp.bean.QuestionBean;
import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;
import cn.my.forward.mvp.sourcequery.mvp.view.IQuestionView;

/**
 * 问卷调查（未实现）
 */
public class QuestionSurveyActivity extends AppCompatActivity implements IQuestionView {

    private SourcePresenter presenter = new SourcePresenter(this);
    private RecyclerView mrv;
    private MyRecyleViewAdapterForQuesion adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_survey);
        mrv = (RecyclerView) findViewById(R.id.showquestion_rc);
        mrv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycle_divider));
        mrv.addItemDecoration(divider);
        //  presenter.question();
    }

    @Override
    public void showSelect(List<QuestionBean> list) {
        Toast.makeText(this, "list的sise" + list.size(), Toast.LENGTH_SHORT).show();
        adapter = new MyRecyleViewAdapterForQuesion(this, list);
        mrv.setAdapter(adapter);
    }

    @Override
    public void showError() {
        Toast.makeText(this, "好像出了点问题", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void submitSuccess() {
        Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void submitError() {
        Toast.makeText(this, "提交出错", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.clearAll();
        presenter = null;
    }


    public void submit(View view) {
        List<String> mlist = adapter.getMlist();//获得选中的东西
        if (mlist == null) {
            Toast.makeText(this, "不可以漏填哟", Toast.LENGTH_SHORT).show();
            return;
        }
        MyLog.i("选中了多少项" + mlist.size());
        //  presenter.questionSubmit(mlist);
    }
}
