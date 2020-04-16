package com.hand.handtruck.bean;

import com.hand.handtruck.model.RealTimeTruckBean;

/**
 * Created by wcf on 2018-5-14.
 */

public class RealTimeTruckResultBean {
    private String token;
    private String message;
    private RealTimeTruckBean result;

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

    public RealTimeTruckBean getResult() {
        return result;
    }

    public void setResult(RealTimeTruckBean result) {
        this.result = result;
    }
}
