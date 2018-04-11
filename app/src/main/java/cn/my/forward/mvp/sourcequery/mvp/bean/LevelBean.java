package cn.my.forward.mvp.sourcequery.mvp.bean;

/**
 * Created by 123456 on 2018/4/9.
 * 等级考试的实体类
 */

public class LevelBean {
    private String l_name;
    private String grade;
    private String admissionTicket;

    @Override
    public String toString() {
        return "LevelBean{" +
                "l_name='" + l_name + '\'' +
                ", grade='" + grade + '\'' +
                ", admissionTicket='" + admissionTicket + '\'' +
                '}';
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAdmissionTicket() {
        return admissionTicket;
    }

    public void setAdmissionTicket(String admissionTicket) {
        this.admissionTicket = admissionTicket;
    }

    public LevelBean(String l_name, String admissionTicket, String grade) {
        this.l_name = l_name;
        this.grade = grade;
        this.admissionTicket = admissionTicket;
    }
}
