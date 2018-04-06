package cn.my.forward.mvp.sourcequery.mvp.bean;

/**
 * Created by 123456 on 2018/4/3.
 */

public class ExamBean {
    private String subject;
    private String time;
    private String address_no;


    public ExamBean(String subject, String time, String address_no) {
        this.subject = subject;
        this.time = time;
        this.address_no = address_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress_no() {
        return address_no;
    }

    public void setAddress_no(String address_no) {
        this.address_no = address_no;
    }
}
