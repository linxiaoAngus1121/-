package cn.my.forward.okhttp;


import java.util.Map;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 123456 on 2018/2/10.
 */

public class MyOkhttp {
    private static MyOkhttp instance;
    private OkHttpClient client;

    public static MyOkhttp getInstance() {
        if (instance == null) {
            synchronized (MyOkhttp.class) {
                if (instance == null) {
                    instance = new MyOkhttp();
                }
            }
        }
        return instance;
    }

    //防止被new
    private MyOkhttp() {
        client = new OkHttpClient();
    }

    /**
     * Get请求
     *
     * @param url      请求地址
     * @param callback 回调监听
     */
    public void GetRequest(String url, Callback callback) {
        Request re = buildGetRequestBody(url);
        client.newCall(re).enqueue(callback);
    }

    /**
     * Get请求
     *
     * @param url      请求地址
     * @param map      请求参数
     * @param callback 回调监听
     */
    public void GetRequest(String url, Map<String, String> map, Callback callback) {
        Request re = buildGetRequestBody(url, map);
        client.newCall(re).enqueue(callback);
    }


    /**
     * post请求
     *
     * @param url      请求地址
     * @param beanL    封装的实体类
     * @param map      头部信息
     * @param callback 回调监听
     */
    public void PostRequest(String url, Bean_l beanL, Map<String, String> map, Callback callback) {
        Request request = buildPostRequestBody(url, beanL, map);
        client.newCall(request).enqueue(callback);
    }

    /**
     * 构建post请求
     *
     * @param url      访问的地址
     * @param sub      需要的viewstate
     * @param map      请求头封装在此map中
     * @param callback 回调监听
     */
    public void PostRequest(String url, String sub, Map<String, String> map, Callback callback) {
        Request request = buildPostRequestBody(url, sub, map);
        client.newCall(request).enqueue(callback);
    }

    /**
     * 构建request对想
     *
     * @param url 访问的地址
     * @param sub 需要的viewstate
     * @param m   请求头信息
     * @return 构建好的request
     */
    private Request buildPostRequestBody(String url, String sub, Map<String, String> m) {
        FormBody formBody = buildBody(sub);
        Request.Builder post = new Request.Builder().url(url).post(formBody);
        for (Map.Entry<String, String> stringStringEntry : m.entrySet()) {
            Map.Entry element = (Map.Entry) stringStringEntry;
            String strKey = (String) element.getKey();
            String strObj = (String) element.getValue();
            post.header(strKey, strObj);
        }
        return post.build();
    }

    /**
     * 构建post请求
     *
     * @param url   访问的地址
     * @param beanL 需要拼接的信息
     * @param m     请求头封装在此map中
     * @return Request 返回构建好的request
     */
    private Request buildPostRequestBody(String url, Bean_l beanL, Map<String, String> m) {
        FormBody formBody = buildBody(beanL);
        Request.Builder post = new Request.Builder().url(url).post(formBody);
        for (Map.Entry<String, String> stringStringEntry : m.entrySet()) {
            Map.Entry element = (Map.Entry) stringStringEntry;
            String strKey = (String) element.getKey();
            String strObj = (String) element.getValue();
            post.header(strKey, strObj);
        }
        return post.build();
    }

    /**
     * 构建登录需要传入的参数
     *
     * @return body请求体
     */
    private FormBody buildBody(Bean_l bean) {
        return new FormBody.Builder()
                .add("__VIEWSTATE", bean.getViewState())
                .add("txtUserName", bean.getStuNo())
                .add("TextBox2", bean.getStuPs())
                .add("txtSecretCode", bean.getCode())
                .add("RadioButtonList1", "学生")
                .add("Button1", "")
                .add("lbLanguage", "")
                .add("hidPdrs", "")
                .add("hidsc", "")
                .build();
    }


    /**
     * 构建历年成绩查询需要传入的参数
     *
     * @param substring viewstate参数
     * @return body请请求体
     */
    private FormBody buildBody(String substring) {
        return new FormBody
                .Builder()
                .add("__EVENTTARGET", "")
                .add("__EVENTARGUMENT", "")
                .add("__VIEWSTATE", substring)
                .add("hidLanguage", "")
                .add("ddlXN", "")
                .add("ddlXQ", "")
                .add("ddl_kcxz", "")
                .add("btn_zcj", "历年成绩")
                .build();
    }


    /**
     * 不带参数的构建
     *
     * @param url url地址
     * @return 构建好的body
     */
    private Request buildGetRequestBody(String url) {
        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    /**
     * 带参数的构建
     *
     * @param url url地址
     * @param m   参数
     * @return 构建好的body
     */
    private Request buildGetRequestBody(String url, Map<String, String> m) {
        Request.Builder builder = new Request.Builder().get().url(url);
        for (Map.Entry<String, String> stringStringEntry : m.entrySet()) {
            Map.Entry element = (Map.Entry) stringStringEntry;
            String strKey = (String) element.getKey();
            String strObj = (String) element.getValue();
            builder.header(strKey, strObj);
        }
        return builder.build();
    }


}
