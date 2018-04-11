package cn.my.forward.mvp.sourcequery.mvp.view;

import java.util.ArrayList;

import cn.my.forward.mvp.sourcequery.mvp.bean.LevelBean;

/**
 * Created by 123456 on 2018/4/9.
 * 等级考试查询的view
 */

public interface ILevealView {


    /**
     * 展示等级考试数据
     * @param been
     */
    void showLevelData(ArrayList<LevelBean> been);

    /**
     * 展示等级考试数据错误
     *
     * @param s 错误信息
     */
    void showLevelDataError(String s);
}
