package cn.my.forward.mvp.sourcequery.mvp.bean;

import java.util.List;

/**
 * Created by 123456 on 2018/5/8.
 * 个人信息实体类
 */

public class BeanPerson {
    private String name;
    private String id;
    //专业
    private String major;
    //系别
    private String college;
    private String orientation;
    private String theClass;
    private List<Float> list;

    public List<Float> getList() {
        return list;
    }

    public void setList(List<Float> list) {
        this.list = list;
    }


    private String gradePoint;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getTheClass() {
        return theClass;
    }

    public void setTheClass(String theClass) {
        this.theClass = theClass;
    }

    public String getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(String gradePoint) {
        this.gradePoint = gradePoint;
    }


}
