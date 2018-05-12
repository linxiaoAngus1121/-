package cn.my.forward.mvp.sourcequery.mvp.view;


import java.util.ArrayList;

/**
 * Created by 123456 on 2018/2/9.
 * 成绩的view
 */

public interface ISourceView {

    void showSource(ArrayList<String> list);  //展示学生成绩

    void showSourceError(String s);   //展示错误

}
