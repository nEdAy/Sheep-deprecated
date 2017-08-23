package com.neday.bomb.network;


import com.neday.bomb.entity.PortItem;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * PortItem Api
 *
 * @author nEdAy
 */
public interface PortItemApi {
    /**
     * 大淘客商品条件查询
     *
     * @param options 参数
     * @return 大淘客商品
     */
    @GET("portItems")
    Observable<PortItem> queryPortItem(@QueryMap Map<String, Object> options);
}
