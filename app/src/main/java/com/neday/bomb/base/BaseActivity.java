package com.neday.bomb.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.neday.bomb.R;
import com.neday.bomb.view.HeaderLayout;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxFragmentActivity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Activity基类
 *
 * @author nEdAy
 */
public abstract class BaseActivity extends RxFragmentActivity implements IBaseActivity {
    protected Context mContext;
    private HeaderLayout mHeaderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        // 定制流程
        //设置渲染视图View
        View mContextView = LayoutInflater.from(mContext).inflate(bindLayout(), null);
        setContentView(mContextView);
        //初始化控件
        initView(savedInstanceState);
    }


    /**
     * 获取共通操作机能
     */
    protected BaseOperation getOperation() {
        return new BaseOperation(this);
    }

    /**
     * back+title
     */
    protected void initTopBarForLeft(String titleName, String leftText) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.top_title_bar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_LIFT_IMAGE_BUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.ic_back, leftText,
                this::finish);
    }

    /**
     * back+title+右文字
     */
    protected void initTopBarForBoth(String titleName, String leftText, String rightText,
                                     HeaderLayout.onRightButtonClickListener onRightButtonClickListener) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.top_title_bar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGE_BUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.ic_back, leftText,
                this::finish);
        mHeaderLayout.setTitleAndRightTextButton(titleName, rightText,
                onRightButtonClickListener);
    }

    /**
     * back**+title+右文字
     */
    protected void initTopBarForBoth(String titleName, String leftText, HeaderLayout.onLeftButtonClickListener onLeftButtonClickListener, String rightText,
                                     HeaderLayout.onRightButtonClickListener onRightButtonClickListener) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.top_title_bar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGE_BUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.ic_back, leftText, onLeftButtonClickListener);
        mHeaderLayout.setTitleAndRightTextButton(titleName, rightText,
                onRightButtonClickListener);

    }

    /**
     * RxJava调度器 有doOnSubscribe
     *
     * @param o           观察者
     * @param onSubscribe 在subscribe()前初始化数据
     * @param onNext      the {@code Action1<T>} you have designed to accept emissions from the Observable
     * @param onError     the {@code Action1<Throwable>} you have designed to accept any error notification from the
     *                    Observable
     * @param <T>         传输实体
     */
    protected <T> void toSubscribe(Observable<T> o, Action0 onSubscribe, Action1<? super T> onNext, Action1<Throwable> onError) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(onSubscribe)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
    }

    /**
     * RxJava调度器 无doOnSubscribe
     *
     * @param o       观察者
     * @param onNext  the {@code Action1<T>} you have designed to accept emissions from the Observable
     * @param onError the {@code Action1<Throwable>} you have designed to accept any error notification from the
     *                Observable
     * @param <T>     传输实体
     */
    protected <T> void toSubscribe(Observable<T> o, Action1<? super T> onNext, Action1<Throwable> onError) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
    }

    /**
     * 设置状态栏颜色
     */
    protected void setTintManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.red);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
