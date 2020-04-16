package com.hand.handtruck.ui.form.bean;

import java.io.Serializable;

public class FormBean implements Serializable {

    private String carNumber;
    private String address;
    private String factoryCode;
    private String factoryName;
    private String orderCode;
    private String sendWeight;
    private String saleAreaName;
    private String leaveCode;
    private String saleAreaId;
    private String custName;

    private String custId;
    private String prodName;
    private String packType;
    private String emptyLoadTime;
    private String emptyLoadWeight;
    private String fullLoadTime;
    private String fullLoadWeight;
    private String leaveTime;
    private String salType;
    private DevOrder deviceOrder;


    private String orderStatus;   //订单状态 0 运输中 1 卸货完成
    private String unloadEndTime;
    private String deviceId;
    private String createTime;
    private String financialCheckMsg;  ;//;//总经理审核意见
    private String financialCheckStatus;  ;// 0-未审核   1-审核通过  2审核不通过
    private String id;
    private String updateTime;
    private String railCheckStatus; ;//0未审核  1正常卸货、2异常卸货  3设备异常  4.数据异常 5.未配置围栏
    private String checkImgUrl; ;//;//人工审核上传图片路径
    private String detailMsg; ;//;//人工处理备注信息
    private String detailStatus; ;//人工处理状态，0未处理、1已处理
    private String marketCheckMsg;;//销售部审核意见
    private String marketCheckStatus; ;//物流部审核状态0-未审核1-审核通过2审核不通过
    private String salesCheckMsg;;//分管领导审核意见
    private String salesCheckStatus;;//分管领导审核状态0-未审核1-审核通过2审核不通过

    private String   level1CheckMsg;// "33" //level1是销售部审核    level2区域经理   level3是分管领导
    private String level1CheckStatus;// 2        0未审核、1已审核、2审核不通过
    private String  level1CheckTime;// "2019-11-26 14;//33;//28"
    private String level1CheckUser;// "超级管理员"

    private String level2CheckMsg;// ""
    private String level2CheckStatus;// 2         0未审核、1已审核、2审核不通过
    private String level2CheckTime;// "2019-11-26 14;//33;//43"
    private String  level2CheckUser;// "超级管理员"

    private String  level3CheckMsg;// ""
    private String level3CheckStatus;// 1         0未审核、1已审核、2审核不通过
    private String level3CheckTime;// "2019-11-26 14;//33;//51"
    private String level3CheckUser;// "超级管理员"

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getSendWeight() {
        return sendWeight;
    }

    public void setSendWeight(String sendWeight) {
        this.sendWeight = sendWeight;
    }

    public String getSaleAreaName() {
        return saleAreaName;
    }

    public void setSaleAreaName(String saleAreaName) {
        this.saleAreaName = saleAreaName;
    }

    public String getLeaveCode() {
        return leaveCode;
    }

    public void setLeaveCode(String leaveCode) {
        this.leaveCode = leaveCode;
    }

    public String getSaleAreaId() {
        return saleAreaId;
    }

    public void setSaleAreaId(String saleAreaId) {
        this.saleAreaId = saleAreaId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getEmptyLoadTime() {
        return emptyLoadTime;
    }

    public void setEmptyLoadTime(String emptyLoadTime) {
        this.emptyLoadTime = emptyLoadTime;
    }

    public String getEmptyLoadWeight() {
        return emptyLoadWeight;
    }

    public void setEmptyLoadWeight(String emptyLoadWeight) {
        this.emptyLoadWeight = emptyLoadWeight;
    }

    public String getFullLoadTime() {
        return fullLoadTime;
    }

    public void setFullLoadTime(String fullLoadTime) {
        this.fullLoadTime = fullLoadTime;
    }

    public String getFullLoadWeight() {
        return fullLoadWeight;
    }

    public void setFullLoadWeight(String fullLoadWeight) {
        this.fullLoadWeight = fullLoadWeight;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getSalType() {
        return salType;
    }

    public void setSalType(String salType) {
        this.salType = salType;
    }

    public DevOrder getDeviceOrder() {
        return deviceOrder;
    }

    public void setDeviceOrder(DevOrder deviceOrder) {
        this.deviceOrder = deviceOrder;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreateTime() {
        return createTime;
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

    public String getFinancialCheckStatus() {
        return financialCheckStatus;
    }

    public void setFinancialCheckStatus(String financialCheckStatus) {
        this.financialCheckStatus = financialCheckStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRailCheckStatus() {
        return railCheckStatus;
    }

    public void setRailCheckStatus(String railCheckStatus) {
        this.railCheckStatus = railCheckStatus;
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

    public String getMarketCheckStatus() {
        return marketCheckStatus;
    }

    public void setMarketCheckStatus(String marketCheckStatus) {
        this.marketCheckStatus = marketCheckStatus;
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

    public String getUnloadEndTime() {
        return unloadEndTime;
    }

    public void setUnloadEndTime(String unloadEndTime) {
        this.unloadEndTime = unloadEndTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLevel1CheckMsg() {
        return level1CheckMsg;
    }

    public void setLevel1CheckMsg(String level1CheckMsg) {
        this.level1CheckMsg = level1CheckMsg;
    }

    public String getLevel1CheckStatus() {
        return level1CheckStatus;
    }

    public void setLevel1CheckStatus(String level1CheckStatus) {
        this.level1CheckStatus = level1CheckStatus;
    }

    public String getLevel1CheckTime() {
        return level1CheckTime;
    }

    public void setLevel1CheckTime(String level1CheckTime) {
        this.level1CheckTime = level1CheckTime;
    }

    public String getLevel1CheckUser() {
        return level1CheckUser;
    }

    public void setLevel1CheckUser(String level1CheckUser) {
        this.level1CheckUser = level1CheckUser;
    }

    public String getLevel2CheckMsg() {
        return level2CheckMsg;
    }

    public void setLevel2CheckMsg(String level2CheckMsg) {
        this.level2CheckMsg = level2CheckMsg;
    }

    public String getLevel2CheckStatus() {
        return level2CheckStatus;
    }

    public void setLevel2CheckStatus(String level2CheckStatus) {
        this.level2CheckStatus = level2CheckStatus;
    }

    public String getLevel2CheckTime() {
        return level2CheckTime;
    }

    public void setLevel2CheckTime(String level2CheckTime) {
        this.level2CheckTime = level2CheckTime;
    }

    public String getLevel2CheckUser() {
        return level2CheckUser;
    }

    public void setLevel2CheckUser(String level2CheckUser) {
        this.level2CheckUser = level2CheckUser;
    }

    public String getLevel3CheckMsg() {
        return level3CheckMsg;
    }

    public void setLevel3CheckMsg(String level3CheckMsg) {
        this.level3CheckMsg = level3CheckMsg;
    }

    public String getLevel3CheckStatus() {
        return level3CheckStatus;
    }

    public void setLevel3CheckStatus(String level3CheckStatus) {
        this.level3CheckStatus = level3CheckStatus;
    }

    public String getLevel3CheckTime() {
        return level3CheckTime;
    }

    public void setLevel3CheckTime(String level3CheckTime) {
        this.level3CheckTime = level3CheckTime;
    }

    public String getLevel3CheckUser() {
        return level3CheckUser;
    }

    public void setLevel3CheckUser(String level3CheckUser) {
        this.level3CheckUser = level3CheckUser;
    }
}
