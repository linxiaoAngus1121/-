package cn.my.forward.mvp.sourcequery.mvp.biz;

import java.util.ArrayList;

import cn.my.forward.mvp.sourcequery.mvp.bean.LevelBean;

/**
 * Created by 123456 on 2018/4/6.
 * 等级考试监听回调
 */

public interface ILevelListener {
    void showResultSucceed(ArrayList<LevelBean> been);

    void showResultError(String s);
}
