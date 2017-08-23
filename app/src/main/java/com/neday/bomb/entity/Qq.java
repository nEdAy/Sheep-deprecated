package com.neday.bomb.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * QQ授权信息
 */
class Qq implements Parcelable {
    public static final Parcelable.Creator<Qq> CREATOR = new Parcelable.Creator<Qq>() {
        @Override
        public Qq createFromParcel(Parcel source) {
            return new Qq(source);
        }

        @Override
        public Qq[] newArray(int size) {
            return new Qq[size];
        }
    };
    private String openid;//"2345CA18A5CD6255E5BA185E7BECD222"
    private String access_token;//"12345678-SM3m2avZxh5cjJmIrAfx4ZYyamdofM7IjU"
    private Long expires_in;//1382686496

    public Qq() {
    }

    private Qq(Parcel in) {
        this.openid = in.readString();
        this.access_token = in.readString();
        this.expires_in = (Long) in.readValue(Long.class.getClassLoader());
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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
        dest.writeString(this.openid);
        dest.writeString(this.access_token);
        dest.writeValue(this.expires_in);
    }
}
