package cn.my.forward.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cn.my.forward.mvp.sourcequery.mvp.ListForSaveData;
import cn.my.forward.mvp.sourcequery.mvp.User;
import cn.my.forward.mvp.sourcequery.mvp.bean.BeanPerson;
import cn.my.forward.mvp.sourcequery.mvp.bean.JsonRoot;
import cn.my.forward.mvp.sourcequery.mvp.bean.LevelBean;
import cn.my.forward.mvp.sourcequery.mvp.utils.DBUtil;
import cn.my.forward.mvp.sourcequery.mvp.utils.Md5Util;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;

/**
 * Created by 123456 on 2018/5/14.
 * 保存用户登陆信息的service
 */

public class LoginService extends IntentService {

    public static final String INSERTUSERURL = "insert into stu(xuehao,password,name)  values(?," +
            "?,?)";
    public static final String INSERTBUGURL = "insert into login(login_stu_no,login_name," +
            "login_data,login_dowhat)" +
            "  values(?,?,?,?)";

    public static final String INSERTLEVELURL = "INSERT INTO level(stu_no, stu_name,level_data) " +
            "VALUES (?,?,?)";


    public static final String UPDATELEVELDATAURL = "update level set level_data=? where stu_no=?";


    public static final String UPDATEINFORMATIONURL = "update level set information_data=? where " +
            "stu_no=?";

    public static final String SELECTLEVELURL = "select * from level where stu_no=?";

    public static final String SELECTSOURCEID = "select realsource from source where stuid=?";

    public static final String UPDATESOURCE = "update source set realsource= ? where stuid=?";

    public static final String INSERTSOURCE = "insert into source(stuid,realsource) values(?,?)";

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
            User login = User.getInstance();    //当前登陆用户的信息
            ArrayList<String> list = intent.getStringArrayListExtra("list");    //用户点击了什么功能
            String password = Md5Util.md5Password(login.getStuPs());           //加密用户密码
            Connection con;
            ResultSet rs = null;    //用户信息的set
            ResultSet resultSet = null; //是否有成绩信息的set
            ResultSet query = null; //成绩信息的set
            Gson gson = new Gson();
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
                    //这个是插入新用户的
                    PreparedStatement prepareStatement = con.prepareStatement(INSERTUSERURL);
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
                //插入每个用户登陆系统的时间，干了什么
                PreparedStatement prepareStatement = con.prepareStatement(INSERTBUGURL);
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

                //执行保存等级考试查询、个人信息查询信息到数据库,先查询是否有记录
                //考虑将list转为Json
                PreparedStatement statement = con.prepareStatement(SELECTLEVELURL);
                statement.setInt(1, Integer.valueOf(login.getStuNo()));

                ListForSaveData data = ListForSaveData.getInstance();
                List<LevelBean> leList = data.getLeList();
                BeanPerson person = data.getPerson();
                String lev_toJson = gson.toJson(leList);
                String info_json = gson.toJson(person);
                ResultSet set = statement.executeQuery();
                if (set.next()) {    //代表之前就有数据了,还要比较数据是否发生了变化
                    String levelData = set.getString("level_data");
                    if (null != levelData) {
                        MyLog.i("之前就有level数据了");
                        if (!"null".equals(lev_toJson)) {
                            if (!levelData.equals(lev_toJson)
                                    ) {  //level需要更新数据
                                PreparedStatement preparedStatement = con.prepareStatement
                                        (UPDATELEVELDATAURL);
                                preparedStatement.setString(1, lev_toJson);
                                preparedStatement.setInt(2, Integer.valueOf(login.getStuNo()));
                                int rs1 = preparedStatement.executeUpdate();
                                if (rs1 > 0) {
                                    MyLog.i("更新4，6级信息");
                                }
                            }
                        }

                    } else {        //代表之前没有下面
                        if (!lev_toJson.equals("null")) {    //需要更新数据了，
                            PreparedStatement preparedStatement = con.prepareStatement
                                    (UPDATELEVELDATAURL);
                            preparedStatement.setString(1, lev_toJson);
                            preparedStatement.setInt(2, Integer.valueOf(login.getStuNo()));
                            int rs1 = preparedStatement.executeUpdate();
                            if (rs1 > 0) {
                                MyLog.i("更新4，6级信息");
                            }
                        }
                    }
                    //假设之前有上面的，但是没有下面的，现在带了下面的过来了，那就要更新了
                    String information_data = set.getString("information_data");
                    if (null != information_data) {    //代表有下面的
                        if (!"null".equals(info_json)) {
                            if (!information_data.equals
                                    (info_json)) {
                                PreparedStatement statement1 = con.prepareStatement
                                        (UPDATEINFORMATIONURL);
                                statement1.setString(1, info_json);
                                statement1.setInt(2, Integer.valueOf(login.getStuNo()));
                                int rs1 = statement1.executeUpdate();
                                if (rs1 > 0) {
                                    MyLog.i("更新inforamtion信息");
                                }
                            }
                        }
                    } else {        //代表之前没有下面
                        if (!TextUtils.isEmpty(info_json)) {    //需要更新数据了，
                            PreparedStatement statement1 = con.prepareStatement
                                    (UPDATEINFORMATIONURL);
                            statement1.setString(1, info_json);
                            statement1.setInt(2, Integer.valueOf(login.getStuNo()));
                            int rs1 = statement1.executeUpdate();
                            if (rs1 > 0) {
                                MyLog.i("更新inforamtion信息");
                            }
                        }
                    }

                    //保存成绩信息到数据库
                    PreparedStatement statement1 = con.prepareStatement(SELECTSOURCEID);
                    statement1.setInt(1, Integer.valueOf(login.getStuNo()));
                    //保存成绩信息到数据库
                    resultSet = statement1.executeQuery();
                    SparseArray<ArrayList<String>> map = ListForSaveData.getInstance()
                            .getAllMap();
                    int size = map.size();
                    if (resultSet.next()) {
                        MyLog.i("有数据");
                        String sourceId = resultSet.getString("realsource");        //之前的数据
                        if (size > 0) {//证明用户点击过成绩查询
                            //保存到数据库
                            String json = gson.toJson(map);
                            JsonRoot root = gson.fromJson(sourceId, JsonRoot.class);
                            if (root.getMSize() == 7) {     //=7说明之前保存好了所有的信息
                                //不需要更新数据了
                                MyLog.i("不需要更新数据了");
                                return;
                            }
                            MyLog.i(json);
                            if (!"null".equals(json) && !json.equals(sourceId)) {
                                //更新数据
                                PreparedStatement statement2 = con.prepareStatement(UPDATESOURCE);
                                statement2.setString(1, json);
                                statement2.setInt(2, Integer.valueOf(login.getStuNo()));
                                int i = statement2.executeUpdate();
                                if (i > 0) {
                                    MyLog.i("更新成功了5555");
                                } else {
                                    MyLog.i("更新失败了5555");
                                }
                            }
                        }
                    } else {        //没数据
                        if (size > 0) {//证明用户点击过成绩查询
                            //保存到数据库
                            String json = gson.toJson(map);
                            MyLog.i(json);
                            PreparedStatement statement2 = con.prepareStatement
                                    (INSERTSOURCE);
                            statement2.setInt(1, Integer.valueOf(login.getStuNo()));
                            statement2.setString(2, json);
                            int i = statement2.executeUpdate();
                            if (i > 0) {
                                MyLog.i("插入成功6666");
                            } else {
                                MyLog.i("插入失败6666");
                            }

                        }
                    }
                    return;
                }
                //这里能否直接插入两个数据呢？？？？不能,既然走到这里，就说明是要插入的
                if (null != leList) {
                    PreparedStatement preparedStatement = con.prepareStatement(INSERTLEVELURL);
                    preparedStatement.setInt(1, Integer.valueOf(login.getStuNo()));
                    preparedStatement.setString(2, login.getName());
                    preparedStatement.setString(3, lev_toJson);
                    int i = preparedStatement.executeUpdate();
                    if (i >= 1) {
                        MyLog.i("插入成功了333");
                    } else {
                        MyLog.i("插入失败了333 ");
                    }
                }
                if (null != person) {
                    PreparedStatement preparedStatement01 = con.prepareStatement
                            (UPDATEINFORMATIONURL);
                    preparedStatement01.setString(1, info_json);
                    preparedStatement01.setInt(2, Integer.valueOf(login.getStuNo()));
                    int y = preparedStatement01.executeUpdate();
                    if (y >= 1) {
                        MyLog.i("插入成功了444");
                    } else {
                        MyLog.i("插入失败了444 ");
                    }
                }




                   /* //查询成绩
                    PreparedStatement statement2 = con.prepareStatement(SELECTSOURCE);
                    statement2.setInt(1, sourceId);
                    query = statement2.executeQuery();
                    if (query.next()) {
                        String realsource = query.getString("realsource");//真正的成绩信息
                        gson.fromJson(realsource, )
                    }*/


            } catch (Exception e) {
                MyLog.i(e.toString() + "数据库链接出问题");
                MyLog.i("任意环节出现问题");
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (resultSet != null) {
                        resultSet.close();
                    }
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
