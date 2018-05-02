package cn.my.forward.mvp.sourcequery.mvp.biz;

import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
import cn.my.forward.mvp.sourcequery.mvp.bean.ExamBean;
import cn.my.forward.mvp.sourcequery.mvp.bean.LevelBean;
import cn.my.forward.mvp.sourcequery.mvp.bean.TimeTableBean;
import cn.my.forward.mvp.sourcequery.mvp.utils.CoursePages;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;
import cn.my.forward.okhttp.MyOkhttp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by 123456 on 2018/2/9.
 * 逻辑处理层
 */

public class SourceAndLoginBiz implements ILogin {
    private String stuName; //学生的名字
    private MyOkhttp instance = MyOkhttp.getInstance();     //单例实现
    private ArrayList<Bean_s> list = new ArrayList<>();     //存放成绩的list
    private Bean_l bean;
    private static SourceAndLoginBiz mInstance;
    private String viewState;

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
    public void score(String year, IOnQuerySourceListener querySourceListener) {
        toGradeQurry(year, bean, querySourceListener);
    }

    @Override
    public void timeTable(int start, ITimeTableListener listener) {
        toTimeQuery(start, bean, stuName, listener);
    }


    @Override
    public void examQuery(String postion, IExamListener listener) {
        StringBuilder builder = new StringBuilder(postion);
        String[] s = new String[5];
        int indexOf = builder.lastIndexOf("-");
        s[0] = builder.substring(0, indexOf);//年
        s[1] = builder.substring(indexOf + 1);//学期
        toExam(listener, s);
    }

    @Override
    public void levelQuery(final ILevelListener listener) {
        final String url = "http://jwxt.sontan.net/xsdjkscx.aspx?xh=" + bean.getStuNo() + "&xm=" +
                stuName + "&gnmkdm=N121606";
        Log.i("000", url);
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;" +
                "q=0.8");
        map.put("Accept-Language", "zh-CN,zh;q=0.8");
        map.put("Connection", "keep-alive");
        map.put("Cookie", bean.getCookies());
        map.put("Referer", "http://jwxt.sontan.net/xs_main.aspx?xh=" + bean.getStuNo());
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, " +
                "like Gecko) Chrome/55.0.2883.87 UBrowser/6.2.3964.2 Safari/537.36");

        instance.GetRequest(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.showResultError("嗷了个嗷~~跑偏了");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //   listener.showResultSucceed();
                if (response.body().byteStream() == null) {
                    listener.showResultError("嗷了个嗷~~跑偏了");
                    return;
                }
                getLevelData(response.body().byteStream(), url, listener);
            }
        });
    }


    /**
     * 截取等级考试的html
     *
     * @param inputStream 输入流
     * @param url         baseurl
     * @param listener    回调监听
     */
    private void getLevelData(InputStream inputStream, String url, ILevelListener listener) {
        if (inputStream == null || url == null) {
            return;
        }

        try {
            Document document = Jsoup.parse(inputStream, "gb2312", url);
            Elements table = document.select("table tr:not(.datelisthead)");
            ArrayList<LevelBean> been = new ArrayList<>();
            Log.i("000", table.size() + "");
            for (Element the : table) {
                Elements td = the.select("td");
                LevelBean bean = new LevelBean(td.get(2).text() + "      ", td.get(3).text(), td.get
                        (5).text
                        ());
                been.add(bean);
            }
            listener.showResultSucceed(been);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 考试查询
     *
     * @param listener 监听器
     * @param s        数组保存所有数据
     */
    private void toExam(final IExamListener listener, String[] s) {
        final String url = "http://jwxt.sontan.net/xskscx" + ".aspx?xh=" + bean.getStuNo() +
                "&xm=" +
                stuName + "&gnmkdm=N121604";
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;" +
                "q=0.8");
        map.put("Accept-Language", "zh-CN,zh;q=0.8");
        map.put("Connection", "keep-alive");
        map.put("Cookie", bean.getCookies());
        map.put("Referer", "http://jwxt.sontan.net/xs_main.aspx?xh=" + bean.getStuNo());
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, " +
                "like Gecko) Chrome/55.0.2883.87 UBrowser/6.2.3964.2 Safari/537.36");


        instance.PostExamRequest(url, viewState, s, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("000", e.toString());
                listener.showExamError("嗷了个嗷，出错了");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                castExamState(response.body().byteStream(), url, listener);

            }
        });
    }


    /**
     * 获取考试查询页面上的viewstate
     *
     * @param inputStream 输入流
     * @param url         url地址
     * @param listener    回调监听
     */
    private void castExamState(InputStream inputStream, String url, IExamListener listener) {
        if (inputStream == null) {
            return;
        }
        Document document = null;
        try {
            document = Jsoup.parse(inputStream, "gb2312", url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (document != null) {
            Elements elements = document.select("input");   //第一次进入是为了获取viewstate，同时判断是否需要查询数据
            //获取出第三个input标签
            Element element = elements.get(2);
            viewState = element.attr("value");
            //  getdata(document, listener);
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getdata(document, listener);
    }

    /**
     * 进行考试查询页面数据的截取
     *
     * @param document 文档对象
     * @param listener 回调监听
     */
    private void getdata(Document document, IExamListener listener) {
        if (document != null) {
            //这里需要再一次判断，第一次有没有数据，没有签的话没有只有一个<tr>标签，直接return 回去
            if (isfirstHasData(document)) {
                //进行数据解析
                pastExamData(document, listener);
            }
        }
    }

    /**
     * 解析考试查询数据
     *
     * @param document 文档对象
     * @param listener 回调监听
     */
    private void pastExamData(Document document, IExamListener listener) {
        //语法含义：找出table 下所有tr标签除了class为datelisthead
        Elements elements = document.select("table tr:not(.datelisthead)");
        castExamData(listener, elements, elements.size());
    }


    /**
     * 对考试的信息进行解析返回给activity
     *
     * @param listener 回调监听
     * @param elements 文档对象
     * @param size     数据集合大小
     */
    private void castExamData(IExamListener listener, Elements elements, int size) {
        int i = 0;
        List<ExamBean> list = new ArrayList<>();
        while (i < size) {
            Elements select = elements.get(i).select("td");
            String s = "".equals(select.get(6).text()) ? "" : " " + select.get(6).text() + "号";
            ExamBean bean = new ExamBean(select.get(1).text(), select.get(3).text(), select.get
                    (4).text() + s);
            list.add(bean);
            i++;
        }
        listener.showExamSuccess(list);

    }

    /**
     * 判断第一次是否有数据
     *
     * @param document 文档对象
     * @return true 有数据 false 没有数据
     */
    private boolean isfirstHasData(Document document) {
        return document.select("tr").size() > 1;
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
                    viewState = substring;
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
                        return;
                    }
                }
                listener.OnLoginError("开了会小差，出错了");

            }
        });
    }

    /**
     * 课表查询
     *
     * @param start    周数信息
     * @param bean     登录信息
     * @param stuName  学生姓名
     * @param listener 回调监听
     */
    private void toTimeQuery(final int start, Bean_l bean, String stuName, final ITimeTableListener
            listener) {

        Map<String, String> map = new HashMap<>();
        map.put("Cookie", bean.getCookies());
        map.put("Connection", " Keep-Alive");
        map.put("Accept-Language", "zh-CN,zh;q=0.8");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0)" +
                " like Gecko");
        map.put("Referer", "http://jwxt.sontan.net/xs_main.aspx?xh=" + bean.getStuNo());
        String url = "http://jwxt.sontan.net/xskbcx" +
                ".aspx?xh=" + bean.getStuNo() + "&xm=" + stuName + "&gnmkdm=N121603";
        MyLog.i(url);
        instance.GetRequest(url, map, new Callback() {
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
                MyLog.i("请求成功");
                String streamtoString = streamtoString(response.body().byteStream());
                if (streamtoString != null) {
                    List<TimeTableBean> list = JsoupMethod(streamtoString);
                    ArrayList xuanke = CoursePages.xuanke(list);
                    ArrayList tempal2 = (ArrayList) xuanke.get(start);
                    listener.QueryTimeTableSuccess(tempal2);
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
    private List<TimeTableBean> JsoupMethod(String streamtoString) {
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
    private List<TimeTableBean> CastData(ArrayList<Node> nodes) {
        List<TimeTableBean> list = new ArrayList<>();  //这个list最终展示到界面上的所有信息
        int s = nodes.size();   //每个node节点代表的是星期一到星期五的1，2节，3，4节，5，6节，7，8节，9，10，11节
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

    private void dealData2(List<TimeTableBean> list, Node node) {
        int size = node.childNodeSize() - 2;
        for (int t = 2; t < size; t++) {    //取出所有的课程信息
            //  String s1 = removeHtml(node.childNode(t).outerHtml());
            Node node1 = node.childNode(t);
            nodeInsert(node1, list);
        }
    }

    private void dealData3(List<TimeTableBean> list, Node node) {
        int size = node.childNodeSize() - 2;
        for (int t = 3; t < size; t++) {    //取出所有的课程信息,去掉部分头尾
            Node node1 = node.childNode(t);
            nodeInsert(node1, list);
        }
    }

    /**
     * 为node节点增加一个<br>标签
     *
     * @param node1 哪个node节点
     * @param list  存放TimeTableBean的list
     */
    private void nodeInsert(Node node1, List<TimeTableBean> list) {
        StringBuilder builder = new StringBuilder(node1.outerHtml());
        int indexOf = builder.indexOf(">");
        String insert = builder.insert(indexOf + 1, "<br>").toString();
        String replace = builder.replace(0, builder.length(), insert).toString();//可以进行提取的东西
        //这里如果页面出现其他信息的话，应该先把他去除，即先去除
        // <br>
        //<font color='red'>(调0230)</font>
        //<br>

       /* if (replace.contains("<font")) {
            StringBuilder builder1 = new StringBuilder(replace);
            int i = builder1.indexOf("<font");  //出现<font的位置
            int replace1 = builder1.indexOf("</fo nt>");
            StringBuilder delete = builder1.delete(i - 4, replace1 + 11);
            replace = delete.toString();
        }
*/
        while (replace.contains("<font")) {
            StringBuilder builder1 = new StringBuilder(replace);
            int i = builder1.indexOf("<font");  //出现<font的位置
            int replace1 = builder1.indexOf("</font>");
            int i1 = builder1.indexOf("</td>");
            StringBuilder delete;
            if (replace1 + 7 == i1) {    //代表是结尾的那个《font》
                delete = builder1.delete(i - 8, replace1 + 7);
            } else {
                delete = builder1.delete(i - 4, replace1 + 11);
            }
            replace = delete.toString();
        }

        String[] split = replace.split("<br>");
        for (int i = 1; i < split.length; i = i + 5) {

            TimeTableBean mTimeBean = new TimeTableBean();
            for (int j = i; j <= i + 3; j++) {
                String t = removeHtml(split[j]);
                assert t != null;
                if (t.equals("空")) {
                    break;
                }
                if (j == i) {   //课程名
                    mTimeBean.setName(split[j]);
                } else if (j == i + 1) {    //课程上课时间
                    int day[] = selectWeek(split[j]);
                    mTimeBean.setXingqi(day[0]);
                    mTimeBean.setStartzhou(day[1]);
                    mTimeBean.setEndzhou(day[2]);
                    mTimeBean.setBiaozhi(day[3]);
                    mTimeBean.setStartjie(day[4]);
                    if (day[6] != 0) {
                        mTimeBean.setEndjie(day[6]);
                    } else {
                        mTimeBean.setEndjie(day[5]);
                    }
                } else if (j == i + 2) {    //课程教师
                    mTimeBean.setTeacher(split[j]);
                } else if (j == i + 3) {    //课程地点
                    mTimeBean.setAddress(removeHtml(split[j]));
                }
            }
            list.add(mTimeBean);
        }

    }

    /**
     * 选出是周几等，如果某一块出现问题，就会忽视，全部返回o
     *
     * @param s 字符串数据
     * @return [0]代表周几
     * [1]代表几周开始
     * [2]几周结束
     * [3]代表是否单双周，1代表单周，2代表双周，3代表不分周
     * [4] 第几节开始
     * [5] [6] 第几节结束
     */
    private int[] selectWeek(String s) {
        int day[] = new int[7];
        StringBuilder builder = new StringBuilder(s);
        int indexOf = builder.indexOf("第");     //第出现的位置
        int indexOf1 = builder.indexOf("-");    //出现-的位置
        int indexOf2 = builder.indexOf("{");    //出现{的位置
        int indexOf3 = builder.indexOf("}");    //出现}的位置
        int indexOf4 = builder.indexOf("|");     //|出现的位置
        String substring = builder.substring(indexOf - 1, indexOf);//第前面一位，就是我们需要周几
        //     MyLog.i(substring);
        switch (substring) {
            case "一":
                day[0] = 1;
                break;
            case "二":
                day[0] = 2;
                break;
            case "三":
                day[0] = 3;
                break;
            case "四":
                day[0] = 4;
                break;
            case "五":
                day[0] = 5;
                break;
            case "六":
                day[0] = 6;
                break;
            default:    //不满足周的情况,会出现奇怪的情况，直接返回不处理
                for (int i = 1; i <= 6; i++) {
                    day[i] = 0;
                }
                return day;
        }
        String substring1 = builder.substring(indexOf2 + 2, indexOf1);  //-前面的周数
        day[1] = Integer.valueOf(substring1);

        if (indexOf4 == -1) {   //没有单双周的情况
            String substring2 = builder.substring(indexOf1 + 1, indexOf3 - 1);  //-后面出现的周数
            try {
                day[2] = Integer.valueOf(substring2);
            } catch (NumberFormatException e) {
                for (int i = 1; i <= 6; i++) {
                    day[i] = 0;
                }
                return day;
            }
            day[3] = 3;                         //=3两周都要上
        } else {                              //有单双周的情况
            String substring2 = builder.substring(indexOf1 + 1, indexOf4 - 1);
            try {
                day[2] = Integer.valueOf(substring2);
            } catch (NumberFormatException e) {
                for (int i = 1; i <= 6; i++) {
                    day[i] = 0;
                }
                return day;
            }
            String substring3 = builder.substring(indexOf4 + 1, indexOf3);
            if (substring3.startsWith("单")) {
                day[3] = 1;
            } else {
                day[3] = 2;
            }
            //         MyLog.i(substring2);
        }
        String substring4 = builder.substring(indexOf + 1, indexOf2);   //第几节到第几节
        String[] split = substring4.split(",");
        for (int i = 0; i < split.length; i++) {
            try {
                if (i == 0) {
                    day[4] = Integer.valueOf(split[i]);
                } else if (i == 1) {    //这个也可能会包含节字
                    day[5] = Integer.valueOf(split[i].replaceAll("\\D", ""));
                } else if (i == split.length - 1) {   //这个会包含节字
                    day[6] = Integer.valueOf(split[i].replaceAll("\\D", ""));
                }
            } catch (NumberFormatException e) {
                for (int t = 1; t <= 6; t++) {
                    day[t] = 0;
                }
                return day;
            }

        }
        return day;
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
     * @param inputStream 源inputstream
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
     * @param year                包含学年学期信息
     * @param bean                实体类
     * @param querySourceListener 成绩查询回调
     */

    private void toGradeQurry(final String year, final Bean_l bean, final IOnQuerySourceListener
            querySourceListener) {
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
                    MyLog.i("进入成绩成功了");
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
                    //   Log.i("000", split[0] + "这是0的部分");
                    //  Log.i("000", split[1] + "这是1的部分");  //取1这一部分
                    String[] split1 = split[1].split("\"", 2);//继续splite
                    //   Log.i("000", split1[0] + "这是0的部分+++++");    //最终需要的
                    //   Log.i("000", split1[1] + "这是1的部分+++++");
                    substring = split1[0];
                    if (year.equals("")) {   //默认开始历年成绩查询
                        pastScouce(bean, substring, "", "", querySourceListener);
                    } else if (year.equals("历年成绩")) {  //选择的是历年成绩查询拿一项
                        pastScouce(bean, substring, "", "", querySourceListener);
                    } else {
                        StringBuilder builder = new StringBuilder(year);
                        String[] s = new String[5];
                        int index = builder.lastIndexOf("-");
                        s[0] = builder.substring(0, index);//年
                        s[1] = builder.substring(index + 1);//学期
                        MyLog.i(s[0] + s[1] + "这即使数据");
                        pastScouce(bean, substring, s[0], s[1], querySourceListener);
                    }
                }
            }
        });
    }


    /**
     * 成绩查询
     *
     * @param bean                实体类
     * @param substring1          viewstatstate
     * @param s                   学年
     * @param substring           学期
     * @param querySourceListener 回调
     */
    private void pastScouce(Bean_l bean, final String substring1, final String s, final String
            substring, final IOnQuerySourceListener querySourceListener) {
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
        map.put("Accept-Language", "zh-CN,zh;q=0.8");
        instance.PostRequest(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLog.i(e.getMessage() + "查询成绩失败");
                querySourceListener.OnError(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    //       parseToFile(response.body().byteStream());
                    String cast = cast(response.body().byteStream());
                 /*   if (substring.equals("") || s.equals("")) {
                        Log.i("000", "查询成绩成功");
                        if (cast == null) {
                            querySourceListener.OnError("查询成绩时发生未知错误");
                            return;
                        }
                        finalCast(cast);
                        querySourceListener.OnQuerySuccess(list);
                    } else {
                        //这里执行的是有学期的
                        MyLog.i("走了另外一个方法");
                    }*/
                    finalCast(cast);
                    querySourceListener.OnQuerySuccess(list);
                }
            }
        }, substring1, s, substring);
    }


    /**
     * 最终裁剪数据
     *
     * @param cast 数据源
     */
    private void finalCast(String cast) {
        if (list.size() > 0) { //这里返回的数据要先清除，不然会由于单例而造成的数据错乱
            list.clear();
        }
        StringBuilder builder = new StringBuilder(cast);
        int indexOf = builder.indexOf("<tr>");
        if (indexOf == -1) {    //为空的情况下
            return;
        }
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
                //    Log.i("000", className + "课程名字");       //正确
            }
            if (i == 9) {                                    //第九个<td>是分数
                String local = sb.substring(indexOf + 3);   //同上
                int i1 = local.indexOf("</td>");            //同上
                source = local.substring(0, i1);            //同上
                //   Log.i("000", source + "分数是");           //正确

            }
        }
        Bean_s bean_s = new Bean_s(className, source);      //放入实体类对象中
        list.add(bean_s);
        String s = sb.substring(indexOf - 1);               //因为上面最后一个会+1，所以这里-1
        //   Log.i("000", "截取后的" + s);
        int i2 = s.indexOf("<tr");                          //应该跳到下一个<tr 出现的位置
        if (i2 == -1) {                                     //防止出现数组越界
            return;
        }
        String substring1 = s.substring(i2, s.length());     //下一个即将被截取的字符串
        //  Log.i("000", "下一个要进行截取的" + substring1);
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
            MyLog.i(substring + "成绩成绩");
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
        MyLog.i(builder.toString() + "拼接完成");

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
