package cn.my.forward.mvp.sourcequery.mvp.biz;

import java.util.List;

/**
 * Created by 123456 on 2018/3/2.
 */

public interface ITimeTableListener {
    void QueryTimeTableSuccess(List<String> nodes);

    void QuertTimeTableFailure(String s);
}
