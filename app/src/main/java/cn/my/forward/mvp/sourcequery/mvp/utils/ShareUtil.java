package cn.my.forward.mvp.sourcequery.mvp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 123456 on 2018/7/6.
 * SharedPreferences工具封装
 */

public class ShareUtil {
    private static final String SHARED_NAME = "MyPs";

    public static void putString(Context context, String key, String value) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences
                (SHARED_NAME, Context
                        .MODE_PRIVATE).edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getString(Context context, String key, String defaultvalue) {
        return context.getApplicationContext().getSharedPreferences(SHARED_NAME, Context
                .MODE_PRIVATE).getString(key, defaultvalue);
    }

}
