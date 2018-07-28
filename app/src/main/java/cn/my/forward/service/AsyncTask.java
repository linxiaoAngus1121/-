package cn.my.forward.service;

import android.content.Context;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cn.my.forward.R;
import cn.my.forward.mvp.sourcequery.mvp.utils.DBUtil;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;

/**
 * Created by 123456 on 2018/5/25.
 * 执行错误反馈的任务
 */

public class AsyncTask extends android.os.AsyncTask<String, Void, Void> {


    private Context context;
    private Button mBt;
    private static final String URL = "insert into bug(content,contact,submitter,bug_no) values" +
            "(?,?,?,?)";

    public AsyncTask(Context applicationContext, Button mBt) {
        this.context = applicationContext;
        this.mBt = mBt;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mBt.setText(R.string.submiting);

    }


    @Override
    protected Void doInBackground(String... params) {   //这边会有三个参数，第一个是bug的内容，第二个是姓名，第三个是学号
        MyLog.i(params[0] + "内容");
        MyLog.i(params[1] + "联系方式");
        MyLog.i(params[2] + "姓名");
        MyLog.i(params[3] + "学号");

        try {
            //进行意见建议的上传，获取数据库连接
            Connection con = DBUtil.getConnection();
            PreparedStatement prepareStatement = con.prepareStatement(URL);
            prepareStatement.setString(1, params[0]);
            prepareStatement.setString(2, params[1]);
            prepareStatement.setString(3, params[2]);
            prepareStatement.setString(4, params[3]);
            int update = prepareStatement.executeUpdate();
            if (update >= 1) {
                MyLog.i("bug插入成功了");
            } else {
                MyLog.i("bug插入失败了");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.Close();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mBt.setText(R.string.submit);
        super.onPostExecute(aVoid);
        Toast.makeText(context, R.string.submitsuccess, Toast.LENGTH_SHORT).show();
    }

}
