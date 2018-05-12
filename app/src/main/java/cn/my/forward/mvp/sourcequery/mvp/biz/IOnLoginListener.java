package cn.my.forward.mvp.sourcequery.mvp.biz;

/**
 * Created by 123456 on 2018/2/9.
 * 登陆成功失败的回调
 */

public interface IOnLoginListener {
    void OnLoginSuccess(String username);

    void OnLoginError(String s);

}
