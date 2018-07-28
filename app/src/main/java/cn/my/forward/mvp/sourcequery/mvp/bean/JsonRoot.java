/**
 * Copyright 2018 bejson.com
 */
package cn.my.forward.mvp.sourcequery.mvp.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Auto-generated: 2018-07-26 19:21:13
 * 数据库对象所需要的实体类
 */
public class JsonRoot {

    private boolean mGarbage;
    private List<Integer> mKeys;
    private int mSize;
    private List<ArrayList<String>> mValues;

    public void setMGarbage(boolean mGarbage) {
        this.mGarbage = mGarbage;
    }

    public boolean getMGarbage() {
        return mGarbage;
    }

    public void setMKeys(List<Integer> mKeys) {
        this.mKeys = mKeys;
    }

    public List<Integer> getMKeys() {
        return mKeys;
    }

    public void setMSize(int mSize) {
        this.mSize = mSize;
    }

    public int getMSize() {
        return mSize;
    }

    public void setMValues(List<ArrayList<String>> mValues) {
        this.mValues = mValues;
    }

    public List<ArrayList<String>> getMValues() {
        return mValues;
    }

}