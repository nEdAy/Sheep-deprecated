package com.neday.bomb.entity;

import android.os.Parcel;

/**
 * 首页图标
 */
class Icon extends BaseObject {
    public static final Creator<Icon> CREATOR = new Creator<Icon>() {
        @Override
        public Icon createFromParcel(Parcel source) {
            return new Icon(source);
        }

        @Override
        public Icon[] newArray(int size) {
            return new Icon[size];
        }
    };
    // 图标分类
    private String type;
    // 图标活动及编号
    private String content;
    // 图标地址
    private String url;

    public Icon() {
    }

    private Icon(Parcel in) {
        super(in);
        this.type = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.type);
        dest.writeString(this.content);
        dest.writeString(this.url);
    }
}
