package com.hand.handtruck.model;

import java.io.Serializable;

/**
 * Created by hand-hitech2 on 2018-05-04.
 */

public class GPSDataBean implements Serializable {
/*keytool -list -v -keystore C:\SVNAndroidWork\HangProject\hande.jks*/
    /**
     * deviceId : 20995
     * date : 2016-11-25 00:00:01
     * weight : 10
     * x : 113.7992983
     * y : 22.78091
     */

    private String deviceId;
    private String date;
    private String weight;
    private String x;
    private String y;
    private String status;
    private String speed;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
