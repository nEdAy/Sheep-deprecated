package com.neday.bomb.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * JavaBean基类
 */
public class BaseObject implements Parcelable {
    public static final Creator<BaseObject> CREATOR = new Creator<BaseObject>() {
        @Override
        public BaseObject createFromParcel(Parcel source) {
            return new BaseObject(source);
        }

        @Override
        public BaseObject[] newArray(int size) {
            return new BaseObject[size];
        }
    };
    public String objectId;//唯一键 uuid
    private String createdAt;//YYYY-mm-dd HH:ii:ss
    private String updatedAt;//YYYY-mm-dd HH:ii:ss
    private String count;

    public BaseObject() {
    }

    protected BaseObject(Parcel in) {
        this.objectId = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.count = in.readString();
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCount() {
        return count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.objectId);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.count);
    }
}
