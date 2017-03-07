package com.nimrag.kevin.aweweico.sinasdk.bean;

/**
 * Created by kevin on 2017/2/27.
 */

public class UserInfo {
    /**
     * access_token : ACCESS_TOKEN
     * expires_in : 1234
     * remind_in : 798114
     * uid : 12341234
     */

    private String access_token;
    private int expires_in;
    private String remind_in;
    private String uid;

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public int getExpiresIn() {
        return expires_in;
    }

    public void setExpiresIn(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRemindIn() {
        return remind_in;
    }

    public void setRemindIn(String remind_in) {
        this.remind_in = remind_in;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}