package com.nimrag.kevin.aweweico.ui;

/**
 * Created by kevin on 2017/2/19.
 */

public class WeiboItem {

    private  String mProfileImageUrl;
    private  String mScreenName;
    private  String mText;

    public WeiboItem(String profileImageUrl, String screenName, String text) {
        mProfileImageUrl = profileImageUrl;
        mScreenName = screenName;
        mText = text;
    }

    public String getProfileImageUrl() {
        return mProfileImageUrl;
    }

    public String getScreenName() {
        return mScreenName;
    }

    public String getText() {
        return mText;
    }
}
