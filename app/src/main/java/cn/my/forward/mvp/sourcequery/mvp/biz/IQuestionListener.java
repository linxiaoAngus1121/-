package cn.my.forward.mvp.sourcequery.mvp.biz;

import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.bean.QuestionBean;

/**
 * Created by 123456 on 2018/5/16.
 */

public interface IQuestionListener {
    void questionSuccess(List<QuestionBean> list);

    void questionError();
}
