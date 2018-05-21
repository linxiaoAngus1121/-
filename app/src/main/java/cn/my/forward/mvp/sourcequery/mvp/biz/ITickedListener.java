package cn.my.forward.mvp.sourcequery.mvp.biz;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_SpareTicket;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_ticket;

/**
 * Created by 123456 on 2018/5/19.
 * 火车票查询
 */

public interface ITickedListener {
    void getDataSuccess(Bean_ticket ticket, Bean_SpareTicket spareTicket);

    void getDataError();
}
