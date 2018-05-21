package cn.my.forward.mvp.sourcequery.mvp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.my.forward.R;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_SpareTicket;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_SpareTicket_Result;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_ticket;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_ticketResult;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;

/**
 * Created by 123456 on 2018/5/19.
 * 火车票查询功能
 */

public class MyAdapterForTicket extends RecyclerView.Adapter<MyAdapterForTicket.MyHolder> {

    private static final String SEAT_TYPE = "seat_type";
    private static final String SEAT_MONEY = "seat_money";
    private static final String SEAT_REMAIN = "seat_remain";
    private static final String TYPE_ONE = "商务座";
    private static final String TYPE_TWO = "一等座";
    private static final String TYPE_THREE = "二等座";
    private static final String PLACEHOLDER = "--";
    private Bean_ticket ticket; //车票信息
    private Context mContext;
    private Bean_SpareTicket spareTicket;   //余票信息


    public MyAdapterForTicket(Bean_SpareTicket spareTicket, Bean_ticket ticket, Context mContext) {
        this.ticket = ticket;
        this.mContext = mContext;
        this.spareTicket = spareTicket;
        MyLog.i(ticket.getResult().size() + "我一个有这么多个");
        MyLog.i(spareTicket.getResult().size() + "余票有这么多个");
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.train_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        final Bean_ticketResult result = ticket.getResult().get(position);      //12
        final List<Bean_SpareTicket_Result> result1 = spareTicket.getResult();  //21
        int remain_size = result1.size();
        int clickpostion = -1;
        //这里要采用遍历的方式，理论上余票还有站站查询的列车是双双对应的，只是为了出现意外情况，所以采用这种
        for (int i = 0; i < remain_size; i++) {
            if (Objects.equals(result1.get(i).getTrainno(), result.getTrainno())) { //判断两者的车号是不是相同
                holder.mRemain_num.setText(result1.get(i).getEd());
                clickpostion = i;
                break;
            }
        }
        if (result == null) {
            Toast.makeText(mContext, "出了点小问题", Toast.LENGTH_SHORT).show();
            return;
        }
        final int finalClickpostion = clickpostion;
        final int finalClickpostion1 = clickpostion;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalClickpostion1 == -1) {
                    //如果是-1的话，就证明没找到这辆车的相关信息，
                    //      Toast.makeText(mContext, "没有这辆车的信息", Toast.LENGTH_SHORT).show();
                    showmore(null, result);
                } else {
                    showmore(result1.get(finalClickpostion), result);
                }
            }
        });
        holder.mFrom.setText(result.getStation());
        holder.mTo.setText(result.getEndstation());
        holder.mDuration.setText(result.getCosttime());
        holder.mStart_time.setText(result.getDeparturetime());
        holder.mEnd_time.setText(result.getArrivaltime());
        holder.mTrain_no.setText(result.getTrainno());
        if (java.util.Objects.equals(result.getPriceed(), "0.0") || java.util.Objects.equals
                (result.getPriceed(), "0") || null == result.getPriceed()) {
            holder.mSeat_price.setText(PLACEHOLDER);
            return;
        }
        holder.mSeat_price.setText(result.getPriceed());
    }

    private void showmore(Bean_SpareTicket_Result bean_spareTicket_result, Bean_ticketResult
            result) {
        //显示一个自定义的对话框，然后把那些一等座，商务座显示出来
        View view = LayoutInflater.from(mContext).inflate(R.layout.train_tickets_showmore, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(view).create();
        dialog.setCanceledOnTouchOutside(true);
        TextView confirm = view.findViewById(R.id.tickets_confirm);    //确定按钮
        TextView title = view.findViewById(R.id.ticker_info_title);    //谁谁谁的余票信息
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        title.setText(result.getTrainno() + "的余票信息如下：");
        final ListView ticketsList = view.findViewById(R.id.train_tickets);
        ArrayList<Map<String, String>> list = getMaps(bean_spareTicket_result, result);
        SimpleAdapter adapter = new SimpleAdapter(mContext, list, R.layout.dialog_view, new
                String[]{SEAT_TYPE, SEAT_MONEY, SEAT_REMAIN}, new int[]{R.id
                .dialog_item_seat_type, R.id.dialog_item_seat_money, R.id.dialog_item_seat_remain});
        ticketsList.setAdapter(adapter);
        dialog.show();
    }

    @NonNull
    private ArrayList<Map<String, String>> getMaps(Bean_SpareTicket_Result result1,
                                                   Bean_ticketResult result) {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put(SEAT_TYPE, TYPE_ONE);
        String sw1 = result.getPricesw();
        if (null == sw1 || "".equals(sw1)) {
            map.put(SEAT_MONEY, PLACEHOLDER);
        } else {
            map.put(SEAT_MONEY, sw1 + "元");
        }

        if (result1 != null) {
            String sw = result1.getSw();
            switch (sw) {
                case "有":
                case "无":
                    map.put(SEAT_REMAIN, sw);
                    break;
                case "--":
                    map.put(SEAT_REMAIN, "0张");
                    break;
                default:
                    map.put(SEAT_REMAIN, sw + "张");
                    break;
            }
        } else {
            map.put(SEAT_REMAIN, PLACEHOLDER);
        }

        list.add(map);


        //一等座
        Map<String, String> map1 = new HashMap<>();
        map1.put(SEAT_TYPE, TYPE_TWO);
        String sw2 = result.getPriceyd();
        if (null == sw2 || "".equals(sw2)) {
            map1.put(SEAT_MONEY, PLACEHOLDER);
        } else {
            map1.put(SEAT_MONEY, sw2 + "元");
        }
        if (result1 != null) {
            String yd = result1.getYd();
            switch (yd) {
                case "有":
                case "无":
                    map1.put(SEAT_REMAIN, yd);
                    break;
                case "--":
                    map1.put(SEAT_REMAIN, "0张");
                    break;
                default:
                    map1.put(SEAT_REMAIN, yd + "张");
                    break;
            }
        } else {
            map1.put(SEAT_REMAIN, PLACEHOLDER);
        }
        list.add(map1);

        Map<String, String> map2 = new HashMap<>();
        map2.put(SEAT_TYPE, TYPE_THREE);
        String priceed = result.getPriceed();
        if (null == priceed || "".equals(priceed)) {
            map2.put(SEAT_MONEY, PLACEHOLDER);
        } else {
            map2.put(SEAT_MONEY, priceed + "元");
        }
        if (result1 != null) {
            String ed = result1.getEd();
            switch (ed) {
                case "有":
                case "无":
                    map2.put(SEAT_REMAIN, ed);
                    break;
                case "--":
                    map2.put(SEAT_REMAIN, "0张");
                    break;
                default:
                    map2.put(SEAT_REMAIN, ed + "张");
                    break;
            }
        } else {
            map2.put(SEAT_REMAIN, PLACEHOLDER);
        }
        list.add(map2);

        return list;
    }


    @Override
    public int getItemCount() {
        return ticket.getResult().size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView mFrom;     //出发地
        TextView mTrain_no; //火车号
        TextView mStart_time;   //出发时间
        TextView mSeat_price;   //费用
        TextView mTo;       //目的地
        TextView mDuration;     //运行时长
        TextView mEnd_time;     //到达时间
        TextView mSeat;         //座位类型
        TextView mRemain_num;   //余票信息

        MyHolder(View itemView) {
            super(itemView);
            mFrom = itemView.findViewById(R.id.from);
            mTrain_no = itemView.findViewById(R.id.train_no);
            mStart_time = itemView.findViewById(R.id.start_time);
            mSeat_price = itemView.findViewById(R.id.seat_price);
            mTo = itemView.findViewById(R.id.to);
            mDuration = itemView.findViewById(R.id.duration);
            mEnd_time = itemView.findViewById(R.id.end_time);
            mSeat = itemView.findViewById(R.id.seat);
            mRemain_num = itemView.findViewById(R.id.remain_num);
        }
    }
}
