package cn.my.forward.mvp.sourcequery.mvp.view;

import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.bean.QuestionBean;

/**
 * Created by 123456 on 2018/5/16.
 * 问卷调查的页面view
 */

public interface IQuestionView {
    void showSelect(List<QuestionBean> list);

    void showError();

    void submitSuccess();

    void submitError();
}
