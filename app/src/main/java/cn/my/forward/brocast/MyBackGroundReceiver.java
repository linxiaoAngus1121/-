package cn.my.forward.brocast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;
import cn.my.forward.service.LoginService;

public class MyBackGroundReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MyLog.i("收到了广播，即将进行数据上传");
        Bean_l bean_l = intent.getParcelableExtra("information");
        Intent intentforward = new Intent(context, LoginService.class);
        intentforward.putExtra("information", bean_l);
        context.startService(intent);
    }
}
