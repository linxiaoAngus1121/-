package cn.my.forward.okhttp.builder;

/**
 * Created by 123456 on 2018/2/10.
 */

public class MyRequest {

    private String url;
    private String method;
    private String Cookie;
    private String Connection;
    private String User_Agent;
    private String Referer;
    private String Content_Type;
    private String Accept;

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getCookie() {
        return Cookie;
    }

    public String getConnection() {
        return Connection;
    }

    public String getUser_Agent() {
        return User_Agent;
    }

    public String getReferer() {
        return Referer;
    }

    public String getContent_Type() {
        return Content_Type;
    }

    public String getAccept() {
        return Accept;
    }

    public String getContent_Length() {
        return Content_Length;
    }

    public String getAccept_Language() {
        return Accept_Language;
    }

    private String Content_Length;
    private String Accept_Language;

    private MyRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        Cookie = builder.cookie;
        Connection = builder.connection;
        User_Agent = builder.user_Agent;
        Referer = builder.referer;
        Content_Type = builder.content_Type;
        Accept = builder.accept;
        Content_Length = builder.content_Length;
        Accept_Language = builder.accept_Language;
    }



    public static class Builder {

        private String url;
        private String method;
        private String cookie;
        private String connection;
        private String user_Agent;
        private String referer;
        private String content_Type;
        private String accept;
        private String content_Length;
        private String accept_Language;


        public Builder() {
            this.method = "GET";
            this.connection = "Keep-Alive";
            this.user_Agent = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0)" +
                    " like Gecko";
            this.content_Type = "application/x-www-form-urlencoded";
            this.accept_Language = "zh-CN,zh;q=0.8";
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder get() {
            this.method = "GET";
            return this;
        }

        public Builder cookies(String cookie) {
            this.cookie = cookie;
            return this;
        }

        public Builder referer(String referer) {
            this.referer = referer;
            return this;
        }

        public Builder accept(String accept) {
            this.accept = accept;
            return this;
        }

        public Builder content_Length(String content_Length) {
            this.content_Length = content_Length;
            return this;
        }

        public MyRequest build() {
            return new MyRequest(this);
        }

    }
}
