package com.hand.handtruck.bean;

import com.hand.handtruck.model.CompanyTruckBean;

import java.io.Serializable;

/**
 * Created by wcf on 2018-03-08.
 */

public class TruckChildBean implements Serializable {
    private String name;
    private String sign;
    private CompanyTruckBean truckModel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public CompanyTruckBean getTruckModel() {
        return truckModel;
    }

    public void setTruckModel(CompanyTruckBean truckModel) {
        this.truckModel = truckModel;
    }
}
