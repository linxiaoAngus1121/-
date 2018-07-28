package cn.my.forward.mvp.sourcequery.mvp;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.bean.BeanPerson;
import cn.my.forward.mvp.sourcequery.mvp.bean.LevelBean;

/**
 * Created by 123456 on 2018/7/22.
 * 保存各个功能的数据
 */

public class ListForSaveData {

    private static ListForSaveData instance;
    private static List<LevelBean> leList;  //保存levelbean的List
    private static BeanPerson person;     //个人信息
    private static SparseArray<ArrayList<String>> map = new SparseArray<>();    //保存成绩信息的array


    public static ListForSaveData getInstance() {
        if (null == instance) {
            synchronized (ListForSaveData.class) {
                if (null == instance) {
                    instance = new ListForSaveData();
                }
            }
        }
        return instance;
    }


    public SparseArray<ArrayList<String>> getAllMap() {
        return map;
    }

    public ArrayList<String> getMap(int key) {
        return map.get(key);
    }

    public void setMap(int key, ArrayList<String> list) {
        map.put(key, list);
    }

    public void setLeList(List<LevelBean> leList) {

        if (null == ListForSaveData.leList) {
            ListForSaveData.leList = leList;
        }
        //判断是因为这里的数据更新比较缓慢，等到出现size不同的时候再更新
        if (null != ListForSaveData.leList && leList.size() != ListForSaveData.leList.size()) {
            ListForSaveData.leList = leList;
        }
    }

    public BeanPerson getPerson() {
        return person;
    }

    public void setPerson(BeanPerson person) {
        ListForSaveData.person = person;
    }

    public List<LevelBean> getLeList() {
        return leList;
    }
}
