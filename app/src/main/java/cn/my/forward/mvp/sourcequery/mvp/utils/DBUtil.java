package cn.my.forward.mvp.sourcequery.mvp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static String URL = "jdbc:mysql://b29bbh4t.2368.dnstoo" +
            ".com/system_lx?useUnicode=true&characterEncoding=utf8";
    private static String DRIVERCLASS = "com.mysql.jdbc.Driver";
    private static String USERNAME = "system_lx_f";
    private static String PASSWORD = "aa123456789";
    private static Connection conn;

    //装载驱动  
    static {
        try {
            Class.forName(DRIVERCLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //获取数据库连接  
    public static Connection getConnection() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //关闭数据库连接
    public static void Close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}