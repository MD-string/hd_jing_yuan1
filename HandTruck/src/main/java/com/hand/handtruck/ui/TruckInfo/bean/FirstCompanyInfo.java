package com.hand.handtruck.ui.TruckInfo.bean;

import java.io.Serializable;
import java.util.List;

public class FirstCompanyInfo implements Serializable {


    private String id;
    private String parent; //父类ID
    private String remark;
    private String text;
    private String name;
    private List<SecondCompanyInfo> children;
    private String companyCode;

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

    public List<SecondCompanyInfo> getChildren() {
        return children;
    }

    public void setChildren(List<SecondCompanyInfo> children) {
        this.children = children;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
