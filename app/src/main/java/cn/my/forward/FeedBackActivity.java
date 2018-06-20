package cn.my.forward;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;
import cn.my.forward.service.AsyncTask;

/**
 * 意见反馈activity
 */
public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEt;
    private Button mBt;
    private Bean_l extra;
    private TextView mTv;   //展示剩余字数的
    private EditText mContactEd;    //联系方式的Editext
    private android.os.AsyncTask<String, Void, Void> task;
    private static final int MAX_SUM = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        mEt = (EditText) findViewById(R.id.content);
        mBt = (Button) findViewById(R.id.confirm_suggest);
        mTv = (TextView) findViewById(R.id.show_sum);
        mContactEd = (EditText) findViewById(R.id.contact);
        mBt.setOnClickListener(this);
        extra = getIntent().getParcelableExtra("information");
        mEt.addTextChangedListener(new TextWatcher() {  //监听上面字数的变化
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Editable editable = mEt.getText();
                int sum = s.length();
                if (sum > MAX_SUM) {
                    int end = Selection.getSelectionEnd(editable);
                    String substring = editable.toString().substring(0, MAX_SUM);
                    mEt.setText(substring);
                    editable = mEt.getText();
                    int newlenth = editable.length();
                    if (end > newlenth) {
                        end = newlenth;
                    }
                    Selection.setSelection(editable, end);
                    mTv.setText(MAX_SUM + "/140");
                } else {
                    mTv.setText(sum + "/140");
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        toclosekeyboard();
        if (TextUtils.isEmpty(mEt.getText().toString())) {
            Toast.makeText(this, "请输入您的意见建议哟", Toast.LENGTH_SHORT).show();
            return;
        }
        task = new AsyncTask(getApplicationContext(), mBt)
                .execute(mEt.getText().toString(), mContactEd.getText().toString(), extra.getName(),
                        extra.getStuNo(),
                        extra.getStuPs());
    }


    /**
     * 关闭软键盘
     */
    public void toclosekeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context
                .INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel(true);
        }
        task = null;
        extra = null;
    }
}
