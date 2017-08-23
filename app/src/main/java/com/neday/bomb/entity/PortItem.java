package com.neday.bomb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 大淘客商品
 */
public class PortItem implements Parcelable {
    public static final Creator<PortItem> CREATOR = new Creator<PortItem>() {
        @Override
        public PortItem createFromParcel(Parcel source) {
            return new PortItem(source);
        }

        @Override
        public PortItem[] newArray(int size) {
            return new PortItem[size];
        }
    };
    private int total_num;
    private List<PortItem> result;
    private int ID;
    private String GoodsID;
    private String Title;
    private String Pic;
    private int Cid;
    private String Org_Price;
    private String Price;
    private int IsTmall;
    private int Sales_num;
    private String Dsr;
    private String Introduce;
    private String Quan_price;
    private String Quan_time;
    private int Quan_surplus;
    private int Quan_receive;
    private String Quan_condition;
    private String Quan_link;
    private String ali_click;
    private boolean Commission_check;

    public PortItem() {
    }

    protected PortItem(Parcel in) {
        this.total_num = in.readInt();
        this.result = in.createTypedArrayList(PortItem.CREATOR);
        this.ID = in.readInt();
        this.GoodsID = in.readString();
        this.Title = in.readString();
        this.Pic = in.readString();
        this.Cid = in.readInt();
        this.Org_Price = in.readString();
        this.Price = in.readString();
        this.IsTmall = in.readInt();
        this.Sales_num = in.readInt();
        this.Dsr = in.readString();
        this.Introduce = in.readString();
        this.Quan_price = in.readString();
        this.Quan_time = in.readString();
        this.Quan_surplus = in.readInt();
        this.Quan_receive = in.readInt();
        this.Quan_condition = in.readString();
        this.Quan_link = in.readString();
        this.ali_click = in.readString();
        this.Commission_check = in.readByte() != 0;
    }

    public int getTotal_num() {
        return total_num;
    }

    public List<PortItem> getResult() {
        return result;
    }

    public int getID() {
        return ID;
    }

    public String getGoodsID() {
        return GoodsID;
    }

    public String getTitle() {
        return Title;
    }

    public String getPic() {
        return Pic;
    }

    public int getCid() {
        return Cid;
    }

    public String getOrg_Price() {
        return Org_Price;
    }

    public String getPrice() {
        return Price;
    }

    public int getIsTmall() {
        return IsTmall;
    }

    public int getSales_num() {
        return Sales_num;
    }

    public String getDsr() {
        return Dsr;
    }

    public String getIntroduce() {
        return Introduce;
    }

    public String getQuan_price() {
        return Quan_price;
    }

    public String getQuan_time() {
        return Quan_time;
    }

    public int getQuan_surplus() {
        return Quan_surplus;
    }

    public int getQuan_receive() {
        return Quan_receive;
    }

    public String getQuan_condition() {
        return Quan_condition;
    }

    public String getQuan_link() {
        return Quan_link;
    }

    public String getAli_click() {
        return ali_click;
    }

    public boolean isCommission_check() {
        return Commission_check;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total_num);
        dest.writeTypedList(this.result);
        dest.writeInt(this.ID);
        dest.writeString(this.GoodsID);
        dest.writeString(this.Title);
        dest.writeString(this.Pic);
        dest.writeInt(this.Cid);
        dest.writeString(this.Org_Price);
        dest.writeString(this.Price);
        dest.writeInt(this.IsTmall);
        dest.writeInt(this.Sales_num);
        dest.writeString(this.Dsr);
        dest.writeString(this.Introduce);
        dest.writeString(this.Quan_price);
        dest.writeString(this.Quan_time);
        dest.writeInt(this.Quan_surplus);
        dest.writeInt(this.Quan_receive);
        dest.writeString(this.Quan_condition);
        dest.writeString(this.Quan_link);
        dest.writeString(this.ali_click);
        dest.writeByte(this.Commission_check ? (byte) 1 : (byte) 0);
    }
}
