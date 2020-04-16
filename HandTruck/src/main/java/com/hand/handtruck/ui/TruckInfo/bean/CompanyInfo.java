package com.hand.handtruck.ui.TruckInfo.bean;

import java.io.Serializable;
import java.util.List;

public class CompanyInfo implements Serializable {


    private String icon;  // 有online  表示在线
    private String id;
    private String parent; //父类ID
    private String remark;
    private String text;
    private List<CarInfo> carList;
    private String name;
    private int  onLineNumber;//在线数量
    private String tag;
    private String companyCode;
    private String companyName;


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getOnLineNumber() {
        return onLineNumber;
    }

    public void setOnLineNumber(int onLineNumber) {
        this.onLineNumber = onLineNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<CarInfo> getCarList() {
        return carList;
    }

    public void setCarList(List<CarInfo> carList) {
        this.carList = carList;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
