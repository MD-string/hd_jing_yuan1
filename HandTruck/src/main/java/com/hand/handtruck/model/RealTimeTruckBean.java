package com.hand.handtruck.model;

import java.io.Serializable;

/**
 * Created by wcf on 2018-05-14.
 */

public class RealTimeTruckBean implements Serializable{

    /**
     * deviceId : 45578
     * carNumber : 苏AH1991
     * speed : 0
     * weight : 12.9179459
     * x : 118.9107217
     * y : 32.120505
     * date : 2017-04-14 11:00:28
     */

    private String deviceId;
    private String carNumber;
    private String speed;
    private String weight;
    private String x;
    private String y;
    private String date;
    private String course;
    private String status;
    private String address;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
