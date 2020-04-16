package com.hand.handtruck.bean;

import com.hand.handtruck.model.WeightTrendBean;

import java.util.List;

/**
 * Created by wcf on 2018-05-04.
 */

public class WeightTrendResultBean {
    private String token;
    private String message;
    private List<WeightTrendBean> result;

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

    public List<WeightTrendBean> getResult() {
        return result;
    }

    public void setResult(List<WeightTrendBean> result) {
        this.result = result;
    }
}
