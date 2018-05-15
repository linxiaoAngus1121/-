package cn.my.forward.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;
import cn.my.forward.mvp.sourcequery.mvp.utils.DBUtil;
import cn.my.forward.mvp.sourcequery.mvp.utils.Md5Utils;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;

/**
 * Created by 123456 on 2018/5/14.
 */

public class LoginService extends IntentService {


    public LoginService() {
        super("name");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LoginService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.i("我创建了");
    }

    //执行在子线程中
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Bean_l login = intent.getParcelableExtra("information");
            String password = Md5Utils.md5Password(login.getStuPs());
            try {
                Connection con = DBUtil.getConnection();
                String sql = "select * from stu where xuehao='" + login.getStuNo() + "' and " +
                        "password='" + password + "'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    //之前已经存在数据了
                    MyLog.i("之前就有了，或者插入成功了");
                } else {
                    String sql1 = "insert into stu(xuehao,password,name)  values(?,?,?)";
                    PreparedStatement prepareStatement = con.prepareStatement(sql1);
                    prepareStatement.setString(1, login.getStuNo());
                    prepareStatement.setString(2, password);
                    prepareStatement.setString(3, login.getName());
                    int update = prepareStatement.executeUpdate();
                    if (update >= 1) {
                        MyLog.i("插入成功了");
                    } else {
                        MyLog.i("插入失败了");
                    }
                }

            } catch (Exception e) {
                MyLog.i("数据库链接时遇到了问题");
            } finally {
                DBUtil.Close();
            }


        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.i("我销毁了");
    }
}
