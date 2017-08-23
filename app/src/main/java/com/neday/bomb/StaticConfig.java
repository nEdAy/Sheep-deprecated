package com.neday.bomb;

import android.annotation.SuppressLint;
import android.content.Context;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Sets;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;

import okhttp3.OkHttpClient;

/**
 * 系统变量参数
 *
 * @author nEdAy
 */
public final class StaticConfig {
    public static final String BASE_URL = "https://api.neday.cn/api/v1/";
    public static final int PAGE_SIZE = 20;
    public static final int AUTO_SIZE = 4;
    // 注册成功之后登陆页面退出
    public static final String ACTION_REGISTER_SUCCESS_FINISH = "register.success.finish";
    // 更新昵称之后账户页面刷新
    public static final String ACTION_UPDATE_NICKNAME_SUCCESS_FINISH = "updateNickname.success.finish";

    // Mob的App Key,用于初始化操作
    public static final String MOB_APP_KEY = "1ad08332b2ac0";
    // Mob的App Secret,用于初始化操作
    public static final String MOB_APP_SECRET = "414843b7cc2d9a20aff4b2fd9bc8f21b";
    // Bugly APP ID
    static final String BUGLY_APP_ID = "6e86c3ff8d";

    // 淘客Pid
    public static final String DEFAULT_TAOKE_PID = "mm_108668197_20820254_70484723";
    // 用户使用协议
    public static final String KZ_YHSYXY = "http://kz.neday.cn/81/26/p3336309366de35";
    // 天天摇活动介绍
    public static final String KZ_TTY = "http://kz.neday.cn/91/85/p333138498122d7";
    // 积分规则说明
    public static final String KZ_JF = "http://kz.neday.cn/89/30/p33312925275824";
    // 邀请活动奖励规则
    public static final String KZ_YQ = "http://kz.neday.cn/71/75/p333127659e894d";
    // Fresco 参数
    private static final int MAX_DISK_CACHE_SIZE = 40 * ByteConstants.MB;
    private static final String IMAGE_PIPELINE_CACHE_DIR = "image_cache";
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;
    @SuppressLint("StaticFieldLeak")
    private static ImagePipelineConfig sOkHttpImagePipelineConfig;

    /**
     * Creates config using OkHttp as network backed.
     */
    static ImagePipelineConfig getOkHttpImagePipelineConfig(Context context) {
        if (sOkHttpImagePipelineConfig == null) {
            OkHttpClient okHttpClient = new OkHttpClient();
            ImagePipelineConfig.Builder configBuilder = OkHttpImagePipelineConfigFactory.newBuilder(context, okHttpClient);
            configureCaches(configBuilder, context);
            configureLoggingListeners(configBuilder);
            sOkHttpImagePipelineConfig = configBuilder.build();
        }
        return sOkHttpImagePipelineConfig;
    }

    /**
     * Configures disk and memory cache not to exceed common limits
     */
    private static void configureCaches(
            ImagePipelineConfig.Builder configBuilder,
            Context context) {
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE,                     // Max entries in the cache
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                Integer.MAX_VALUE,                     // Max length of eviction queue
                Integer.MAX_VALUE);                    // Max cache entry size
        configBuilder
                .setBitmapMemoryCacheParamsSupplier(() -> bitmapCacheParams)
                .setMainDiskCacheConfig(DiskCacheConfig.newBuilder(context)
                        .setBaseDirectoryPath(context.getApplicationContext().getCacheDir())
                        .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                        .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                        .build());
    }

    /**
     * Configures Logging
     */
    private static void configureLoggingListeners(ImagePipelineConfig.Builder configBuilder) {
        configBuilder.setRequestListeners(
                Sets.newHashSet((RequestListener) new RequestLoggingListener()));
    }

}
