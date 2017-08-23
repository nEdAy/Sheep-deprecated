package com.neday.bomb.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 微信授权信息
 */
public class Wechat implements Parcelable {
    public static final Creator<Wechat> CREATOR = new Creator<Wechat>() {
        @Override
        public Wechat createFromParcel(Parcel source) {
            return new Wechat(source);
        }

        @Override
        public Wechat[] newArray(int size) {
            return new Wechat[size];
        }
    };
    private String objectId;
    private String openId;
    private String inopenId;

    public Wechat() {
    }

    protected Wechat(Parcel in) {
        this.objectId = in.readString();
        this.openId = in.readString();
        this.inopenId = in.readString();
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getInopenId() {
        return inopenId;
    }

    public void setInopenId(String inopenId) {
        this.inopenId = inopenId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.objectId);
        dest.writeString(this.openId);
        dest.writeString(this.inopenId);
    }
}
