package cn.my.forward.mvp.sourcequery.mvp.utils;

import android.util.Log;

/**
 * Created by 123456 on 2018/4/15.
 * 自定义Log类
 */

public class MyLog {
    private static final String TAG = "000";
    private static boolean isDebug = false;

    public static void i(String s) {
        if (isDebug) {
            Log.i(TAG, s);
        }
    }

    public static void d(String s) {

        if (isDebug) {
            Log.d(TAG, s);
        }
    }

    public static void e(String s) {
        if (isDebug) {
            Log.e(TAG, s);

        }
    }

    public static void w(String s) {
        if (isDebug) {
            Log.w(TAG, s);
        }

    }

    public static void v(String s) {
        if (isDebug) {
            Log.v(TAG, s);
        }

    }

}
