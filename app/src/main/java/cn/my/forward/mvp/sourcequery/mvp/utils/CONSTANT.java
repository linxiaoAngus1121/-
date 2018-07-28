package cn.my.forward.mvp.sourcequery.mvp.utils;

/**
 * Created by 123456 on 2018/7/20.
 * 常量类
 */

public class CONSTANT {
    //请求地址相关的
    //乐拍url
    public static final String LEPAIURL = "https://api-cn.faceplusplus.com/facepp/v3/detect";
    //请求首页
    public static final String STBASE = "http://jwxt.sontan.net/";

    //火车票首页
    public static final String TIBASE = "http://api.jisuapi.com/train/";

    //请求头参数相关的
    public static final String USERAGENT = "User-Agent";
    public static final String USERAGENTINFO = "Mozilla/5.0 (Windows NT 10.0; WOW64) " +
            "AppleWebKit/537.36 (KHTML, " +
            "like Gecko) Chrome/55.0.2883.87 UBrowser/6.2.3964.2 Safari/537.36";
    public static final String CONNECTION = "Connection";
    public static final String CONNECTIONINFO = "keep-alive";
    public static final String ACCEPTLANAGE = "Accept-Language";
    public static final String ACCEPTLANAGEINFO = "zh-CN,zh;q=0.8";
    public static final String ACCEPT = "Accept";
    public static final String ACCEPTINFOONE = "text/html,application/xhtml+xml,application/xml;" +
            "q=0.9,image/webp,*/*;" +
            "q=0.8";
    public static final String ACCEPTINFOTWO = "text/html, application/xhtml+xml, image/jxr, */*";
    public static final String CONTENTTYPE = "Content-Type";
    public static final String CONTENTTYPEINFO = "application/x-www-form-urlencoded";

    public static final String COOKIES = "Cookie";
    public static final String REFERER = "Referer";
    public static final String CONTENT_LENGTH = "Content-Length";

    //其他请求相关的
    public static final String EVENTTARGET = "__EVENTTARGET";
    public static final String EVENTARGUMENT = "__EVENTARGUMENT";
    public static final String VIEWSTATE = "__VIEWSTATE";
    public static final String HIDELANGUAGE = "hidLanguage";
    public static final String XN = "ddlXN";
    public static final String XQ = "ddlXQ";
    public static final String XZ = "ddl_kcxz";

    public static final String SLEVELSQL = "SELECT  level_data FROM level WHERE stu_no=?";

    public static final String SLINFORMATIONSQL = "SELECT  information_data FROM level WHERE " +
            "stu_no=?";

    public static final String SELECTSOURCE = "select realsource from source where stuid=?";
}
