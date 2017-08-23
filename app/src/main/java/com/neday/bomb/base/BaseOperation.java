package com.neday.bomb.base;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 基本的操作共通抽取
 *
 * @author nEdAy
 */
public class BaseOperation {
    private Activity mContext;

    BaseOperation(Activity mContext) {
        this.mContext = mContext;
    }

    /**
     * 启动Activity
     */
    public void startActivity(Class<?> cla) {
        mContext.startActivity(new Intent(mContext, cla));
    }


    /**
     * 点击时修改子控件背景样式并在0.5s后恢复
     *
     * @param view       要修改的子控件
     * @param bg         要恢复的背景
     * @param bg_pressed 要变化的背景
     */
    public void changePressedViewBg(View view, int bg, int bg_pressed) {
        view.setPressed(true);
        view.setBackgroundResource(bg_pressed);
        Observable.timer(500, TimeUnit.MILLISECONDS) //延迟SHOW_TIME_MIN秒跳转
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    view.setPressed(false);
                    view.setBackgroundResource(bg);
                });
    }
}
