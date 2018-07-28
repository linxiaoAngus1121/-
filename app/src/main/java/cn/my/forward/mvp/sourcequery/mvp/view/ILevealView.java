package cn.my.forward.mvp.sourcequery.mvp.view;

import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.bean.LevelBean;

/**
 * Created by 123456 on 2018/4/9.
 * 等级考试查询的view，注意和期末考试view的区别
 */

public interface ILevealView {


    /**
     * 展示等级考试数据
     *
     * @param been 返回的实体类对象
     */
    void showLevelData(List<LevelBean> been);

    /**
     * 展示等级考试数据错误
     *
     * @param s 错误信息
     */
    void showLevelDataError(String s);
}
