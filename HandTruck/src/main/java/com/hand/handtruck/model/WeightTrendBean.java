package com.hand.handtruck.model;

import java.io.Serializable;

/**
 * Created by wcf on 2018-05-04.重量趋势实体类
 */

public class WeightTrendBean implements Serializable {

    /**
     * date : 2018-05-02 18:47:00
     * deviceId : 2527
     * weight : 0.5619993
     */

    private String date;
    private String deviceId;
    private String weight;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
