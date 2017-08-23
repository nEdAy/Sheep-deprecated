package com.neday.bomb.network;


import android.support.annotation.NonNull;
import android.util.Log;

import com.neday.bomb.BuildConfig;
import com.neday.bomb.CustomApplication;
import com.neday.bomb.StaticConfig;
import com.neday.bomb.util.CommonUtils;
import com.neday.bomb.util.HttpsUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit和OkHttpClient实例化
 *
 * @author nEdAy
 */
class RxService<T> {
    private T mService = null;

    RxService(Class<T> clazz, String session) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticConfig.BASE_URL)
                .client(genericClient(session))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(clazz);
    }

    T getService() {
        return mService;
    }

    /**
     * 未联网获取缓存数据
     */
    private static Interceptor provideOfflineCacheInterceptor() {
        return chain -> {
            Request request = chain.request();
            if (!CommonUtils.isNetworkAvailable()) {
                //在1天缓存有效，此处测试用，实际根据需求设置具体缓存有效时间
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(1, TimeUnit.DAYS)
                        .build();
                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
            }
            return chain.proceed(request);
        };
    }

    private static Interceptor provideCacheInterceptor() {
        return chain -> {
            Response response = chain.proceed(chain.request());
            // re-write response header to force use of cache
            // 正常访问同一请求接口（多次访问同一接口），给10秒缓存，超过时间重新发送请求，否则取缓存数据
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(10, TimeUnit.SECONDS)
                    .build();
            return response.newBuilder().removeHeader("Pragma")
                    .header("Cache-Control", cacheControl.toString())
                    .build();
        };
    }

    /**
     * 设置缓存目录和缓存空间大小
     *
     * @return 缓存
     */
    private static Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(CustomApplication.getInstance().getCacheDir(), "http-cache"),
                    50 * 1024 * 1024); // 50 MB
        } catch (Exception e) {
            Log.e("cache", "Could not create Cache!");
        }
        return cache;
    }

    private HttpLoggingInterceptor httpLoggingInterceptor = createHttpLoggingInterceptor();

    @NonNull
    private static HttpLoggingInterceptor createHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private OkHttpClient genericClient(String session) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.LOG_DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor);
        }
        //配置HTTPS
        HttpsUtils.SSLParams sslParams = CustomApplication.getSSLParams();
        return builder
                .addInterceptor(provideOfflineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache())
                .retryOnConnectionFailure(true)
                .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Session-Token", session == null ? "" : session)
                            .build();
                    return chain.proceed(request);
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
    }
}
