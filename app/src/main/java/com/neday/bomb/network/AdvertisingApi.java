package com.neday.bomb.network;


import com.neday.bomb.entity.Advertising;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Advertising Api
 *
 * @author nEdAy
 */
public interface AdvertisingApi {

    /**
     * 广告条件查询
     *
     * @param options 参数
     * @return 广告
     */
    @Headers({"X-Bmob-Application-Id: d4ae81c0f33067d25cb2f07f5180f262", "X-Bmob-REST-API-Key: 5091cef3dabab327de388df62e0384e2"})
    @GET("https://api.bmob.cn/1/classes/Advertising")
    Observable<Advertising> queryAdvertising(
            @QueryMap Map<String, Object> options);
}
