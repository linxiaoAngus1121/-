package cn.my.forward.mvp.sourcequery.mvp.biz;

import cn.my.forward.mvp.sourcequery.mvp.bean.BeanPerson;

/**
 * Created by 123456 on 2018/5/7.
 * 个人信息查询回调接口
 */

public interface IPersonListener {
    void onPersonQuerySuccess(BeanPerson person);

    void onPersonQueryError();
}
