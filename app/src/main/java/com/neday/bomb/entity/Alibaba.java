package com.neday.bomb.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 阿里授权信息
 */
class Alibaba implements Parcelable {
    public static final Parcelable.Creator<Alibaba> CREATOR = new Parcelable.Creator<Alibaba>() {
        @Override
        public Alibaba createFromParcel(Parcel source) {
            return new Alibaba(source);
        }

        @Override
        public Alibaba[] newArray(int size) {
            return new Alibaba[size];
        }
    };
    private String userId;//AAFBwAg6ACXcfdsTiIVhA3po
    private String authorizationCode;//"8HbqCs4RDZ7VpnsVf76vNHOl298308"
    private Long loginTime;//1460940821699

    public Alibaba() {
    }

    private Alibaba(Parcel in) {
        this.userId = in.readString();
        this.authorizationCode = in.readString();
        this.loginTime = (Long) in.readValue(Long.class.getClassLoader());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.authorizationCode);
        dest.writeValue(this.loginTime);
    }
}
