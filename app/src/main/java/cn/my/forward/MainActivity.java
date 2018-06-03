package cn.my.forward;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;
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
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + File
            .separator + "tupian.png";

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
        mEditnum = findViewById(R.id.ed_num);
        mEditPass = findViewById(R.id.edit_password);
        miv = findViewById(R.id.showCode);
        mButton = findViewById(R.id.finalto);
        editText = findViewById(R.id.edit_ps);
        mCheckBox = findViewById(R.id.activity_main_remberps_cb);
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
    public void showCode(InputStream inputStream) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            miv.setImageBitmap(bitmap);
            FileOutputStream outputStream = new FileOutputStream(FILE_PATH);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // recognition();
    }

  /*  private void recognition() {
        if (OCR.getInstance().getAccessToken() != null) {
            // 通用文字识别参数设置
            GeneralBasicParams param = new GeneralBasicParams();
            param.setDetectDirection(true);
            param.setImageFile(new File(FILE_PATH));
            //高精度版本
            OCR.getInstance().recognizeAccurateBasic(param, new OnResultListener<GeneralResult>() {
                @Override
                public void onResult(GeneralResult generalResult) {
                    // 调用成功，返回GeneralResult对象
                    StringBuilder sb = new StringBuilder();
                    for (WordSimple wordSimple : generalResult.getWordList()) {
                        // wordSimple不包含位置信息
                        sb.append(wordSimple.getWords());
                        editText.setText(wordSimple.getWords());
                    }
                    MyLog.i(sb.toString() + "***");
                }

                @Override
                public void onError(OCRError ocrError) {
                    MyLog.i("调用失败");
                }
            });
        }

    }*/

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
        Bean_l lo = new Bean_l();
        lo.setStuNo(this.getstudNo());
        lo.setName(name);
        lo.setStuPs(this.getstuPs());
        intent.putExtra("information", lo);
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
        Toast.makeText(this, "登录失败,请重试", Toast.LENGTH_SHORT).show();
        mButton.setText("登陆");
        editText.setText("");
        mButton.setClickable(true);
        presenter.changeCode();
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
    }

}
