package cn.my.forward.okhttp;


import java.util.Map;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 123456 on 2018/2/10.
 * 请求工具封装
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


    public void PostQuestionRequest(String url, String viewstate, Map<String, String> map,
                                    String[] string, Callback
                                            callback) {
        Request request = buildRequest(url, viewstate, map, string);
        client.newCall(request).enqueue(callback);
    }


    private Request buildRequest(String url, String s, Map<String, String> m, String[] mlist) {
        if (mlist == null) {    //这种是请求填写调查问卷的
            FormBody body = new FormBody.Builder().add("__EVENTTARGET", "Datagrid4:_ctl2:link").add
                    ("__EVENTARGUMENT", "").add("__VIEWSTATE", s).build();
            Request.Builder post = new Request.Builder().url(url).post(body);
            for (Map.Entry<String, String> stringStringEntry : m.entrySet()) {
                Map.Entry element = (Map.Entry) stringStringEntry;
                String strKey = (String) element.getKey();
                String strObj = (String) element.getValue();
                post.header(strKey, strObj);
            }
            return post.build();
        } else {                    //这种是提交问卷调查的
            FormBody.Builder body = new FormBody.Builder().add("__EVENTTARGET", "").add
                    ("__EVENTARGUMENT", "").add("__VIEWSTATE", s);
            for (int i = 2; i <= 23; i++) {
                body.add("Datagrid1%3Actl" + i + "%3ARadioButtonList1", mlist[i]);
                MyLog.i("Datagrid1%3A_ctl" + i + "%3ARadioButtonList1" + "     内容为   " + mlist[i]);
            }
            body.add("Button1", "%CC%E1 %A1%A1%BD%BB");
            FormBody formBody = body.build();
            Request.Builder post = new Request.Builder().url(url).post(formBody);
            for (Map.Entry<String, String> stringStringEntry : m.entrySet()) {
                Map.Entry element = (Map.Entry) stringStringEntry;
                String strKey = (String) element.getKey();
                String strObj = (String) element.getValue();
                post.header(strKey, strObj);
            }
            return post.build();
        }
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
     * 构建post请求(历年成绩查询)
     *
     * @param url      访问的地址
     * @param map      请求头封装在此map中
     * @param callback 回调监听
     * @param sub      需要的viewstate,学年，学期信息
     */
    public void PostRequest(String url, Map<String, String> map, Callback callback, String... sub) {
        Request request = buildPostRequestBody(url, map, sub);
        if (request != null) {
            client.newCall(request).enqueue(callback);
        }
    }


    public void PostExamRequest(String url, String view, String[] s, Map<String, String> map,
                                Callback
                                        callback) {
        Request request = ExamPostRequest(url, view, s, map);
        client.newCall(request).enqueue(callback);
    }


    /**
     * 构建exam的Request
     *
     * @param url 请求的路径
     * @param sub viewstate
     * @param s   包含请求的学期还有学期
     * @param map 请求头
     * @return 返回构建的request
     */
    private Request ExamPostRequest(String url, String sub, String[] s, Map<String, String> map) {
        FormBody formBody = buildBodyExam(sub, s);
        Request.Builder request = new Request.Builder().post(formBody).url(url);
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            Map.Entry element = (Map.Entry) stringStringEntry;
            String strKey = (String) element.getKey();
            String strObj = (String) element.getValue();
            request.header(strKey, strObj);
        }
        return request.build();
    }

    /**
     * 构建request对象
     *
     * @param url 访问的地址
     * @param sub 需要的viewstate
     * @param m   请求头信息    @return 构建好的request
     */
    private Request buildPostRequestBody(String url, Map<String, String> m, String... sub) {
        FormBody formBody = buildBody(sub);
        if (formBody != null) {
            Request.Builder post = new Request.Builder().url(url).post(formBody);
            for (Map.Entry<String, String> stringStringEntry : m.entrySet()) {
                Map.Entry element = (Map.Entry) stringStringEntry;
                String strKey = (String) element.getKey();
                String strObj = (String) element.getValue();
                post.header(strKey, strObj);
            }
            return post.build();
        }
        return null;
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
     * @return body请请求体
     */
    private FormBody buildBody(String... sub) {
        MyLog.i("构建历年成绩查询需要传入的参数" + sub[0] + "viewstate" + sub[1] + "学年" + sub[2] + "学期");
        // if (sub[3] != null) {
        if (sub[1].equals("") && sub[2].equals("") && sub[3].equals("空")) {  //个人信息查询
            MyLog.i("走了我想要的");
            return new FormBody.Builder()
                    .add("__EVENTTARGET", "")
                    .add("__EVENTARGUMENT", "")
                    .add("__VIEWSTATE", sub[0])
                    .add("hidLanguage", "")
                    .add("ddlXN", "")
                    .add("ddlXQ", "")
                    .add("ddl_kcxz", "")
                    .add("Button1", "成绩统计")
                    .build();
            //  }
        } else if (sub[1].equals("") && sub[2].equals("")) { //历年成绩查询
            return new FormBody.Builder()
                    .add("__EVENTTARGET", "")
                    .add("__EVENTARGUMENT", "")
                    .add("__VIEWSTATE", sub[0])
                    .add("hidLanguage", "")
                    .add("ddlXN", "")
                    .add("ddlXQ", "")
                    .add("ddl_kcxz", "")
                    //历年成绩
                    .add("btn_zcj", "历年成绩")
                    .build();
        } else { //如果sub[1],sub[2]是空的话，那就是历年成绩查询。否则的就是学期成绩
            return new FormBody.Builder()
                    .add("__EVENTTARGET", "")
                    .add("__EVENTARGUMENT", "")
                    .add("__VIEWSTATE", sub[0])
                    .add("hidLanguage", "")
                    .add("ddlXN", sub[1])
                    .add("ddlXQ", sub[2])
                    .add("ddl_kcxz", "")
                    //这里修改了，学期成绩，
                    .add("btn_xq", "学期成绩")
                    .build();
        }
    }


    /**
     * @param viewstate viewstate状态
     * @return 构建的body
     */

    private FormBody buildBodyExam(String viewstate, String[] s) {
        return new FormBody.Builder()
                .add("__EVENTTARGET", "xnd")
                .add("__EVENTARGUMENT", "")
                .add("__VIEWSTATE", viewstate)
                .add("xnd", s[0])
                .add("xqd", s[1])
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
