package com.neday.bomb.network;


import com.neday.bomb.entity.UploadJSON;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Public Api
 *
 * @author nEdAy
 */
public interface PublicApi {
    /**
     * 获取口袋币商城免登陆url
     *
     * @param uid     用户ID
     * @param credits 用户积分
     * @return 免登陆url
     */
    @GET("https://api.neday.cn/api/duiba/autoLogin")
    @Headers("Pragma: no-cache")
    Observable<String> getAutoLoginUrl(@Query("uid") String uid, @Query("credits") Integer credits);

    /**
     * 上传单张图片
     *
     * @param fileName 图片名
     * @param file     图片
     * @return 返回信息（包含网络真实地址）
     */
    @Headers({"X-Bmob-Application-Id: d4ae81c0f33067d25cb2f07f5180f262",
            "X-Bmob-REST-API-Key: 5091cef3dabab327de388df62e0384e2",
            "Content-Type: image/*"})
    @POST("https://api.bmob.cn/2/files/{fileName}")
    Observable<UploadJSON> uploadImage(@Path("fileName") String fileName, @Body RequestBody file);
}
