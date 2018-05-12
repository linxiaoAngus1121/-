package cn.my.forward;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.view.ILoginView;

public class MainActivity extends BaseActivity implements ILoginView, View
        .OnClickListener {
    private EditText editText;  //验证码
    private EditText mEditnum;  //学号
    private cn.my.forward.customview.MyPsEditText mEditPass; //密码
    private CheckBox mCheckBox; //复选框
    private ImageView miv;
    private Button mButton;
    private SourcePresenter presenter = new SourcePresenter(this);
    private static final String FILE_NAME = "MyPs";

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
        SharedPreferences preferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String no = preferences.getString("no", "");
        String ps = preferences.getString("ps", "");
        mEditnum = (EditText) findViewById(R.id.ed_num);
        mEditPass = (cn.my.forward.customview.MyPsEditText) findViewById(R.id.edit_password);
        miv = (ImageView) findViewById(R.id.showCode);
        mButton = (Button) findViewById(R.id.finalto);
        editText = (EditText) findViewById(R.id.edit_ps);
        mCheckBox = (CheckBox) findViewById(R.id.activity_main_remberps_cb);
        miv.setOnClickListener(this);
        mButton.setOnClickListener(this);
        mEditnum.setText(no);
        mEditPass.setText(ps);
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
    public void showSource(ArrayList<String> list) {

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
    public void showLoginSuccess(String name) {
        if (mCheckBox.isChecked()) {
            remberPs(this.getstudNo(), this.getstuPs());
        }
        Intent intent = new Intent(MainActivity.this, ChoiseActivity.class);
        intent.putExtra("stu_no", this.getstudNo());
        intent.putExtra("stu_name", name);
        startActivity(intent);
        finish();
    }

    /**
     * 记住密码功能
     *
     * @param s        学号
     * @param getstuPs 密码
     */
    private void remberPs(String s, String getstuPs) {
        SharedPreferences.Editor edit = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        edit.putString("no", s);
        edit.putString("ps", getstuPs);
        edit.apply();
    }

    @Override
    public void showLoginError() {
        Toast.makeText(this, "登录失败,请刷新验证码后重试", Toast.LENGTH_SHORT).show();
        mButton.setText("登陆");
        mButton.setClickable(true);
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
                if (TextUtils.isEmpty(this.getstudNo()) || TextUtils.isEmpty(this.getstuPs()) ||
                        TextUtils.isEmpty(this.getCode())) {
                    Toast.makeText(this, "不可以偷懒不填哟", Toast.LENGTH_SHORT).show();
                    return;
                }
                presenter.login();
                mButton.setText("正在登陆");
                mButton.setClickable(false);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.clearAll();
        presenter = null;
        finish();
    }

}
