package com.neday.bomb.entity;


import android.os.Parcel;

import net.nashlegend.anypref.AnyPref;
import net.nashlegend.anypref.annotations.PrefField;
import net.nashlegend.anypref.annotations.PrefModel;

/**
 * 用户
 */
@PrefModel("User")
public class User extends BaseObject {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    // 账户 唯一键 11位手机号
    public String username;
    // Token
    public String sessionToken;
    // 昵称 默认值 口袋爆料人
    @PrefField(strDef = "口袋爆料人")
    public String nickname;
    // 头像 默认值 null
    public String avatar;
    /* 永远不返回 password字段加密可行方案，客户端提交 md5(password) 密码。
     服务端数据库通过 md5(salt+md5(password)) 的规则存储密码，该 salt 仅存储在服务端，且在每次存储密码时都随机生成。
     密码被 md5() 提交到服务端之后，可通过 md5(salt + form['password']) 与数据库密码比对。
     另防止 replay 攻击（请求被重新发出一次即可能通过验证）的问题，由服务端颁发并验证一个带有时间戳的可信 token （或一次性的）。*/
    // 登录密码
    private String password;
    // 口袋币 默认值 0 即等级 ！！！必须设默认值0  0=0 1>=100 2>=200 3>=500 4>=1000 5>=2000 6>=5000 7>=15000 8>=50000 9>=100000 10>=200000
    private Integer credit;
    // 签到 默认值 true
    private Boolean sign_in;
    // 次数 默认值 3
    private Integer shake_times;
    // 第三方授权信息
    private AuthData authData;

    public User() {
    }

    protected User(Parcel in) {
        super(in);
        this.username = in.readString();
        this.sessionToken = in.readString();
        this.nickname = in.readString();
        this.avatar = in.readString();
        this.password = in.readString();
        this.credit = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sign_in = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.shake_times = (Integer) in.readValue(Integer.class.getClassLoader());
        this.authData = in.readParcelable(AuthData.class.getClassLoader());
    }

    public static User getCurrentUser() {
        User user = AnyPref.get(User.class, "_CurrentUser", true);
        if (user != null) {
            return user;
        }
        return null;
    }

    /**
     * 退出登录,清空缓存数据
     *
     * @param objectId 别名
     */
    public static void logout(String objectId) {
//        PushManager.getInstance().unBindAlias(CustomApplication.getInstance(), objectId, true);
        //清除本地账户记录
        AnyPref.clear(User.class, "_CurrentUser");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getCredit() {
        return credit;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getShake_times() {
        return shake_times;
    }

    public Boolean getSign_in() {
        return sign_in;
    }

    public AuthData getAuthData() {
        return authData;
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.username);
        dest.writeString(this.sessionToken);
        dest.writeString(this.nickname);
        dest.writeString(this.avatar);
        dest.writeString(this.password);
        dest.writeValue(this.credit);
        dest.writeValue(this.sign_in);
        dest.writeValue(this.shake_times);
        dest.writeParcelable(this.authData, flags);
    }
}
