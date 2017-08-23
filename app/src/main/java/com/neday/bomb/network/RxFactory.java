package com.neday.bomb.network;


import android.text.TextUtils;

/**
 * Api工厂
 *
 * @author nEdAy
 */
public class RxFactory {
    private static final Object monitor = new Object();
    private static UserApi mUserApi;
    private static PublicApi mPublicApi;
    private static CreditsDetailsApi mCreditsDetailsApi;
    private static AdvertisingApi mAdvertisingApi;
    private static PortItemApi mPortItemApi;

    public static PortItemApi getPortItemServiceInstance(String session) {
        synchronized (monitor) {
            return (mPortItemApi == null || !TextUtils.isEmpty(session)) ?
                    mPortItemApi = new RxService<>(PortItemApi.class, session).getService() : mPortItemApi;
        }
    }

    public static UserApi getUserServiceInstance(String session) {
        synchronized (monitor) {
            return (mUserApi == null || !TextUtils.isEmpty(session)) ?
                    mUserApi = new RxService<>(UserApi.class, session).getService() : mUserApi;
        }
    }

    public static PublicApi getPublicServiceInstance(String session) {
        synchronized (monitor) {
            return (mPublicApi == null || !TextUtils.isEmpty(session)) ?
                    mPublicApi = new RxService<>(PublicApi.class, session).getService() : mPublicApi;
        }
    }


    public static CreditsDetailsApi getCreditsDetailsServiceInstance(String session) {
        synchronized (monitor) {
            return (mCreditsDetailsApi == null || !TextUtils.isEmpty(session)) ?
                    mCreditsDetailsApi = new RxService<>(CreditsDetailsApi.class, session).getService() : mCreditsDetailsApi;
        }
    }


    public static AdvertisingApi getAdvertisingServiceInstance(String session) {
        synchronized (monitor) {
            return (mAdvertisingApi == null || !TextUtils.isEmpty(session)) ?
                    mAdvertisingApi = new RxService<>(AdvertisingApi.class, session).getService() : mAdvertisingApi;
        }
    }

}
