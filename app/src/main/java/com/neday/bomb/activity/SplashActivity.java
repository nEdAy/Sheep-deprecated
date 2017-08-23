package com.neday.bomb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.neday.bomb.CustomApplication;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 启动页
 *
 * @author nEdAy
 */
public class SplashActivity extends FragmentActivity {
    private static final int SHOW_TIME_MIN = 300; //0.3s
    private static final int GO_GUIDE = 0; //导航页
    private static final int GO_MAIN = 1; //主页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);//为保证启动速度，SplashActivity不要调用setContentView()方法
        Observable.timer(SHOW_TIME_MIN, TimeUnit.MILLISECONDS) //延迟SHOW_TIME_MIN秒跳转
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    doStartActivity(initAndChoosePath());
                });
    }


    /**
     * 初始化操作并检测是否是第一次启动
     *
     * @return 页面序数
     */
    private int initAndChoosePath() {
        CustomApplication customApplication = CustomApplication.getInstance();
//        PushManager.getInstance().initialize(customApplication);
        Boolean user_first = customApplication.getSpHelper().isAllowFirst();
        if (user_first) {
            return GO_GUIDE;
        } else {
            return GO_MAIN;
        }
    }

    /**
     * 跳转指定Activity
     *
     * @param result 页面序数
     */
    private void doStartActivity(int result) {
        switch (result) {
            case GO_GUIDE:
                startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                break;
            case GO_MAIN:
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                break;
            default:
                break;
        }
        finish();
    }

    /**
     * SplashActivity屏蔽物理返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

}
