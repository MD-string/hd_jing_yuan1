package com.hand.handtruck.ui.form.bean;

import java.io.Serializable;

public class DevOrder implements Serializable {

    private String createTime;
    private String id;
    private String lastDeviceTime;
    private String lastLat;
    private String lastLng;
    private String lastWeight;
    private String mileage;
    private String orderCode;

    private String orderStatus;
    private String updateTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastDeviceTime() {
        return lastDeviceTime;
    }

    public void setLastDeviceTime(String lastDeviceTime) {
        this.lastDeviceTime = lastDeviceTime;
    }

    public String getLastLat() {
        return lastLat;
    }

    public void setLastLat(String lastLat) {
        this.lastLat = lastLat;
    }

    public String getLastLng() {
        return lastLng;
    }

    public void setLastLng(String lastLng) {
        this.lastLng = lastLng;
    }

    public String getLastWeight() {
        return lastWeight;
    }

    public void setLastWeight(String lastWeight) {
        this.lastWeight = lastWeight;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }


    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
