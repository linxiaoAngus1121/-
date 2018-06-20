/**
 * Copyright 2018 bejson.com
 */
package cn.my.forward.mvp.sourcequery.mvp.bean.lepai;

/**
 * Auto-generated: 2018-06-10 9:40:20
 */
public class Beauty {

    private double female_score;
    private double male_score;

    public void setFemale_score(double female_score) {
        this.female_score = female_score;
    }

    public double getFemale_score() {
        return female_score;
    }

    public void setMale_score(double male_score) {
        this.male_score = male_score;
    }

    public double getMale_score() {
        return male_score;
    }

    @Override
    public String toString() {
        return "Beauty{" +
                "female_score=" + female_score +
                ", male_score=" + male_score +
                '}';
    }
}