package com.neday.bomb;

import android.app.Application;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.baichuan.android.trade.model.AlibcTaokeParams;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.neday.bomb.activity.MainActivity;
import com.neday.bomb.util.HttpsUtils;
import com.neday.bomb.util.SharedPreferencesHelper;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import net.nashlegend.anypref.AnyPref;

import okio.Buffer;


/**
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 * 自定义全局Application类
 *
 * @author nEdAy
 */
public class CustomApplication extends Application {
    // 对外提供整个应用生命周期的Context
    private static CustomApplication mInstance;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private MediaPlayer mMediaPlayer;
    private static HttpsUtils.SSLParams sslParams;

    /**
     * 对外提供Application Context
     *
     * @return Application
     */
    public static CustomApplication getInstance() {
        return mInstance;
    }

    /**
     * 对外提供sslParams
     *
     * @return sslParams
     */
    public static HttpsUtils.SSLParams getSSLParams() {
        return sslParams;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (BuildConfig.LOG_DEBUG) {
            Logger.addLogAdapter(new AndroidLogAdapter());
            initBugly(true);
        } else {
            initBugly(false);
            Thread.setDefaultUncaughtExceptionHandler(new MyUnCaughtExceptionHandler());
        }
        // 配置HTTPS
        sslParams = HttpsUtils.getSslSocketFactory(new Buffer()
                .writeUtf8(CER)
                .inputStream(), null, null);
        LeakCanary.install(this);
        // 初始化百川SDK
        initAlibcTradeSDK();
        // 初始化SharedPreferences工具类
        AnyPref.init(this);
        // 初始化Fresco
        Fresco.initialize(this, StaticConfig.getOkHttpImagePipelineConfig(this));
    }

    /**
     * 初始化阿里百川SDK
     */
    private void initAlibcTradeSDK() {
        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                //初始化成功，设置相关的全局配置参数
                /**
                 * 是否走强制H5的逻辑。false：按照默认规则策略打开页面；true：全部页面均为H5打开;
                 * 注意：初始化完成后调用才能生效
                 *
                 * @param isforceH5 （默认为false）
                 * @return 返回打开策略是否设置成功
                 */
                //AlibcTradeSDK.setForceH5( boolean isforceH5)
                /**
                 * 设置是否使用同步淘客打点。true：使用淘客同步打点；false：关闭同步打点，使用异步打点；
                 * 注意：初始化完成后调用才能生效；在加购场景下，只有异步淘客打点
                 *
                 * @param isSyncForTaoke（默认为true）
                 * @return 返回同步淘客打点策略是否设置成功
                 */
                //AlibcTradeSDK.setSyncForTaoke(boolean isSyncForTaoke)
                /**
                 * 设置全局淘客参数，方便开发者用同一个淘客参数，不需要在show接口重复传入
                 * 注意：初始化完成后调用才能生效
                 *
                 * @param taokeParams 淘客参数
                 */
                AlibcTradeSDK.setTaokeParams(new AlibcTaokeParams(StaticConfig.DEFAULT_TAOKE_PID, null, null));
                Logger.i("百川SDK初始化成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                //初始化失败，可以根据code和msg判断失败原因，详情参见错误说明
                Logger.e("百川SDK初始化失败" + code + msg);
            }
        });
    }


    /**
     * Beta高级设置
     *
     * @param logDebug 是否开启log
     */
    private void initBugly(boolean logDebug) {
        /**
         * true表示app启动自动初始化升级模块;
         * false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
        Beta.autoInit = true;
        /**
         * true表示初始化时自动检查升级;
         * false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = true;
        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
        Beta.upgradeCheckPeriod = 60 * 1000;
        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 5 * 1000;
        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源;
         */
        Beta.largeIconId = R.mipmap.ic_launcher;
        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
         */
        Beta.smallIconId = R.mipmap.ic_launcher;
        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.drawable.ic_update_banner;
        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true;
        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);
        //统一初始化Bugly产品，包含Beta
        Bugly.init(this, StaticConfig.BUGLY_APP_ID, logDebug);//测试阶段建议设置成true，发布时设置为false。
    }


    /**
     * 单例模式，及时返回MediaPlayer
     *
     * @return MediaPlayer
     */
    public synchronized MediaPlayer getMediaPlayer() {
        if (mMediaPlayer == null)
            mMediaPlayer = MediaPlayer.create(mInstance, R.raw.shake_sound);
        return mMediaPlayer;
    }

    /**
     * 单例模式，及时返回SharedPreferences
     *
     * @return SharedPreferences
     */
    public synchronized SharedPreferencesHelper getSpHelper() {
        if (sharedPreferencesHelper == null) {
            sharedPreferencesHelper = new SharedPreferencesHelper(mInstance);
        }
        return sharedPreferencesHelper;
    }


    /**
     * http://www.sixwolf.net/blog/2016/04/11/Android去除烦人的闪退弹窗/
     */
    private class MyUnCaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            Intent intent = new Intent(mInstance, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }


    private final static String CER = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEowIBAAKCAQEAqfHTX5+5OyKyEVrjKy+4Sh7UxpDr/qrRL0FmGBpI1j7Ha4fT\n" +
            "ufQfyKKGuZIGVwKRPF88onepTLdbJh3Cjwe8jI+pjPE8x9a6dwzQ95ouEzeYvR19\n" +
            "OQgb0yp5m89oSlxlEluINFwFFlSHUvozL2EcoFLyEd0DL+VSVA8/cvGy7L2r0PkM\n" +
            "wZ+nmsGePc0N5YWU1OMIKAVmh+fWApdWuQrgh9CmTdR7BjmBwSfigntzyqseBwaU\n" +
            "dZWk5lJ0xVZ2c94dsOBtKFkzpZTDO/uXCx082Fk5s2Dzut3COvfrkczTlbHHGjDG\n" +
            "jOePkH2dl/X0AEyj38K/mxUXFQy9+UXAmZWuJQIDAQABAoIBABO4TreEJH2W8VQ/\n" +
            "uGgQvcEGElknRGc1AZiK7XKjDCwmNURGStyDVjPVHi6G4grzLpCzmWjd20yeYyqJ\n" +
            "XgP0WR3zZrCG5q9mvJTnbREqOCn9M9FQDHGTfg0TGvDoUJj+XzER43x16BY3AUvw\n" +
            "4gMfsO9uqI3HmGDUqqi76zgmkUVUraTZDLdMoFr1VRHonzbfu+TkyC9oKPdt2ry2\n" +
            "PRqyrjMoyZyXZnQv4HBD7Su8bMZ8ssPQIBaAIUi60x/psm0wROBk4qmkWuU+eEzt\n" +
            "K1Wy11Id2fNrW8PuOlwGJQAiKohz+6lfy8Y9XEriKoASWo/WcWwd9rKV/Y90Ttdy\n" +
            "KyXopyECgYEA0KZaxKtI86ZNvuX2PIDXMnLTMM8S9amuS2G4yB4avZ4qNcaS9ImL\n" +
            "LKia1CY8L1LlTKWaMSVDCv2wCoBfmCBDBXtNpeONVWo8q5XStfg+ZAr0kkM0/icn\n" +
            "8XJDNPPTpF6FXr3V6yn7z6fVImKs0/H0pLTJG7FxjSr29pVu3ad88mECgYEA0ILe\n" +
            "QZy4DTfmHamRHTyOch3FLHMze36EmtOZv08+xuiBi0T24DAi+r9hvf4SOcqK/c6h\n" +
            "YrH2uY6MUfTiuflmFvd24vStayJUF+evoerqMNMTkvQUL9aVE6fkgXy72lrl4SP+\n" +
            "6vikSBoPazOkHL3/fM4izuPzq0J2uAXK5jVamkUCgYEAuSAgwQbIv4X6zTn0dFJM\n" +
            "nbJLt67DP7zBrJiT/trXFw7SnMwHb3jqR0GBvmH+XG7MjGkSmjBZf9L+8xJbvvpA\n" +
            "/QgeKOXxbKvKEOPqBRKxKMqfnXaL+kR7qDqdbso4KkrDQwBkYTbq+aAL92pdtd+k\n" +
            "FM9i8HNMMNjCLp7/syL8NYECgYBFtitGtnXWfYTjD+kjZDafspJUfSOOO0tsVCiI\n" +
            "DJ+Jcvy0qlb7tzxKpyevlL1VMwFrP+U3ERbTDPSfBgezc82NCilmUbWVJwIEiAJx\n" +
            "WQ/5SOi3bgE9yTlIPA6quCA6Lb/DmPxGZODP9l6HUdmhfmhnuqdqkk3KSUtV1SHz\n" +
            "+4ySHQKBgDj/fTcKGWS/DYsXp93nDY5ah+OnuhSJF42xujVUZBiX/VmHSix5U4hM\n" +
            "5BiGzy1K5W9DGPQoyO9FJwXDughD2z7zxQC6GPAjmNrS7Ejg8REWc6TYidUDx8f7\n" +
            "FXsEJrLGYtiwZgSwW5wi3q91mbpXi5Jt0csnIWKAFfhL18MoRxHV\n" +
            "-----END RSA PRIVATE KEY-----";
}
