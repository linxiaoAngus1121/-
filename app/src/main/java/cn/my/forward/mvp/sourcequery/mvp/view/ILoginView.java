package cn.my.forward.mvp.sourcequery.mvp.view;

import java.io.InputStream;

/**
 * Created by 123456 on 2018/3/1.
 */

public interface ILoginView extends ISourceView {
    @Override
    String getstudNo();

    @Override
    String getstuPs();

    @Override
    String getCode();

    @Override
    void showCode(InputStream inputStream);

    @Override
    void showCodeError(String s);

    @Override
    void showViewStateError(String s);

    @Override
    void showLoginSuccess();

    @Override
    void showLoginError();

    @Override
    void closekeyboard();
}
