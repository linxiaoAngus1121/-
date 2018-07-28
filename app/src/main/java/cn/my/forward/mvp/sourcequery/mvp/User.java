package cn.my.forward.mvp.sourcequery.mvp;

/**
 * Created by 123456 on 2018/7/22.
 * 保存当前登陆的用户信息，例如学号，姓名
 */

public class User {
    private static User instance;
    private String stuNo;
    private String name;
    private String stuPs;

    private User() {

    }

    public static User getInstance() { //程序中这里不会出现多线程状态，因此去掉线程锁
        if (null == instance) {
            instance = new User();
        }
        return instance;
    }

    public void setInformation(String stuNo, String name, String stuPs) {
        //程序中这里不会出现多线程状态，因此去掉线程锁
        this.stuNo = stuNo;
        this.name = name;
        this.stuPs = stuPs;
    }

    public String getStuNo() {
        return stuNo;
    }

    public String getName() {
        return name;
    }

    public String getStuPs() {
        return stuPs;
    }
}
