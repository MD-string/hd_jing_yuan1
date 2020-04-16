package com.hand.handtruck.ui.TransportThing.bean;

import java.io.Serializable;

/**
 * Created by wcf on 2018-05-03.
 */

public class TransportBean implements Serializable {
    private String address;
    private String carNumber;
    private String createTime;
    private String deviceId;
    private String endTime;
    private String handleStatus;///处理状态，未处理:0,已处理:1
    private String id;
    private String lat;
    private String lng;
    private String offTime;
    private String onTime;
    private String searchKey;
    private String startTime;
    private String updateTime;
    private String powerOffTime;
    private String offSecond;
    private String offSecondStr;
    private String unloadAddress;  ////卸货地址
    private String unloadEndTime; ////卸货时间
    private String addvice;  ////处理意见
    private String type;
    private String message;//异常信息

    private String orderCode;//订单编号
    private String custName;
    private String parkOverTime;
    private String parkStartTime;
    private String factoryName;
    private boolean isRead;//false 未读  true  已读

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnloadEndTime() {
        return unloadEndTime;
    }

    public void setUnloadEndTime(String unloadEndTime) {
        this.unloadEndTime = unloadEndTime;
    }

    public String getUnloadAddress() {
        return unloadAddress;
    }

    public void setUnloadAddress(String unloadAddress) {
        this.unloadAddress = unloadAddress;
    }

    public String getOnTime() {
        return onTime;
    }

    public void setOnTime(String onTime) {
        this.onTime = onTime;
    }

    public String getPowerOffTime() {
        return powerOffTime;
    }

    public void setPowerOffTime(String powerOffTime) {
        this.powerOffTime = powerOffTime;
    }

    public String getOffSecond() {
        return offSecond;
    }

    public void setOffSecond(String offSecond) {
        this.offSecond = offSecond;
    }

    public String getOffSecondStr() {
        return offSecondStr;
    }

    public void setOffSecondStr(String offSecondStr) {
        this.offSecondStr = offSecondStr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddvice() {
        return addvice;
    }

    public void setAddvice(String addvice) {
        this.addvice = addvice;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOffTime() {
        return offTime;
    }

    public void setOffTime(String offTime) {
        this.offTime = offTime;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getParkOverTime() {
        return parkOverTime;
    }

    public void setParkOverTime(String parkOverTime) {
        this.parkOverTime = parkOverTime;
    }

    public String getParkStartTime() {
        return parkStartTime;
    }

    public void setParkStartTime(String parkStartTime) {
        this.parkStartTime = parkStartTime;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }
}
