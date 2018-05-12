package cn.my.forward.mvp.sourcequery.mvp.view;

import cn.my.forward.mvp.sourcequery.mvp.bean.BeanPerson;

/**
 * Created by 123456 on 2018/5/7.
 */

public interface IPersonView {
    void showData(BeanPerson person);

    void showDataError();

    void loading();
}
