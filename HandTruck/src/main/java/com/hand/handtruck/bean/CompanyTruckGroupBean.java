package com.hand.handtruck.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wcf on 2018-03-08.
 */

public class CompanyTruckGroupBean implements Serializable {
    private String name;
    private int truckNumber;
    private int onlineNumber;
    private List<TruckChildBean> children;
    private String letters;//显示拼音的首字母

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(int truckNumber) {
        this.truckNumber = truckNumber;
    }

    public int getOnlineNumber() {
        return onlineNumber;
    }

    public void setOnlineNumber(int onlineNumber) {
        this.onlineNumber = onlineNumber;
    }

    public List<TruckChildBean> getChildren() {
        return children;
    }

    public void setChildren(List<TruckChildBean> children) {
        this.children = children;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }
}

