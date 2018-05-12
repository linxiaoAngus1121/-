package cn.my.forward.mvp.sourcequery.mvp.view;

import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.bean.ExamBean;

/**
 * Created by 123456 on 2018/4/2.
 * 期末考试查询的view,注意和等级考试view的区别
 */

public interface IExamView {
    void showExam(List<ExamBean> list);

    void showError(String s);
}
