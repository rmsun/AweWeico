package com.nimrag.kevin.aweweico.sinasdk;

import com.nimrag.kevin.aweweico.AppContext;
import com.nimrag.kevin.aweweico.sinasdk.bean.UserInfo;
import com.nimrag.kevin.aweweico.lib.DefaultHttpUtility;
import com.nimrag.kevin.aweweico.lib.HttpConfig;
import com.nimrag.kevin.aweweico.lib.IHttpUtility;
import com.nimrag.kevin.aweweico.lib.Params;
import com.nimrag.kevin.aweweico.sinasdk.bean.FriendsTimeLine;

/**
 * Created by kevin on 2017/3/1.
 */

public class SinaSDK {
    public SinaSDK() {}

    public static SinaSDK getInstance() { return new SinaSDK(); }

    public UserInfo getAccessToken(String code) {
        Params urlParams = new Params();
        urlParams.addParam("client_id", "295644704");
        urlParams.addParam("client_secret", "cac67dabdec521e0981709f9ad8d2654");
        urlParams.addParam("code", code);
        urlParams.addParam("redirect_uri", "https://api.weibo.com/oauth2/default.html");
        IHttpUtility httpUtility = new DefaultHttpUtility();
        return httpUtility.doPost(new HttpConfig(), "https://api.weibo.com/oauth2/access_token", urlParams, null, null, UserInfo.class);
    }

    public FriendsTimeLine getFriendsTimeLine() {
        Params urlParams = new Params();
        urlParams.addParam("access_token", AppContext.getUserInfo().getAccessToken());
        IHttpUtility httpUtility = new DefaultHttpUtility();
        return httpUtility.doGet(new HttpConfig(), "https://api.weibo.com/2/statuses/friends_timeline.json", urlParams, FriendsTimeLine.class);
    }
}
