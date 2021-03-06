package cn.my.forward.mvp.sourcequery.mvp.biz;


import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;

/**
 * Created by 123456 on 2018/2/9.
 * 一个登录的方法
 * 一个查询成绩的方法
 * 一个登录的准备方法，主要是拼接登录所需要的信息
 * 一个课表查询方法
 * 一个考试查询的方法
 */

public interface ILogin {

    void login(Bean_l bean, IOnLoginListener listener);

    void prepareLogin(IGetCodeListtener listener);

    void score(String year, int postion, IOnQuerySourceListener querySourceListener);

    void timeTable(int start, ITimeTableListener listener);

    void examQuery(String postion, IExamListener listener);

    void levelQuery(ILevelListener listener);

    void personInfomation(IPersonListener listener);

    //   void questionQuery(IQuestionListener listener);

    //  void questionSubmit(List<String> list, ISubmitListener listener);

    void lepai(String path, ILePaiListener lePaiListener);

    void tickets(String from, String to, ITickedListener listener);
}
