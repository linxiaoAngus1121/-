package cn.my.forward.mvp.sourcequery.mvp.biz;


import java.util.ArrayList;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_s;

/**
 * Created by 123456 on 2018/2/9.
 */

public interface IOnQuerySourceListener {
    void OnQuerySuccess(ArrayList<Bean_s> list);

    void OnError(String s);
}
