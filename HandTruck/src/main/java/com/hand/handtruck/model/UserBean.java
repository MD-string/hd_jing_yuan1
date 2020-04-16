package com.hand.handtruck.model;

import java.io.Serializable;

/**
 * Created by wcf on 2018-03-09.
 * describe:用户信息实体类
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class UserBean implements Serializable {


    /**
     * userName : admin
     * roleName : 超级管理员
     * creatTime : 2016-07-30 22:05:14
     */

    private String userName;
    private String roleName;
    private String creatTime;
    private String userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
