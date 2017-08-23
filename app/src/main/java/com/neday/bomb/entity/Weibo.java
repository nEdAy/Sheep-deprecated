package com.neday.bomb.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 新浪微博授权信息
 */
class Weibo implements Parcelable {
    public static final Parcelable.Creator<Weibo> CREATOR = new Parcelable.Creator<Weibo>() {
        @Override
        public Weibo createFromParcel(Parcel source) {
            return new Weibo(source);
        }

        @Override
        public Weibo[] newArray(int size) {
            return new Weibo[size];
        }
    };
    private String uid;//"123456789"
    private String access_token;//"2.00ed6eMCV9DWcBcb79e8108f8m1HdE"
    private Long expires_in;//1564469423540

    public Weibo() {
    }

    private Weibo(Parcel in) {
        this.uid = in.readString();
        this.access_token = in.readString();
        this.expires_in = (Long) in.readValue(Long.class.getClassLoader());
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.access_token);
        dest.writeValue(this.expires_in);
    }
}
