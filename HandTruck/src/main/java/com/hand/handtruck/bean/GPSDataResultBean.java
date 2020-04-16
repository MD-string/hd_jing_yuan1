package com.hand.handtruck.bean;

import com.hand.handtruck.model.GPSDataBean;

import java.util.List;

/**
 * Created by wcf on 2018-05-04.
 */

public class GPSDataResultBean {
    private String token;
    private String message;
    private List<GPSDataBean> result;

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

    public List<GPSDataBean> getResult() {
        return result;
    }

    public void setResult(List<GPSDataBean> result) {
        this.result = result;
    }
}
