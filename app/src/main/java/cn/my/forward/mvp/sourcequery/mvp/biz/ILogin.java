package cn.my.forward.mvp.sourcequery.mvp.biz;


import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;

/**
 * Created by 123456 on 2018/2/9.
 * 一个登录的方法
 * 一个查询成绩的方法
 * 一个登录的准备方法，主要是拼接登录所需要的信息
 * 一个课表查询方法
 */

public interface ILogin {

    void login(Bean_l bean, IOnLoginListener listener);

    void prepareLogin(IGetCodeListtener listener);

    void score(IOnQuerySourceListener querySourceListener);

    void timeTable(ITimeTableListener listener);
}
