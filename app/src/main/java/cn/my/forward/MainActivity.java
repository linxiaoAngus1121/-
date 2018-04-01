package cn.my.forward;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_s;
import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.view.ILoginView;

public class MainActivity extends BaseActivity implements ILoginView, View
        .OnClickListener {
    private EditText editText;  //验证码
    private EditText mEditnum;  //学号
    private EditText mEditPass; //密码
    private ImageView miv;
    private TextView mtv;
    private SourcePresenter presenter = new SourcePresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
        mtv = (TextView) findViewById(R.id.show);
        mEditnum = (EditText) findViewById(R.id.ed_num);
        mEditPass = (EditText) findViewById(R.id.edit_password);
        miv = (ImageView) findViewById(R.id.showCode);
        Button mButton = (Button) findViewById(R.id.finalto);
        editText = (EditText) findViewById(R.id.edit_ps);
        miv.setOnClickListener(this);
        mButton.setOnClickListener(this);

        //预备登陆
        presenter.prepareLogin();
    }

    @Override
    public String getstudNo() {
        return mEditnum.getText().toString();
    }

    @Override
    public String getstuPs() {
        return mEditPass.getText().toString();
    }

    @Override
    public String getCode() {
        return editText.getText().toString();
    }

    @Override
    public void showSource(ArrayList<Bean_s> list) {

    }

    @Override
    public void showSourceError(String s) {

    }


    @Override
    public void showCode(InputStream inputStream) {
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        miv.setImageBitmap(bitmap);
    }

    @Override
    public void showCodeError(String s) {
        Toast.makeText(this, "获取验证码失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showViewStateError(String s) {
        Toast.makeText(this, "获取viewstate失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginSuccess() {
        mtv.setText("登录成功");
        Intent intent = new Intent(MainActivity.this, ChoiseActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoginError() {
        mtv.setText("登录失败");
    }

    @Override
    public void closekeyboard() {
        toclosekeyboard();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showCode: //验证码
                presenter.changeCode();
                break;

            case R.id.finalto:  //登录按钮
                presenter.login();
                break;

            default:
                break;
        }
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
}
