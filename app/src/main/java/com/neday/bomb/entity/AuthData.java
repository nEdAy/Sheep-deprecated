package com.neday.bomb.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 第三方授权信息库
 */
public class AuthData implements Parcelable {

    public static final Creator<AuthData> CREATOR = new Creator<AuthData>() {
        @Override
        public AuthData createFromParcel(Parcel source) {
            return new AuthData(source);
        }

        @Override
        public AuthData[] newArray(int size) {
            return new AuthData[size];
        }
    };
    private Weibo weibo;//新浪微博
    private Qq qq;//腾讯QQ
    private Alibaba alibaba;//阿里系
    private Wechat wechat;//阿里系

    public AuthData() {
    }

    protected AuthData(Parcel in) {
        this.weibo = in.readParcelable(Weibo.class.getClassLoader());
        this.qq = in.readParcelable(Qq.class.getClassLoader());
        this.alibaba = in.readParcelable(Alibaba.class.getClassLoader());
        this.wechat = in.readParcelable(Wechat.class.getClassLoader());
    }

    public Weibo getWeibo() {
        return weibo;
    }

    public void setWeibo(Weibo weibo) {
        this.weibo = weibo;
    }

    public Qq getQq() {
        return qq;
    }

    public void setQq(Qq qq) {
        this.qq = qq;
    }

    public Alibaba getAlibaba() {
        return alibaba;
    }

    public void setAlibaba(Alibaba alibaba) {
        this.alibaba = alibaba;
    }

    public Wechat getWechat() {
        return wechat;
    }

    public void setWechat(Wechat wechat) {
        this.wechat = wechat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.weibo, flags);
        dest.writeParcelable(this.qq, flags);
        dest.writeParcelable(this.alibaba, flags);
        dest.writeParcelable(this.wechat, flags);
    }
}
