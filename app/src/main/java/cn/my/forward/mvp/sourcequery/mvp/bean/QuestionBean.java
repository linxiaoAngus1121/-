package cn.my.forward.mvp.sourcequery.mvp.bean;

import android.util.SparseArray;

/**
 * Created by 123456 on 2018/5/17.
 * 问卷调查的实体类
 */

public class QuestionBean {
    /**
     * 问题
     */
    private String question;
    /**
     * 答案
     */
    private SparseArray<String> answer;

    private SparseArray<Boolean> array;

    private int location;
    private boolean flag;

    public void setLocationAndFlag(int location, boolean flag) {
        this.location = location;
        this.flag = flag;

    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public SparseArray<String> getAnswer() {
        return answer;
    }

    public void setAnswer(SparseArray<String> answer) {
        this.answer = answer;
    }
}
