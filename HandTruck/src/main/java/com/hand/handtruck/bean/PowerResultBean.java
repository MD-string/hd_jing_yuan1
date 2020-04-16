package com.hand.handtruck.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wcf on 2018-05-03.
 */

public class PowerResultBean implements Serializable {
    private String token;
    private String message;
    private List<PowerBean> result;

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

    public List<PowerBean> getResult() {
        return result;
    }

    public void setResult(List<PowerBean> result) {
        this.result = result;
    }
}
