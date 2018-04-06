package cn.my.forward.mvp.sourcequery.mvp.biz;

import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.bean.ExamBean;

/**
 * Created by 123456 on 2018/4/2.
 * 考试查询成绩回调
 */

public interface IExamListener {
    void showExamSuccess(List<ExamBean> list);

    void showExamError(String s);
}
