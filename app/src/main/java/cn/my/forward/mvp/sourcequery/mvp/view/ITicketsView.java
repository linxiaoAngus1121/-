package cn.my.forward.mvp.sourcequery.mvp.view;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_SpareTicket;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_ticket;

/**
 * Created by 123456 on 2018/5/19.
 * 火车票查询的接口
 */

public interface ITicketsView {
    void showTicketData(Bean_ticket bean_ticket,Bean_SpareTicket spareTicket);

    void showTicketError();

    String getFrom();

    String getTo();

    void loading();

}
