package com.hand.handtruck.ui.form.bean;

import java.io.Serializable;

public class OrderStatus implements Serializable {

    private String createTime;
    private String financialCheckMsg;  ////总经理审核意见
    private String financialCheckStatus;  // 0-未审核   1-审核通过  2审核不通过
    private String id;
    private String orderCode;
    private String updateTime;
    private String railCheckStatus; // 0-未审核  1-正常卸货  2异常卸货
    private String checkImgUrl; ////人工审核上传图片路径
    private String detailMsg; ////人工处理备注信息
    private String detailStatus; //人工处理状态，0未处理、1已处理
    private String marketCheckMsg;//物流部审核意见
    private String marketCheckStatus; //物流部审核状态0-未审核1-审核通过2审核不通过
    private String salesCheckMsg;//分管领导审核意见
    private String salesCheckStatus;//分管领导审核状态0-未审核1-审核通过2审核不通过

    public String getMarketCheckStatus() {
        return marketCheckStatus;
    }

    public void setMarketCheckStatus(String marketCheckStatus) {
        this.marketCheckStatus = marketCheckStatus;
    }

    public String getCheckImgUrl() {
        return checkImgUrl;
    }

    public void setCheckImgUrl(String checkImgUrl) {
        this.checkImgUrl = checkImgUrl;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }

    public String getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(String detailStatus) {
        this.detailStatus = detailStatus;
    }

    public String getMarketCheckMsg() {
        return marketCheckMsg;
    }

    public void setMarketCheckMsg(String marketCheckMsg) {
        this.marketCheckMsg = marketCheckMsg;
    }


    public String getSalesCheckMsg() {
        return salesCheckMsg;
    }

    public void setSalesCheckMsg(String salesCheckMsg) {
        this.salesCheckMsg = salesCheckMsg;
    }

    public String getSalesCheckStatus() {
        return salesCheckStatus;
    }

    public void setSalesCheckStatus(String salesCheckStatus) {
        this.salesCheckStatus = salesCheckStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getFinancialCheckStatus() {
        return financialCheckStatus;
    }

    public String getRailCheckStatus() {
        return railCheckStatus;
    }

    public void setRailCheckStatus(String railCheckStatus) {
        this.railCheckStatus = railCheckStatus;
    }

    public void setFinancialCheckStatus(String financialCheckStatus) {
        this.financialCheckStatus = financialCheckStatus;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinancialCheckMsg() {
        return financialCheckMsg;
    }

    public void setFinancialCheckMsg(String financialCheckMsg) {
        this.financialCheckMsg = financialCheckMsg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
