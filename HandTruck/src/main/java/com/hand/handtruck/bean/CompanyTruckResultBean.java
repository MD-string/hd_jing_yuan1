package com.hand.handtruck.bean;

import com.hand.handtruck.model.CompanyTruckBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hand-hitech2 on 2018-03-08.
 */

public class CompanyTruckResultBean implements Serializable {

    /**
     * token : CenC2huA
     * message : success
     * result : [{"id":1,"deviceId":"43102","carNumber":"苏AH8189","gpsId":"100000170100033","companyName":"博润环卫公司","city":"南京市","province":"江苏省","loadCapacity":"50","mfgDate":"2015","updateDate":"2016-10-19 23:34:30"}]
     */

    private String token;
    private String message;
    private List<CompanyTruckBean> result;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CompanyTruckBean> getResult() {
        return result;
    }

    public void setResult(List<CompanyTruckBean> result) {
        this.result = result;
    }

}
