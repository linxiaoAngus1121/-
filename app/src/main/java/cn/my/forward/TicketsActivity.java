package cn.my.forward;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.my.forward.mvp.sourcequery.mvp.adapter.MyAdapterForTicket;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_SpareTicket;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_ticket;
import cn.my.forward.mvp.sourcequery.mvp.presenter.SourcePresenter;
import cn.my.forward.mvp.sourcequery.mvp.view.ITicketsView;

public class TicketsActivity extends AppCompatActivity implements ITicketsView, View
        .OnClickListener {
    private EditText mEdit_from;
    private EditText mEdit_to;
    private TextView mTv_time;
    private TextView mTv_date;
    private RecyclerView mShow_data;
    private SourcePresenter presenter = new SourcePresenter(this);
    private ProgressBar mpb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        bindViews();
    }


    private void bindViews() {
        mEdit_from = findViewById(R.id.edit_from);
        mEdit_to = findViewById(R.id.edit_to);
        mTv_time = findViewById(R.id.train_time);
        mTv_date = findViewById(R.id.train_pre_day);
        mShow_data = findViewById(R.id.show_data);
        mpb = findViewById(R.id.ticket_pb);
        ImageView swap_iv = findViewById(R.id.swap_iv);
        findViewById(R.id.confirm).setOnClickListener(this);
        swap_iv.setOnClickListener(this);
        mShow_data.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线颜色
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycle_divider));
        mShow_data.addItemDecoration(divider);
        getDate();
    }

    /**
     * 为页面上的tv设置时间数据
     */
    private void getDate() {
        String s[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六",};
        Date day = new Date();
        Calendar can = Calendar.getInstance();
        can.setTime(day);
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        int i = can.get(Calendar.DAY_OF_WEEK);
        mTv_date.setText(df.format(day) + "  " + s[i - 1]);
      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                while (isflag) {
                    try {
                        Thread.sleep(1);
                        if (handler != null) {
                            Message message = handler.obtainMessage(0x110);
                            handler.sendMessage(message);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
    }


    @Override
    public void showTicketData(Bean_ticket bean_ticket, Bean_SpareTicket spareTicket) {
        if (mpb != null) {
            mpb.setVisibility(View.GONE);
        }
        MyAdapterForTicket adapter = new MyAdapterForTicket(spareTicket, bean_ticket,
                TicketsActivity
                        .this);
        mShow_data.setAdapter(adapter);

    }

    @Override
    public void showTicketError() {
        if (mpb != null) {
            mpb.setVisibility(View.GONE);
        }
        Toast.makeText(this, "查询出错啦，换个地址试试", Toast.LENGTH_SHORT).show();

    }

    @Override
    public String getFrom() {
        return mEdit_from.getText().toString();
    }

    @Override
    public String getTo() {
        return mEdit_to.getText().toString();
    }

    @Override
    public void loading() {
        if (mpb != null) {
            mpb.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                if (getFrom().equals("") || getTo().equals("")) {
                    Toast.makeText(this, "两个地点都要填哟", Toast.LENGTH_SHORT).show();
                    return;
                }
                presenter.ticket();
                toclosekeyboard();
                break;
            case R.id.swap_iv:
                String[] swap = presenter.swap(getFrom(), getTo());
                mEdit_from.setText(swap[0]);
                mEdit_to.setText(swap[1]);
                break;
        }
    }

    /**
     * 关闭软键盘
     */
    public void toclosekeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context
                .INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.clearAll();
        presenter = null;
    }


}
