package cn.my.forward.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;
import cn.my.forward.mvp.sourcequery.mvp.utils.DBUtil;
import cn.my.forward.mvp.sourcequery.mvp.utils.Md5Utils;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;

/**
 * Created by 123456 on 2018/5/14.
 * 保存用户登陆信息的service
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
            ArrayList<String> list = intent.getStringArrayListExtra("list");    //用户点击了什么功能
            String password = Md5Utils.md5Password(login.getStuPs());           //加密用户密码
            Connection con;
            ResultSet rs = null;
            try {
                con = DBUtil.getConnection();
                String sql = "select * from stu where xuehao='" + login.getStuNo() + "' and " +
                        "password='" + password + "'";
                Statement stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    //之前已经存在数据了，那就取出名字和id(注意此处不取学号)
                    MyLog.i("之前就有了，或者插入成功了");
                    //如果修改了密码的话
                    if (!Objects.equals(rs.getString("password"), password)) {    //证明修改了密码
                        String update = "update 'stu' set 'password'='" + password + "'where " +
                                "xuehao='" + login.getStuNo() + "'";
                        Statement stmt1 = con.createStatement();
                        int rs1 = stmt1.executeUpdate(update);
                        if (rs1 > 0) {
                            MyLog.i("更新密码");
                        }
                    }
                } else {
                    String sql1 = "insert into stu(xuehao,password,name)  values(?,?,?)";
                    //这个是插入新用户的
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
                String sql2 = "insert into login(login_stu_no,login_name,login_data,login_dowhat)" +
                        "  values(?,?,?,?)";                               //插入每个用户登陆系统的时间，干了什么
                PreparedStatement prepareStatement = con.prepareStatement(sql2);
                prepareStatement.setString(1, login.getStuNo());
                prepareStatement.setString(2, login.getName());
                Date date = new Date();
                Timestamp t = new Timestamp(date.getTime());
                prepareStatement.setTimestamp(3, t);
                StringBuilder builder = new StringBuilder(list.size());
                for (int i = 0; i < list.size(); i++) {
                    builder.append(i + 1);
                    builder.append(".");
                    builder.append(list.get(i));
                }
                prepareStatement.setString(4, builder.toString());
                int update = prepareStatement.executeUpdate();
                if (update >= 1) {
                    MyLog.i("插入成功了222");
                } else {
                    MyLog.i("插入失败了2222");
                }

            } catch (Exception e) {
                MyLog.i("数据库链接时遇到了问题");
            } finally {
                try {
                    assert rs != null;
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
