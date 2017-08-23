package com.neday.bomb.entity;


import android.os.Parcel;

import java.util.List;

/**
 * 口袋币记录
 */
public class CreditsHistory extends BaseObject {
    public static final Creator<CreditsHistory> CREATOR = new Creator<CreditsHistory>() {
        @Override
        public CreditsHistory createFromParcel(Parcel source) {
            return new CreditsHistory(source);
        }

        @Override
        public CreditsHistory[] newArray(int size) {
            return new CreditsHistory[size];
        }
    };
    private String userId;//用户Id
    private Integer type;//修改类型
    private Integer change;//修改的积分变化值
    private Integer credit;//积分修改后的积分数值
    private List<CreditsHistory> results;

    private CreditsHistory(Parcel in) {
        super(in);
        this.userId = in.readString();
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
        this.change = (Integer) in.readValue(Integer.class.getClassLoader());
        this.credit = (Integer) in.readValue(Integer.class.getClassLoader());
        this.results = in.createTypedArrayList(CreditsHistory.CREATOR);
    }

    public List<CreditsHistory> getElements() {
        return results;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getChange() {
        return change;
    }

    public void setChange(Integer change) {
        this.change = change;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.userId);
        dest.writeValue(this.type);
        dest.writeValue(this.change);
        dest.writeValue(this.credit);
        dest.writeTypedList(this.results);
    }
}
