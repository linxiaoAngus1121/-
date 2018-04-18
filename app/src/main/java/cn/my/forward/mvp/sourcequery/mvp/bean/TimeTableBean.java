package cn.my.forward.mvp.sourcequery.mvp.bean;

/**
 * Created by 123456 on 2018/4/16.
 * 课程表分周展示实体类
 */

public class TimeTableBean {
    private String name;
    private String teacher;
    private String address;
    private int startzhou;
    private int endzhou;
    private int xingqi;     //周几
    private int biaozhi;// 1单 2双 3空
    // private String time;    //1，2节，3，4节，还是其他。。。
    private int startjie;
    private int endjie;

    public TimeTableBean() {
    }


    public TimeTableBean(String name, String teacher, String address, int startzhou, int endzhou,
                         int xingqi, int biaozhi, int startjie, int endjie) {
        this.name = name;
        this.teacher = teacher;
        this.address = address;
        this.startzhou = startzhou;
        this.endzhou = endzhou;
        this.xingqi = xingqi;
        this.biaozhi = biaozhi;
        this.startjie = startjie;
        this.endjie = endjie;
    }


    public TimeTableBean(String name, int startjie, int endjie) {
        this.name = name;
        this.startjie = startjie;
        this.endjie = endjie;
    }

    public int getStartjie() {

        return startjie;
    }

    public void setStartjie(int startjie) {
        this.startjie = startjie;
    }

    public int getEndjie() {
        return endjie;
    }

    public void setEndjie(int endjie) {
        this.endjie = endjie;
    }

    public int getStartzhou() {
        return startzhou;
    }

    public void setStartzhou(int startzhou) {
        this.startzhou = startzhou;
    }

    public int getEndzhou() {
        return endzhou;
    }

    public void setEndzhou(int endzhou) {
        this.endzhou = endzhou;
    }

    public int getXingqi() {
        return xingqi;
    }

    public void setXingqi(int xingqi) {
        this.xingqi = xingqi;
    }

    public int getBiaozhi() {
        return biaozhi;
    }

    public void setBiaozhi(int biaozhi) {
        this.biaozhi = biaozhi;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
