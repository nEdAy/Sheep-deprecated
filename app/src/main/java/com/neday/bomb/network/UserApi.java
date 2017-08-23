package com.neday.bomb.network;


import com.neday.bomb.entity.BaseObject;
import com.neday.bomb.entity.CreditsHistory;
import com.neday.bomb.entity.User;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * User Api
 *
 * @author nEdAy
 */
public interface UserApi {
    /**
     * 注册（不带邀请人）
     *
     * @param user 用户信息（帐号，MD5后密码）
     * @param code 验证码
     * @return 回调信息
     */
    @POST("users")
    Observable<BaseObject> signUp(@Body User user, @Query("code") String code);

    /**
     * 注册（带邀请人）
     *
     * @param user   用户信息（帐号，MD5后密码）
     * @param code   验证码
     * @param invite 邀请人手机号
     * @return 回调信息
     */
    @POST("users")
    Observable<BaseObject> signUp(@Body User user, @Query("code") String code, @Query("invite") String invite);

    /**
     * 登录
     *
     * @param username 帐号
     * @param password 密码（MD5后）
     * @return 用户信息（sessionToken、objectId等）
     * <p>
     * 客户端提交 md5(password) 密码。服务端数据库通过 md5(salt+md5(password)) 的规则存储密码，
     * 该 salt 仅存储在服务端，且在每次存储密码时都随机生成。
     * 密码被 md5() 提交到服务端之后，通过 md5(salt + form['password']) 与数据库密码比对验证。
     * 服务端颁发并验证一个带有时间戳的可信 token （或一次性的）。
     * 传输过程 HTTPS 加持。
     */
    @GET("users")
    Observable<User> login(@Query("username") String username, @Query("password") String password);

    /**
     * 获取指定条件用户信息
     *
     * @param options 参数
     * @return 用户信息
     */
    @GET("users")
    @Headers("Pragma: no-cache")
    Observable<User> queryUser(@QueryMap Map<String, Object> options);

    /**
     * 获取指定objectId用户全部信息
     *
     * @param objectId 用户ID
     * @return 用户信息
     */
    @GET("users/{objectId}")
    @Headers("Pragma: no-cache")
    Observable<User> getUser(@Path("objectId") String objectId);

    /**
     * 获取指定objectId用户的某个字段信息（返回用户积分信息||返回用户摇一摇信息||返回用户签到信息）
     *
     * @param objectId 用户ID
     * @param include  指定返回字段
     * @return 对应信息
     */
    @GET("users/{objectId}")
    @Headers("Pragma: no-cache")
    Observable<User> getUser(@Path("objectId") String objectId, @Query("include") String include);

    /**
     * 更新用户 需要Session-Token  username和password可以更改，但是新的username不能重复
     *
     * @param objectId 用户ID
     * @param user     需要更新的用户信息
     * @return 回调信息 "updatedAt": YYYY-mm-dd HH:ii:ss
     */
    @PUT("users/{objectId}")
    @Headers("Pragma: no-cache")
    Observable<BaseObject> updateUser(@Path("objectId") String objectId, @Body User user);

    /**
     * 忘记密码
     *
     * @param username 用户名
     * @param password 新密码
     * @param code     验证码
     * @return 回调信息
     */
    @PUT("users")
    Observable<BaseObject> resetUserPassword(@Query("username") String username,
                                             @Query("password") String password, @Query("code") String code);

    /**
     * 重置密码
     *
     * @param objectId    用户Id
     * @param oldPassword 老密码(MD5后)
     * @param newPassword 新密码(MD5后)
     * @return 回调信息
     */
    @PUT("users/{objectId}")
    Observable<BaseObject> updateUserPassword(@Path("objectId") String objectId,
                                              @Query("oldPassword") String oldPassword, @Query("newPassword") String newPassword);

    /**
     * 签到
     *
     * @param objectId 用户Id
     * @param type     签到类型
     * @return 签到记录
     */
    @GET("signIn/{objectId}")
    @Headers("Pragma: no-cache")
    Observable<CreditsHistory> signIn(@Path("objectId") String objectId, @Query("type") Integer type);

    /**
     * 摇一摇
     *
     * @param objectId 用户Id
     * @return 摇一摇获得的积分变化值
     */
    @GET("shake/{objectId}")
    @Headers("Pragma: no-cache")
    Observable<Integer> shake(@Path("objectId") String objectId);

}
