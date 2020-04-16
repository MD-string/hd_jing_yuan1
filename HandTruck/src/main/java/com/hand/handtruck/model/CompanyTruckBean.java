package com.hand.handtruck.model;

import java.io.Serializable;

/**
 * Created by hand-hitech2 on 2018-03-08.
 */

public class CompanyTruckBean implements Serializable {
    /**
     * id : 1
     * deviceId : 43102
     * carNumber : 苏AH8189
     * gpsId : 100000170100033
     * companyName : 博润环卫公司
     * city : 南京市
     * province : 江苏省
     * loadCapacity : 50
     * mfgDate : 2015
     * updateDate : 2016-10-19 23:34:30
     */

    private String id;
    private String deviceId;
    private String carNumber;
    private String gpsId;
    private String companyName;
    private String city;
    private String province;
    private String loadCapacity;
    private String mfgDate;
    private String updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getGpsId() {
        return gpsId;
    }

    public void setGpsId(String gpsId) {
        this.gpsId = gpsId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLoadCapacity() {
        return loadCapacity;
    }

    public void setLoadCapacity(String loadCapacity) {
        this.loadCapacity = loadCapacity;
    }

    public String getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(String mfgDate) {
        this.mfgDate = mfgDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
