package cn.my.forward.mvp.sourcequery.mvp.bean;

/**
 * Created by 123456 on 2018/2/9.
 * 查询成绩返回的实体类
 */

public class Bean_s {
    private String className;   //课程名称
    private String score;       //分数

    public Bean_s(String className, String score) {
        this.className = className;
        this.score = score;
    }

    public Bean_s() {
    }

    public String getClassName() {

        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

}