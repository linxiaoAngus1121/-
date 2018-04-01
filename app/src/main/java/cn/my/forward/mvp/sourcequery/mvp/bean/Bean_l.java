package cn.my.forward.mvp.sourcequery.mvp.bean;

/**
 * Created by 123456 on 2018/2/9.
 * 登录的bean
 */

public class Bean_l {
    private String stuNo;
    private String stuPs;
    private String viewState;
    private String cookies;
    private String code;//验证码

    public Bean_l(String stuNo, String stuPs, String viewState, String cookies, String code) {
        this.stuNo = stuNo;
        this.stuPs = stuPs;
        this.viewState = viewState;
        this.cookies = cookies;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Bean_l{" +
                "stuNo='" + stuNo + '\'' +
                ", stuPs='" + stuPs + '\'' +
                ", viewState='" + viewState + '\'' +
                ", cookies='" + cookies + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public Bean_l() {
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getViewState() {
        return viewState;
    }

    public void setViewState(String viewState) {
        this.viewState = viewState;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getStuPs() {
        return stuPs;
    }

    public void setStuPs(String stuPs) {
        this.stuPs = stuPs;
    }
}
