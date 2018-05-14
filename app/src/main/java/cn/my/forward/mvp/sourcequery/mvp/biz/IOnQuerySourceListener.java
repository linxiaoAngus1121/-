package cn.my.forward.mvp.sourcequery.mvp.biz;


import java.util.ArrayList;

/**
 * Created by 123456 on 2018/2/9.
 * 成绩查询的回调
 */

public interface IOnQuerySourceListener {
    void OnQuerySuccess(ArrayList<String> list);

    void OnError(String s);
}
