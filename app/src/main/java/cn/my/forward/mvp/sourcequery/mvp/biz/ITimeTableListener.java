package cn.my.forward.mvp.sourcequery.mvp.biz;

import java.util.List;

/**
 * Created by 123456 on 2018/3/2.
 * 课表查询的回调
 */

public interface ITimeTableListener {
    void QueryTimeTableSuccess(List nodes);

    void QuertTimeTableFailure(String s);
}
