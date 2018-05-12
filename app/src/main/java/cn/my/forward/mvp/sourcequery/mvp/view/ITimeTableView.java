package cn.my.forward.mvp.sourcequery.mvp.view;

import java.util.List;

/**
 * Created by 123456 on 2018/3/2.
 * 课表的view
 */

public interface ITimeTableView {
    void showTimeTble(List nodes);  //展示课表

    void showTimeTbleError(String s);   //出现错误
}
