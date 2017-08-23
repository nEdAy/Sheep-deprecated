package com.neday.bomb.network;


import com.neday.bomb.entity.CreditsHistory;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * CreditsDetails Api
 *
 * @author nEdAy
 */
public interface CreditsDetailsApi {
    /**
     * 口袋币历史记录条件查询
     *
     * @param options 参数
     * @return 口袋币历史记录
     */
    @GET("CreditsDetails")
    Observable<CreditsHistory> queryCreditsDetails(
            @QueryMap Map<String, Object> options);

}
