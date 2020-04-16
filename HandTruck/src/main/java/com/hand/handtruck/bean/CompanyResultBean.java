package com.hand.handtruck.bean;

import com.hand.handtruck.model.CompanyBean;

import java.util.List;

/**
 * Created by wcf on 2018-05-03.
 */

public class CompanyResultBean {
    private String token;
    private String message;
    private List<CompanyBean> result;

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

    public List<CompanyBean> getResult() {
        return result;
    }

    public void setResult(List<CompanyBean> result) {
        this.result = result;
    }
}
