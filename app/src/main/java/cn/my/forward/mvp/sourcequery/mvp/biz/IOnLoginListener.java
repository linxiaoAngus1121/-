package cn.my.forward.mvp.sourcequery.mvp.biz;

/**
 * Created by 123456 on 2018/2/9.
 */

public interface IOnLoginListener {
    void OnLoginSuccess(String username);

    void OnLoginError(String s);

}
