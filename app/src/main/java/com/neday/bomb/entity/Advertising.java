package com.neday.bomb.entity;

import android.os.Parcel;

import java.util.List;

/**
 * 广告
 */
public class Advertising extends BaseObject {
    public static final Creator<Advertising> CREATOR = new Creator<Advertising>() {
        @Override
        public Advertising createFromParcel(Parcel source) {
            return new Advertising(source);
        }

        @Override
        public Advertising[] newArray(int size) {
            return new Advertising[size];
        }
    };
    //連接
    private String url;
    //圖片
    private String pic;
    //状态
    private Boolean state;
    private List<Advertising> results;

    private Advertising(Parcel in) {
        super(in);
        this.url = in.readString();
        this.pic = in.readString();
        this.state = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.results = in.createTypedArrayList(Advertising.CREATOR);
    }

    public List<Advertising> getElements() {
        return results;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.url);
        dest.writeString(this.pic);
        dest.writeValue(this.state);
        dest.writeTypedList(this.results);
    }
}
