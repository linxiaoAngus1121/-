package cn.my.forward.mvp.sourcequery.mvp.utils;

import java.util.ArrayList;
import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.bean.TimeTableBean;

/**
 * Created by 123456 on 2018/4/17.
 * 课程分周展示
 */

public class CoursePages {

    public static ArrayList al1 = new ArrayList(20);

    public static ArrayList xuanke(List<TimeTableBean> al5) {

        if (al1.size() != 0) {
            return al1;
        }

        al1.add(null);
        ArrayList al2 = null;
        ArrayList al3 = null;
        for (int i = 1; i <= 20; i++) {
            al2 = new ArrayList(6);
            al2.add(null);
            for (int j = 1; j <= 6; j++) {
                //al2.add(new ArrayList<Ke>(5));
                al3 = new ArrayList<TimeTableBean>(10);
                al3.add(null);
                int js = 1;
                for (int k = 1; k <= 5; k++) {
                    if ((js + 1) == 10) {
                        al3.add(k, new TimeTableBean("空", 9, 11));
                        break;
                    }
                    al3.add(k, new TimeTableBean("空", js, js + 1));
                    js += 2;
                }
                al2.add(j, al3);
            }

            al1.add(i, al2);
        }

        for (int i = 0; i < al5.size(); i++) {
            TimeTableBean node = al5.get(i);
            int s = node.getStartzhou();
            int e = node.getEndzhou();
            for (; s <= e; s++) {
                TimeTableBean tempke = new TimeTableBean(node.getName(), node.getTeacher(), node
                        .getAddress(), node.getStartzhou(), node.getEndzhou(), node.getXingqi(),
                        node.getBiaozhi(), node.getStartjie(), node.getEndjie());
                if (node.getBiaozhi() == 3) {
                    ArrayList tempal2 = (ArrayList) al1.get(s);//从第s周开始放
                    ArrayList tempal3 = (ArrayList) tempal2.get(node.getXingqi());
                    tempal3.remove((tempke.getStartjie() - 1) / 2 + 1);
                    tempal3.add((tempke.getStartjie() - 1) / 2 + 1, tempke);
                } else if (node.getBiaozhi() == 1)//单周
                {
                    if (s % 2 != 0) {
                        ArrayList tempal2 = (ArrayList) al1.get(s);//从第s周开始放
                        ArrayList tempal3 = (ArrayList) tempal2.get(node.getXingqi());
                        tempal3.remove((tempke.getStartjie() - 1) / 2 + 1);
                        tempal3.add((tempke.getStartjie() - 1) / 2 + 1, tempke);
                    }
                } else if (node.getBiaozhi() == 2) {
                    if (s % 2 == 0) {
                        ArrayList tempal2 = (ArrayList) al1.get(s);//从第s周开始放
                        ArrayList tempal3 = (ArrayList) tempal2.get(node.getXingqi());
                        tempal3.remove((tempke.getStartjie() - 1) / 2 + 1);
                        tempal3.add((tempke.getStartjie() - 1) / 2 + 1, tempke);
                    }
                }
            }
        }


        return al1;
    }
}