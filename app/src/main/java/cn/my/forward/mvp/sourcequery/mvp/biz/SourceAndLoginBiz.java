package cn.my.forward.mvp.sourcequery.mvp.biz;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.my.forward.mvp.sourcequery.mvp.bean.BeanPerson;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_SpareTicket;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_ticket;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_ticketResult;
import cn.my.forward.mvp.sourcequery.mvp.bean.ExamBean;
import cn.my.forward.mvp.sourcequery.mvp.bean.LevelBean;
import cn.my.forward.mvp.sourcequery.mvp.bean.TimeTableBean;
import cn.my.forward.mvp.sourcequery.mvp.bean.lepai.Beauty;
import cn.my.forward.mvp.sourcequery.mvp.bean.lepai.JsonRootBean;
import cn.my.forward.mvp.sourcequery.mvp.utils.CoursePages;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;
import cn.my.forward.okhttp.MyOkhttp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

import static android.util.Log.i;

/**
 * Created by 123456 on 2018/2/9.
 * 逻辑处理层
 */

public class SourceAndLoginBiz implements ILogin {
    private String stuName; //学生的名字
    private String nameexceptclass;//学生名字没有同学两个字的
    private MyOkhttp instance = MyOkhttp.getInstance();     //单例实现
    private ArrayList<String> list = new ArrayList<>();     //存放成绩的list
    private Bean_l bean;
    private static SourceAndLoginBiz mInstance;
    private String viewState;   //考试查询的viewstate
    private String viewStateForPerson;   //个人信息查询的viewstate（即成绩查询中的成绩统计）
    private boolean flag = false;    //判断是否是个人信息查询的标志位
    private String submitState;
    private Gson gson;
    private boolean isfirst = true;    //默认是false,获取开始查询的viewstate的标志位

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
        toGradeQurry(year, bean, querySourceListener, null);
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
        MyLog.i(url);
        //这里考虑使用SparseArray，但是SparseArray的key不可为string,所以用不了
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

    @Override
    public void personInfomation(IPersonListener listener) {
        GetPersonData(listener);
    }

    @Override
    public void lepai(String path, ILePaiListener lePaiListener) {
        getLepai(path, lePaiListener);
    }

    private void getLepai(String path, final ILePaiListener lePaiListener) {
        if (lePaiListener == null) {
            return;
        }
        final File file = new File(path);
        String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
        if (file.exists()) {
            MyLog.i("存在");
            MyLog.i(file.getPath());
        } else {
            lePaiListener.getCodeFailure("出了点问题，请重新选择图片");
            MyLog.i("不存在");
            return;
        }
        if (determineWhetherTheSizeExceeds2M(file)) {
            MyLog.i("大小超过2M");
            lePaiListener.getCodeFailure("图片不可以超过2M哟");
            return;
        } else {
            MyLog.i("大小没有超过2M");
        }
        instance.testPost(url, file, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLog.i("失败");
                lePaiListener.getCodeFailure("出了点问题，请重新选择图片");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MyLog.i("成功");
                String string = response.body().string();
                Gson gson = new Gson();
                MyLog.i(string);
                JsonRootBean rootBean = gson.fromJson(string, JsonRootBean
                        .class);
                if (null == rootBean.getFaces()) {
                    lePaiListener.getCodeFailure("请求过于频繁");
                    return;
                }
                if (rootBean.getFaces().size() != 0) {
                    MyLog.i("face数组有东西");
                    Beauty beauty = rootBean.getFaces().get(0).getAttributes().getBeauty();
                    MyLog.i(beauty.toString());
                    double male_score = beauty.getMale_score();
                    double female_score = beauty.getFemale_score();
                    DecimalFormat df = new DecimalFormat("######0.00");
                    lePaiListener.getDataSuccess(df.format((male_score + female_score) / 2));
                } else {                        //没有人脸的情况下
                    MyLog.i("face数组没有东西");
                    lePaiListener.getCodeFailure("未检测到人脸");
                }

            }
        });
    }

    /**
     * 判断是否超过2M
     *
     * @param file 文件
     */
    private boolean determineWhetherTheSizeExceeds2M(File file) {
        long length = file.length();
        int MaxSize = 1024 * 1024 * 2;//定义MB的计算常量
        return length > MaxSize;

    }

    @Override
    public void tickets(String from, String to, ITickedListener listener) {
        getTicket(from, to, listener);
    }

    /**
     * 获取火车票信息
     *
     * @param from     出发地点
     * @param to       到达地点
     * @param listener 回调
     */
    private void getTicket(String from, String to, final ITickedListener listener) {
        final String url = "http://api.jisuapi" +
                ".com/train/station2s?appkey=8a4cb09145dbc09b&start=" + from + "&end=" + to +
                "&ishigh=0";    //请求列车信息还有票价
        //日期是明天的
        String date = getDatePlus1();
        final String url2 = "http://api.jisuapi.com/train/ticket?appkey=8a4cb09145dbc09b&start="
                + from + "&end=" + to + "&date=" + date;  //请求列车剩票
        MyLog.i(url2);
        instance.testGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.getDataError();
                }

            }

            @Override
            public void onResponse(Call call, @NonNull Response response) throws IOException {
                if (response.code() != 200) {
                    if (listener != null) {
                        listener.getDataError();
                    }
                    return;
                }
                MyLog.i("请求成功");
                gson = new Gson();
                try {
                    final Bean_ticket bean_ticket = gson.fromJson(response.body().string(),
                            Bean_ticket.class);        //这个bean封装了所有信息,车票信息的bean
                    List<Bean_ticketResult> result = bean_ticket.getResult();
                    int size = result.size();

                    for (int i = 0; i < result.size(); i++) {
                        MyLog.i(bean_ticket.getResult().get(i).toString() + "原来的");
                    }

                    for (int i = 0; i < size; i++) {
                        if (i >= size - 1) {    //防止越界
                            break;
                        }
                        if (result.get(i).getDeparturetime().equals(result.get(i + 1)
                                .getDeparturetime()) && result.get(i).getArrivaltime().equals
                                (result.get(i + 1).getArrivaltime())) {    //重复了，remove后面那个
                            result.remove(i + 1);
                            size = result.size();
                        }
                    }
                    for (int i = 0; i < result.size(); i++) {
                        MyLog.i(bean_ticket.getResult().get(i).toString() + "后来的");
                    }
                    getrequestagain(bean_ticket, url2, listener);
                } catch (JsonSyntaxException e) {
                    if (listener != null) {
                        listener.getDataError();
                    }
                }
            }
        });

    }

    /**
     * 获取明天日期
     */
    private String getDatePlus1() {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = formatter.format(date);
        return dateString;
    }

    private void getrequestagain(final Bean_ticket bean_ticket, String url2, final ITickedListener
            listener) {
        instance.testGet(url2, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.getDataError();
                }
            }

            @Override
            public void onResponse(Call call, @NonNull Response response) throws IOException {
                if (response.code() != 200) {
                    if (listener != null) {
                        listener.getDataError();
                    }
                    return;
                }
                try {
                    Bean_SpareTicket spareTicket = gson.fromJson(response.body().string(),
                            Bean_SpareTicket.class);    //余票信息
                    int size = spareTicket.getResult().size();
                    for (int i = 0; i < size; i++) {
                        MyLog.i(spareTicket.getResult().get(i).toString());
                    }

                    if (listener != null) {
                        listener.getDataSuccess(bean_ticket, spareTicket);
                    }
                } catch (JsonSyntaxException e) {
                    if (listener != null) {
                        listener.getDataError();
                    }
                }
            }
        });
    }

  /*  @Override
    public void questionQuery(final IQuestionListener listener) {
        final String url = "http://jwxt.sontan.net/wjdc.aspx?xh=" + bean.getStuNo() + "&xm=" +
                stuName
                + "&gnmkdm=N121304";
        MyLog.i(url + "问卷调查的url");
        Map<String, String> map = new HashMap<>();
        map.put("Accept-Language", "zh-CN,zh;q=0.8");
        map.put("Connection", "keep-alive");
        map.put("Cookie", bean.getCookies());
        map.put("Referer", "http://jwxt.sontan.net/xs_main.aspx?xh=" + bean.getStuNo());
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, " +
                "like Gecko) Chrome/55.0.2883.87 UBrowser/6.2.3964.2 Safari/537.36");
        instance.GetRequest(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLog.i("妈个鸡，进不去");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() != 200) {
                    return;
                }
                Document document = Jsoup.parse(response.body().byteStream(), "gb2312", url);
                String html = document.html();
                MyLog.i(html);
                MyLog.i("viewstate成功了");
                getQuestionViewState(response.body().byteStream(), url, listener);
            }
        });
    }

    @Override
    public void questionSubmit(List<String> list, final ISubmitListener listener) {
        if (submitState == null) {
            if (listener != null) {
                listener.submitError();

            }
        }
        String[] strings = ChangeCode(list);
        final String url = "http://jwxt.sontan.net/wjdc.aspx?xh=" + bean.getStuNo() + "&xm=" +
                utf8Togb2312
                        (nameexceptclass) + "&gnmkdm=N121304";
        MyLog.i(url);
        Map<String, String> map = new HashMap<>();
        map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-CN,zh;q=0.8");
        map.put("Cache-Control", "max-age=0");
        map.put("Connection", "keep-alive");
        map.put("Cookie", bean.getCookies());
        map.put("Referer", url);
        map.put("Content-Length", "15011");
        map.put("Content-Type", "application/x-www-form-urlencoded");
        map.put("Upgrade-Insecure-Requests", "1");
        map.put("Origin", "http://jwxt.sontan.net");
        map.put("Host", "jwxt.sontan.net");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, " +
                "like Gecko) Chrome/55.0.2883.87 UBrowser/6.2.3964.2 Safari/537.36");
        instance.PostQuestionRequest(url, submitState, map, strings, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.submitError();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Document document = Jsoup.parse(response.body().byteStream(), "gb2312", url);
                document.html();
                boolean b = document.html().contains("提交成功！");
                MyLog.i("提交是否成功" + b);

                if (b) {
                    if (listener != null) {
                        listener.submitSuccess();
                    }
                } else {
                    if (listener != null) {
                        listener.submitError();
                    }
                }
            }
        });

    }
    private String[] ChangeCode(List<String> list) {
        int size = list.size();
        String[] t = new String[size + 2];
        for (int i = 0; i < size; i++) {    //年级那个单独处理,其他的正常
            String s = utf8Togb2312(list.get(i));
            t[i + 2] = s;
        }
        return t;
    }

    private void getQuestionViewState(InputStream inputStream, String url, IQuestionListener
            listener) {
        try {
            Document document = Jsoup.parse(inputStream, "gb2312", url);
            String viewstate = document.select("input").get(2).val();//页面上的viewstate
            requestForpage(viewstate, listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestForpage(String viewstate, final IQuestionListener listener) {
        final String url = "http://jwxt.sontan.net/wjdc.aspx?xh=" + bean.getStuNo() + "&xm=" +
                stuName
                + "&gnmkdm=N121304";
        String name = utf8Togb2312(nameexceptclass);
        if (name == null) {
            if (listener != null) {
                listener.questionError();   //出错
                return;
            }
        }
        Map<String, String> map = new HashMap<>();
      map.put("Accept-Language","zh-CN,zh;q=0.8");
        map.put("Connection","keep-alive");
        map.put("Content-Type","application/x-www-form-urlencoded");
        map.put("Cookie",bean.getCookies());
        map.put("Referer","http://jwxt.sontan.net/wjdc.aspx?xh="+bean.getStuNo()+"&xm="+
    name +
            "&gnmkdm=N121304");
        map.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, "+
                "like Gecko) Chrome/55.0.2883.87 UBrowser/6.2.3964.2 Safari/537.36");
        instance.PostQuestionRequest(url,viewstate,map,null,new

    Callback() {
        @Override
        public void onFailure (Call call, IOException e){
            if (listener != null) {
                listener.questionError();
            }
        }

        @Override
        public void onResponse (Call call, Response response) throws IOException {
            InputStream inputStream = response.body().byteStream();
            Document document = Jsoup.parse(inputStream, "gb2312", url);
            submitState = document.select("input").get(2).val();    //获得待会要点提交后需要的viewstate
            int size = document.select("b").size();
            Elements elements = document.select("b");
            List<QuestionBean> list = new ArrayList<>(size - 2);
            if (size > 0) {
                for (int i = 2; i < size; i++) {    //把所有的b标签取出来，放到实体类对象中
                    QuestionBean questionBean = new QuestionBean();
                    questionBean.setQuestion(elements.get(i).text());//问题
                    SparseArray<String> answer = getAnswer(document, i);
                    questionBean.setAnswer(answer);
                    list.add(questionBean);
                }
            } else {
                if (listener != null) {
                    listener.questionError();
                    return;
                }
            }
            MyLog.i(list.size() + "size");
            if (listener != null) {
                listener.questionSuccess(list);
            }
        }
    });
}

    private SparseArray<String> getAnswer(Document document, int i) {
        SparseArray<String> map = new SparseArray<>();
        Elements elements = document.select("table");
        Elements select = elements.get(i).select("input");
        int size = elements.get(i).select("input").size();
        for (int t = 0; t < size; t++) {
            map.put(t, select.get(t).val());
        }
        return map;
    }*/

    private void GetPersonData(IPersonListener listener) {
        flag = true;
        this.toGradeQurry("", bean, null, listener);//这里就保证了取到viewStateForPerson

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
            MyLog.i(table.size() + "");
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
                MyLog.i(e.toString());
                if (listener != null) {
                    listener.showExamError("嗷了个嗷，出错了");
                }
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
            MyLog.i("inputstream为空");
            return;
        }
        Document document = null;
        try {
            document = Jsoup.parse(inputStream, "gb2312", url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (document != null && isfirst) {
            Elements elements = document.select("input");   //第一次进入是为了获取viewstate，同时判断是否需要查询数据
            //获取出第三个input标签
            Element element = elements.get(2);
            viewState = element.attr("value");
            MyLog.i("我走了getviewstate");
            isfirst = false;
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
            MyLog.i("doucement不为空" + document.html());
            if (hasData(document)) {
                //进行数据解析
                MyLog.i("hasdata为true");
                pastExamData(document, listener);
            } else {
                if (listener != null) {
                    listener.showExamError("这学期还没有数据哟");
                }
            }
        } else {
            if (listener != null) {
                listener.showExamError("好像出了点问题");
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
        MyLog.i("我走了castExamdata");
        while (i < size) {
            Elements select = elements.get(i).select("td");
            String s = "".equals(select.get(6).text()) ? "" : " " + select.get(6).text() + "号";
            ExamBean bean = new ExamBean(select.get(1).text(), select.get(3).text(), select.get
                    (4).text() + s);
            list.add(bean);
            i++;
        }
        if (listener != null) {
            listener.showExamSuccess(list);
        }

    }

    /**
     * 判断第一次是否有数据
     *
     * @param document 文档对象
     * @return true 有数据 false 没有数据
     */
    private boolean hasData(Document document) {
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
                MyLog.i(e.toString());
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
                            MyLog.i(routeid + "截取了乐乐乐乐乐乐");
                        } else {
                            int indexOf = values.get(1).indexOf(";");
                            session_id = values.get(1).substring(0, indexOf);
                            session_id = session_id + ";";
                            MyLog.i(session_id + "sessionid");
                        }
                    }
                    String finalcookies = routeid + session_id;
                    bean.setCookies(finalcookies);
                    MyLog.i(finalcookies);
                    String substring = string.substring(2282, 2302);
                    MyLog.i(substring + "截取后的__VIEWSTATE");
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
                i("000", e.toString() + "获取验证码失败了");
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
     * 执行登录，第一次没有保存的话，就要保存用户名密码到自己的数据库
     *
     * @param bean     实体类对想，包含登录所需要的信息
     * @param listener 回调监听
     */
    private void tologin(final Bean_l bean, final IOnLoginListener listener) {
        if (this.bean != null) {
            this.bean = null;
        }
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
        final String url = "http://jwxt.sontan.net/Default2.aspx";
        instance.PostRequest(url, bean, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLog.i(e.toString() + "嘎嘎嘎嘎嘎嘎嘎嘎嘎");
                if (listener != null) {
                    listener.OnLoginError();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() != 200) {
                    if (listener != null) {
                        listener.OnLoginError();
                    }
                    return;
                }
                Document document = Jsoup.parse(response.body().byteStream(), "gb2312", url);
                try {
                    Element element = document.select("span#xhxm").first();
                    if (element != null) {
                        stuName = element.text();
                        String[] split = null;
                        try {
                            split = stuName.split("同");
                            nameexceptclass = split[0];
                            if (listener != null) {
                                listener.OnLoginSuccess(stuName);
                            }
                        } catch (PatternSyntaxException e) {
                            if (listener != null) {
                                listener.OnLoginError();
                            }
                        }
                    } else {
                        if (listener != null) {
                            listener.OnLoginError();
                        }
                    }
                } catch (Selector.SelectorParseException e) {
                    if (listener != null) {
                        listener.OnLoginError();
                    }
                }
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
                if (listener != null) {
                    listener.QuertTimeTableFailure(e.toString());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() != 200) {
                    if (listener != null) {
                        listener.QuertTimeTableFailure("课表查询出错");
                    }
                    return;
                }
                MyLog.i("请求成功");
                String streamtoString = streamtoString(response.body().byteStream());
                if (streamtoString != null) {
                    List<TimeTableBean> list = JsoupMethod(streamtoString);
                    ArrayList xuanke = CoursePages.xuanke(list);
                    ArrayList tempal2 = (ArrayList) xuanke.get(start);
                    if (listener != null) {
                        listener.QueryTimeTableSuccess(tempal2);
                    }
                } else {
                    if (listener != null) {
                        listener.QuertTimeTableFailure("课表解析出错");
                    }
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
        MyLog.i(nodes.size() + "size");
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
        String className = split[1];
        for (int i = 1; i < split.length; i = i + 5) {
            if (!split[i].equals(className)) {
                i = i - 5 + 1;
                continue;
            }
            TimeTableBean mTimeBean = new TimeTableBean();
            for (int j = i; j <= i + 3; j++) {
                String t = removeHtml(split[j]);
                if (t != null && t.equals("空")) {
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
     * @param listener            个人信息回调
     */

    private void toGradeQurry(final String year, @Nullable final Bean_l bean, final
    IOnQuerySourceListener
            querySourceListener, final IPersonListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("Cookie", bean.getCookies());
        map.put("Connection", " Keep-Alive");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0)" +
                " like Gecko");
        map.put("Referer", "http://jwxt.sontan.net/xs_main.aspx?xh=" + bean.getStuNo());
        final String url = "http://jwxt.sontan.net/xscjcx.aspx?xh=" +
                bean.getStuNo() + "&xm=学生&gnmkdm=N121605";
        instance.GetRequest(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                i("000", e.toString() + "成绩失败了");
                if (querySourceListener != null) {
                    querySourceListener.OnError();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) {
                    MyLog.i("进入成绩成功了");
                    //接下来点击历年成绩，但是首先要去获取到页面上的__VIEWSTATE
                    InputStream inputStream = response.body().byteStream();
                    if (inputStream == null) {
                        return;
                    }
                    Document document;
                    try {
                        document = Jsoup.parse(inputStream, "gb2312", url);
                        Element element = document.select("form#Form1").first();
                        if (element != null) {
                            viewStateForPerson = element.select("input").get(2).val();
                        } else {
                            if (querySourceListener != null) {
                                querySourceListener.OnError();
                            }
                        }
                    } catch (IOException e) {
                        if (querySourceListener != null) {
                            querySourceListener.OnError();
                        }
                    } finally {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }


                    if (flag) {
                        flag = false;
                        if (listener != null) {
                            pastScouce(bean, viewStateForPerson, listener);
                        }
                        return;
                    }
                    switch (year) {
                        case "":    //默认开始历年成绩查询
                            pastScouce(bean, viewStateForPerson, "", "", querySourceListener);
                            break;
                        case "历年成绩":   //选择的是历年成绩查询拿一项
                            pastScouce(bean, viewStateForPerson, "", "", querySourceListener);
                            break;
                        default:
                            StringBuilder builder = new StringBuilder(year);
                            String[] s = new String[5];
                            int index = builder.lastIndexOf("-");
                            s[0] = builder.substring(0, index);//年
                            s[1] = builder.substring(index + 1);//学期
                            MyLog.i(s[0] + s[1] + "这是学期学年数据");
                            pastScouce(bean, viewStateForPerson, s[0], s[1], querySourceListener);
                            break;
                    }
                }
            }
        });
    }

    private void pastScouce(Bean_l bean, final String substring1, final IPersonListener
            personListener) {
        String utf8Togb2312 = utf8Togb2312(stuName);
        final String url = "http://jwxt.sontan.net/xscjcx.aspx?xh=" + bean.getStuNo() +
                "&xm=" + stuName + "&gnmkdm=N121605";
        i("000", url);
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
                if (personListener != null) {
                    personListener.onPersonQueryError();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) {
                    InputStream inputStream = response.body().byteStream(); //正确的，可以进行数据裁剪
                    Document document;
                    try {
                        document = Jsoup.parse(inputStream, "gb2312", url);
                        BeanPerson person = getPerson(document);
                        if (personListener != null) {
                            if (person != null) {
                                personListener.onPersonQuerySuccess(person);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }
        }, substring1, "", "", "空");
    }

    private BeanPerson getPerson(Document document) {
        BeanPerson person = new BeanPerson();
        Element element = document.select("#lbl_xh").first();       //学号
        if (element != null) {
            String s = element.text();
            person.setId(sq(s));
        }
        Element elements1 = document.select("#lbl_xm").first();     //姓名
        if (elements1 != null) {
            String nem = elements1.text();

            person.setName(sq(nem));
        }
        Element elements3 = document.select("#lbl_zymc").first();       //专业
        if (elements3 != null) {
            person.setMajor(elements3.text());
        }
        Element elements4 = document.select("#lbl_zyfx").first();       //专业方向
        if (elements4 != null) {
            String[] split = elements4.text().split(":");
            person.setOrientation(split[1]);
        }
        Element elements5 = document.select("#lbl_xzb").first();    //班级
        if (elements5 != null) {
            String nem = elements5.text();
            person.setTheClass(sq(nem));
        }
        Element elements6 = document.select("#xftj").first();
        Element elements7 = document.select("#pjxfjd").first();
        Element elements8 = document.select("#zyzrs").first();
        //  '所选学分120；获得学分118；重修学分0；正考未通过学分 2。',
        if (elements6 != null && elements7 != null && elements8 != null) {
            String builder = elements6.text() + "；" + elements7.text() + "；" +
                    elements8.text();
            String repickStr = parse(builder);
            String[] split = repickStr.split("；");
            MyLog.i(repickStr);
            List<Float> list = new ArrayList<>();
            try {
                list.add(Float.valueOf(split[0].trim()));
                list.add(Float.valueOf(split[1].trim()));
                list.add(Float.valueOf(split[2].trim()));
                list.add(Float.valueOf(split[3].trim()));
                list.add(Float.valueOf(split[4].trim()));
                list.add(Float.valueOf(split[5].trim()));
            } catch (NumberFormatException e) {    //出错了，让他默认都是0
                list.clear();
                for (int i = 0; i < 4; i++) {
                    list.add(0f);
                }
            }
            person.setList(list);
        }
        return person;
    }

    /**
     * 去掉不必要的汉字
     *
     * @param text 要出除的字符串
     * @return 返回一个新的字符串
     */
    private String parse(String text) {
        String reg = "[\\u4e00-\\u9fa5。：\\s+]";     //去掉汉字和中文的。：
        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(text);
        return mat.replaceAll("");
    }

    /**
     * 对传过来的数据进行中文符号：的分割
     *
     * @param s 源字符串
     * @return 返回分割后的字符串[1]
     */
    private String sq(String s) {
        String[] split = s.split("：");
        return split[1];
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
        final String url = "http://jwxt.sontan.net/xscjcx.aspx?xh=" + bean.getStuNo() +
                "&xm=" + stuName + "&gnmkdm=N121605";
        i("000", url);
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
                if (querySourceListener != null) {
                    querySourceListener.OnError();
                }

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws
                    IOException {
                if (response.code() == 200) {
                    if (response.body().byteStream() != null) {
                        //fialcast()                                    老方法
                        cast(response.body().byteStream(), url);      //改进方法
                        if (querySourceListener != null) {
                            querySourceListener.OnQuerySuccess(list);
                        }
                    }
                }
            }
        }, substring1, s, substring, "");
    }


    /**
     * 进行数据的解析工作,调用jsoup库
     *
     * @param inputStream inputstream
     * @param url         jsoup需要的url
     */
    private void cast(InputStream inputStream, String url) {
        try {
            if (list.size() > 0) {
                list.clear();
            }
            if (inputStream == null) {
                return;
            }
            Document document = Jsoup.parse(inputStream, "gb2312", url);
            Element element = document.select("table.datelist").first();
            //取出所有的<tr>标签数量，注意这时的数据已经是datalist里面的了，即正确的
            int size = element.select("tr").size();
            Elements elements = element.select("tr");
            float sum = 0;                      //平均分
            int score_count = size - 1;         //课程总数
            if (score_count == 0) {
                return;
            }
            for (int i = 1; i < size; i++) {    //第一个tr不是我们想要的，下标是0
                String name = elements.get(i).select("tr").select("td").get(3).text();   //课程名
                String score = elements.get(i).select("tr").select("td").get(8).text();   //成绩
                sum = Float.valueOf(score) + sum;
                list.add(name + "空" + score);
            }
            float range = sum / score_count;
            list.add("平均成绩空" + range);
        } catch (Exception e) {
            list.clear();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 将名字转成gb2312编码，并拼接返回,注意此处如果字符串中包含数字，要进行特殊处理
     *
     * @param name 源字符串
     * @return 返回拼接后的数据
     */
    private String utf8Togb2312(String name) {
        if (name == null) {
            return null;
        }

        //判断是否包含数字
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(name);
        String number = null;
        while (matcher.find()) {    //找到数字
            number = matcher.group(0);
            name = name.replaceAll("\\d+", "");
        }


        byte[] bytes = new byte[0];//先把字符串按gb2312转成byte数组
        try {
            bytes = name.getBytes("gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] a = new String[20];
        int i = 0;
        for (byte b : bytes) {//循环数组
            String s = Integer.toHexString(b);//再用Integer中的方法，把每个byte转换成16进制输出
            //   MyLog.i(s);
            String substring = s.substring(6, 8).toUpperCase();//可以了
            a[i] = substring;
            i++;
            //   MyLog.i(substring);
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
        if (number != null) {   //证明之前有进入while,即有字符串
            builder.append(number);
        }
        for (String anA : a) {
            if (anA == null) {
                continue;
            }
            builder.append("%");
            builder.append(anA);
        }
        //   MyLog.i(builder.toString() + "拼接完成");
        return builder.toString();
    }
}
