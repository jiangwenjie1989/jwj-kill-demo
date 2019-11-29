package com.jwj.im.common;

import java.io.Serializable;


public class UserSessionInfo implements Serializable {

    private static final long serialVersionUID = -1208332156009024077L;

    /**
     * 用户id
     */
    private long userId;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 渠道类型 h5 android ios
     */
    private String channelType;
    /**
     * 设备id
     */
    private String macid;
    /**
     * 会话令牌
     */
    private String token;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getMacid() {
        return macid;
    }

    public void setMacid(String macid) {
        this.macid = macid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
