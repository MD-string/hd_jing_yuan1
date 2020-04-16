package com.hand.handtruck.ui.TruckInfo.bean;

import java.io.Serializable;
import java.util.List;

public class SecondCompanyInfo implements Serializable {


    private String id;
    private String parent; //父类ID
    private String remark;
    private String text;
    private String name;
    private  String icon;
    private String companyCode;
    private List<CompanyInfo> children;
    private int  countNumber;//在线数量
    private int  sencondOnline;//在线数量

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CompanyInfo> getChildren() {
        return children;
    }

    public void setChildren(List<CompanyInfo> children) {
        this.children = children;
    }

    public int getCountNumber() {
        return countNumber;
    }

    public void setCountNumber(int countNumber) {
        this.countNumber = countNumber;
    }

    public int getSencondOnline() {
        return sencondOnline;
    }

    public void setSencondOnline(int sencondOnline) {
        this.sencondOnline = sencondOnline;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
