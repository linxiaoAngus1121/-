package cn.my.forward.mvp.sourcequery.mvp.biz;

import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_s;
import cn.my.forward.okhttp.MyOkhttp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by 123456 on 2018/2/9.
 * 登录，查询成绩类
 */

public class SourceAndLoginBiz implements ILogin {
    private String stuName; //学生的名字
    private MyOkhttp instance = MyOkhttp.getInstance();     //单例实现
    private ArrayList<Bean_s> list = new ArrayList<>();     //存放成绩的list
    private Bean_l bean;
    private static SourceAndLoginBiz mInstance;

    private SourceAndLoginBiz() {

    }

    /**
     * 采用单例模式，保证数据不会丢失
     *
     * @return SourceAndLoginBiz的实例
     */
    public static SourceAndLoginBiz getInstance() {
        if (mInstance == null) {
            synchronized (SourceAndLoginBiz.class) {
                if (mInstance == null) {
                    mInstance = new SourceAndLoginBiz();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void login(Bean_l bean, IOnLoginListener listener) {
        tologin(bean, listener);
    }

    @Override
    public void prepareLogin(IGetCodeListtener listener) {
        getVIEWSTATE(listener);
    }

    @Override
    public void score(IOnQuerySourceListener querySourceListener) {
        toGradeQurry(bean, querySourceListener);
    }

    @Override
    public void timeTable(ITimeTableListener listener) {
        toTimeQuery(bean, stuName, listener);
    }


    /**
     * 获取页面的viewstate
     *
     * @param listener 回调监听
     */
    private void getVIEWSTATE(final IGetCodeListtener listener) {
        final Bean_l bean = new Bean_l();
        instance.GetRequest("http://jwxt.sontan.net/", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("000", e.toString());
                listener.getViewStateError(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.body() != null) {
                    final String string = response.body().string();
                    //取出请求头的信息
                    Headers headers = response.headers();
                    List<String> values = headers.values("Set-Cookie");
                    String routeid = null;
                    String session_id = null;
                    for (int i = 0; i < values.size(); i++) {
                        if (i == 0) {
                            int indexOf = values.get(0).indexOf(";");
                            String substring = values.get(0).substring(0, indexOf);
                            routeid = substring + ";";
                            Log.i("000", routeid + "截取了乐乐乐乐乐乐");
                        } else {
                            int indexOf = values.get(1).indexOf(";");
                            session_id = values.get(1).substring(0, indexOf);
                            session_id = session_id + ";";
                            Log.i("000", session_id + "sessionid");
                        }
                    }
                    String finalcookies = routeid + session_id;
                    bean.setCookies(finalcookies);
                    Log.i("000", finalcookies);
                    String substring = string.substring(2282, 2302);
                    Log.i("000", substring + "截取后的__VIEWSTATE");
                    bean.setViewState(substring);
                    getpic(bean, listener);
                }
            }

        });
    }


    /**
     * viewstate获取后就要获取图片验证码
     *
     * @param bean     里面存放的是登录所需的信息，一步一步拼接
     * @param listener 回调监听
     */
    private void getpic(final Bean_l bean, final IGetCodeListtener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("Cookie", bean.getCookies());
        map.put("Connection", " Keep-Alive");
        instance.GetRequest("http://jwxt.sontan.net/CheckCode.aspx", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("000", e.toString() + "获取验证码失败了");
                listener.getCodeFailure(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    final InputStream inputStream = response.body().byteStream();
                    listener.getCodeSuccess(inputStream, bean);
                }
            }
        });
    }


    /**
     * 执行登录
     *
     * @param bean     实体类对想，包含登录所需要的信息
     * @param listener 回调监听
     */
    private void tologin(final Bean_l bean, final IOnLoginListener listener) {
        this.bean = bean;
        Map<String, String> map = new HashMap<>();
        // TODO: 2018/2/7  因为这个是要和服务器下发的cookies保持一致才能进行登录，之前的就是这里出错，所以不能登录.
        map.put("Cookie", bean.getCookies());
        map.put("Connection", " Keep-Alive");
        map.put("Content-Type", "application/x-www-form-urlencoded");
        map.put("Accept-Language", "zh-CN,zh;q=0.8");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0)" +
                " like Gecko");
        map.put("Content-Length", "230");
        instance.PostRequest("http://jwxt.sontan.net/Default2.aspx", bean, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("000", e.toString() + "嘎嘎嘎嘎嘎嘎嘎嘎嘎");
                listener.OnLoginError(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //这里不应全部读取，当读取到同学的时候，应该停止读取了
                if (response.code() != 200) {
                    listener.OnLoginError(response.message());
                    return;
                }
                InputStream inputStream = response.body().byteStream();
                //网页上采取的是gb2312格式，所以这里要采用一致的才不会乱码
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
                        "gb2312"));
                StringBuilder sb = new StringBuilder();
                String line;
                int indexOf;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    indexOf = sb.indexOf("同学");
                    if (indexOf != -1) {   //找到了
                        Log.i("000", "找到了");
                        Log.i("000", sb.toString());
                        listener.OnLoginSuccess();
                        stuName = getstName(sb.toString(), indexOf);
                        break;
                    }
                }
            }
        });
    }

    /**
     * 课表查询
     *
     * @param bean     登录信息
     * @param stuName  学生姓名
     * @param listener 回调监听
     */
    private void toTimeQuery(Bean_l bean, String stuName, final ITimeTableListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("Cookie", bean.getCookies());
        map.put("Connection", " Keep-Alive");
        map.put("Accept-Language", "zh-CN,zh;q=0.8");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0)" +
                " like Gecko");
        map.put("Referer", "http://jwxt.sontan.net/xs_main.aspx?xh=" + bean.getStuNo());
        String url = "http://jwxt.sontan.net/xskbcx" +
                ".aspx?xh=" + bean.getStuNo() + "&xm=" + stuName + "&gnmkdm=N121603";
        Log.i("000", url);
        instance.GetRequest(url, map, new
                Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        listener.QuertTimeTableFailure(e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() != 200) {
                            listener.QuertTimeTableFailure("课表查询出错");
                            return;
                        }
                        Log.i("000", "请求成功");
                        String streamtoString = streamtoString(response.body().byteStream());
                        if (streamtoString != null) {
                            List<String> list = JsoupMethod(streamtoString);
                            listener.QueryTimeTableSuccess(list);
                        } else {
                            listener.QuertTimeTableFailure("课表解析出错");
                        }


                    }
                });

    }

    /**
     * 调用jsoup库进行解析
     *
     * @param streamtoString jsoup解析的字符串
     * @return 返回最终需要的数据
     */
    private List<String> JsoupMethod(String streamtoString) {
        Document parse = Jsoup.parse(streamtoString);
        Elements elements = parse.select("table.blacktab");
        List<Node> nodes = elements.get(0).childNode(1).childNodes();
        //这个list就放有我需要的课表信息
        Log.i("000", nodes.size() + "size");
        ArrayList<Node> need = new ArrayList<>();
        int size = nodes.size();
        for (int i = 1; i < size - 1; i++) {
            if (i % 2 == 0) {   //只取出双数的，才是我们需要的
                need.add(nodes.get(i));
            }
        }
        return CastData(need);

    }

    /**
     * 进行数据的解析
     *
     * @param nodes 数据源
     * @return 返回可以最终显示的数据
     */
    private List<String> CastData(ArrayList<Node> nodes) {
        List<String> list = new ArrayList<>();  //这个list最终展示到界面上的所有信息
        int s = nodes.size();
        for (int i = 0; i < s; i++) {
            Node node = nodes.get(i);
            switch (i) {
                case 0:
                    dealData3(list, node);
                    break;
                case 1:
                    dealData2(list, node);
                    break;
                case 2:
                    dealData3(list, node);
                    break;
                case 3:
                    dealData2(list, node);
                    break;
                case 4:
                    dealData3(list, node);
                    break;
                default:
                    break;
            }
        }

        return list;
    }

    private void dealData2(List<String> list, Node node) {
        int size = node.childNodeSize() - 2;
        for (int t = 2; t < size; t++) {    //取出所有的课程信息
            String s1 = removeHtml(node.childNode(t).outerHtml());
            list.add(s1);
        }
    }

    private void dealData3(List<String> list, Node node) {
        int size = node.childNodeSize() - 2;
        for (int t = 3; t < size; t++) {    //取出所有的课程信息
            String s1 = removeHtml(node.childNode(t).outerHtml());
            list.add(s1);
        }
    }

    /**
     * 去除字符串中的html标签
     *
     * @param htmlStr 源字符串
     * @return 返回去除后的html的字符串
     */
    private String removeHtml(String htmlStr) {

        if (TextUtils.isEmpty(htmlStr)) {
            return null;
        }
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        java.util.regex.Pattern p_special;
        java.util.regex.Matcher m_special;
        // 定义HTML标签的正则表达式
        String regEx_html = "<[^>]+>";
        // 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        String regEx_special = "\\&[a-zA-Z]{1,10};";

        p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
        m_special = p_special.matcher(htmlStr);
        htmlStr = m_special.replaceAll("空"); // 过滤特殊标签

        return htmlStr;
    }


    /**
     * 把inputstream转成string类型的数据并返回
     *
     * @param inputStream
     * @return string 返回的string
     */
    private String streamtoString(InputStream inputStream) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
                    "gb2312"));
            String line;
            while ((line = reader.readLine()) != null) {

                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 进行成绩查询
     *
     * @param bean                实体类
     * @param querySourceListener 成绩查询回调
     */

    private void toGradeQurry(final Bean_l bean, final IOnQuerySourceListener querySourceListener) {
        Map<String, String> map = new HashMap<>();
        map.put("Cookie", bean.getCookies());
        map.put("Connection", " Keep-Alive");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0)" +
                " like Gecko");
        map.put("Referer", "http://jwxt.sontan.net/xs_main.aspx?xh=" + bean.getStuNo());
        instance.GetRequest("http://jwxt.sontan.net/xscjcx.aspx?xh=" +
                bean.getStuNo() + "&xm=学生&gnmkdm=N121605", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("000", e.toString() + "成绩失败了");
                querySourceListener.OnError(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    Log.i("000", "进入成绩成功了");
                    //接下来点击历年成绩，但是首先要去获取到页面上的__VIEWSTATE
                    InputStream inputStream = response.body().byteStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    int indexOf = -1;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        indexOf = sb.indexOf("__VIEWSTATE\" value=");
                        if (indexOf != -1) {   //找到了
                            Log.i("000", "找到了");
                            Log.i("000", sb.toString());
                            break;
                        }
                    }
                    //substring这里是最终需要的__VIEWSTATE
                    String substring = sb.toString().substring(indexOf);
                    String[] split = substring.split("value=\"", 2);
                    Log.i("000", split[0] + "这是0的部分");
                    Log.i("000", split[1] + "这是1的部分");  //取1这一部分
                    String[] split1 = split[1].split("\"", 2);//继续splite
                    Log.i("000", split1[0] + "这是0的部分+++++");    //最终需要的
                    Log.i("000", split1[1] + "这是1的部分+++++");
                    substring = split1[0];
                    //默认开始历年成绩查询
                    pastScouce(bean, substring, querySourceListener);
                }
            }
        });
    }


    /**
     * 历年成绩查询
     *
     * @param bean                实体类
     * @param substring           需要的参数__VIEWSTATE
     * @param querySourceListener 回调
     */
    private void pastScouce(Bean_l bean, String substring, final IOnQuerySourceListener
            querySourceListener) {
        String url = "http://jwxt.sontan.net/xscjcx.aspx?xh=" + bean.getStuNo() +
                "&xm=" + stuName + "&gnmkdm=N121605";
        Log.i("000", url);
        String utf8Togb2312 = utf8Togb2312(stuName);
        Map<String, String> map = new HashMap<>();
        map.put("Cookie", bean.getCookies());
        map.put("Connection", " Keep-Alive");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0)" +
                " like Gecko");
        map.put("Referer", "http://jwxt.sontan.net/xscjcx.aspx?xh=" + bean.getStuNo()
                + "&xm=" + utf8Togb2312 + "&gnmkdm=N121605");
        map.put("Content-Type", "application/x-www-form-urlencoded");
        map.put("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
        map.put("Content-Length", "3946");
        map.put("Accept-Language", "zh-CN,zh;q=0.8");
        instance.PostRequest(url, substring, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("000", e.getMessage() + "历年成绩失败");
                querySourceListener.OnError(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    Log.i("000", "查询历年成绩成功");
                    String cast = cast(response.body().byteStream());
                    if (cast == null) {
                        querySourceListener.OnError("查询成绩时发生未知错误");
                        return;
                    }
                    finalCast(cast);
                    querySourceListener.OnQuerySuccess(list);
                }
            }
        });
    }

    /**
     * 最终裁剪数据
     *
     * @param cast 数据源
     */
    private void finalCast(String cast) {
        StringBuilder builder = new StringBuilder(cast);
        int indexOf = builder.indexOf("<tr>");
        String substring = builder.substring(indexOf, builder.length());
        Log.i("000", substring + "substring++++++++");
        //还需要对数据进行裁剪
        docast(substring);
    }

    /**
     * 最终执行的方法
     *
     * @param substring 最初的字符串
     */
    private void docast(String substring) {
        if (substring.isEmpty() || !substring.contains("<td>")) {   //字符串是空或者已经没有<td>了，直接return
            return;
        }
        StringBuilder sb = new StringBuilder(substring);
        int indexOf = 0;
        String className = null;
        String source = null;
        for (int i = 1; i <= 9; i++) {
            indexOf = sb.indexOf("<td>", indexOf);
            if (indexOf == -1) {                             //没有找到<td>了
                return;
            }
            indexOf = indexOf + 1;  //让indexof+1,从下一位置开始
            if (i == 4) {                                   //第四个<td>是课程名字
                String local = sb.substring(indexOf + 3);   //去掉td>
                int i1 = local.indexOf("</td>");            //获取到课程名字的结束标签</td>
                className = local.substring(0, i1);         //截取出姓名
                Log.i("000", className + "课程名字");       //正确
            }
            if (i == 9) {                                    //第九个<td>是分数
                String local = sb.substring(indexOf + 3);   //同上
                int i1 = local.indexOf("</td>");            //同上
                source = local.substring(0, i1);            //同上
                Log.i("000", source + "分数是");           //正确

            }
        }
        Bean_s bean_s = new Bean_s(className, source);      //放入实体类对象中
        list.add(bean_s);
        String s = sb.substring(indexOf - 1);               //因为上面最后一个会+1，所以这里-1
        Log.i("000", "截取后的" + s);
        int i2 = s.indexOf("<tr");                          //应该跳到下一个<tr 出现的位置
        if (i2 == -1) {                                     //防止出现数组越界
            return;
        }
        String substring1 = s.substring(i2, s.length());     //下一个即将被截取的字符串
        Log.i("000", "下一个要进行截取的" + substring1);
        this.docast(substring1);                             //重新执行一遍，再查找下一个成绩
    }


    /**
     * 将inputestream截取inputstream中的信息，只读取我们需要的信息.
     *
     * @param inputStream inputstream
     * @return 返回包含成绩的字符串, 但是还需要进一步截取
     */
    private String cast(InputStream inputStream) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
                    "gb2312"));
            String line;
            int num = 0;
            while ((line = reader.readLine()) != null) {
                if ((num = sb.indexOf("align=\"left\"")) != -1) {   //出现这个字符串，那就不要再读了
                    break;
                }
                sb.append(line);
            }
            int i = sb.lastIndexOf("datelisthead");
            String substring = sb.substring(i, num);
            Log.i("000", substring + "成绩成绩");
            return substring;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将名字转成gb2312编码，并拼接返回
     *
     * @param name 名字
     * @return 返回拼接后的数据
     */
    private String utf8Togb2312(String name) {
        byte[] bytes = new byte[0];//先把字符串按gb2312转成byte数组
        try {
            bytes = name.getBytes("gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] a = new String[10];
        int i = 0;
        for (byte b : bytes) {//循环数组
            String s = Integer.toHexString(b);//再用Integer中的方法，把每个byte转换成16进制输出
            Log.i("000", s);
            String substring = s.substring(6, 8).toUpperCase();//可以了
            a[i] = substring;
            i++;
            Log.i("000", substring);
/*
            输出示例
            ffffffc2
            ffffffc8
            ffffffa8
            ffffffcd
            fffffffe
             */
            //去掉前面6个f就是每个名字的gb2312编码
        }
        StringBuilder builder = new StringBuilder();
        for (String anA : a) {
            if (anA == null) {
                continue;
            }
            builder.append("%");
            builder.append(anA);
        }
        Log.i("000", builder.toString() + "拼接完成");

        return builder.toString();
    }


    /**
     * 获取学生的姓名
     *
     * @param s       爬取到网页上的数据
     * @param indexOf 出现了同学两个字的位置
     * @return 返回学生的姓名
     */
    private String getstName(String s, int indexOf) {
        int i = s.indexOf("\"xhxm\">");
        String substring = s.substring(i + 7, indexOf);
        Log.i("000", substring + "截取到的名字");
        return substring;
    }

}
