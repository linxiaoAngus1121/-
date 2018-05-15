package cn.my.forward.mvp.sourcequery.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 123456 on 2018/2/9.
 * 登录的bean
 */

public class Bean_l implements Parcelable {
    private String stuNo;
    private String stuPs;
    private String viewState;
    private String cookies;
    private String code;//验证码
    private String name;

    private Bean_l(Parcel in) {
        stuNo = in.readString();
        stuPs = in.readString();
        viewState = in.readString();
        cookies = in.readString();
        code = in.readString();
        name = in.readString();
    }

    public static final Creator<Bean_l> CREATOR = new Creator<Bean_l>() {
        @Override
        public Bean_l createFromParcel(Parcel in) {
            return new Bean_l(in);
        }

        @Override
        public Bean_l[] newArray(int size) {
            return new Bean_l[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(stuNo);
        dest.writeString(stuPs);
        dest.writeString(viewState);
        dest.writeString(cookies);
        dest.writeString(code);
        dest.writeString(name);
    }
}
