package cn.my.forward.mvp.sourcequery.mvp.view;

import java.io.InputStream;

/**
 * Created by 123456 on 2018/3/1.
 * 登陆功能的view
 */

public interface ILoginView {
    String getstudNo();

    String getstuPs();

    String getCode();

    void showCode(InputStream inputStream);

    void showCodeError(String s);

    void showViewStateError(String s);

    void showLoginSuccess(String name);

    void showLoginError();

    void closekeyboard();
}
