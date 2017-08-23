package com.neday.bomb.base;

import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;

import com.neday.bomb.R;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.lang.reflect.Field;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Fragment基类
 *
 * @author nEdAy
 */
public class BaseFragment extends RxFragment {
    /**
     * 获取共通操作机能
     */
    protected BaseOperation getOperation() {
        return new BaseOperation(getActivity());
    }

    /**
     * 将整个页面下移一个状态栏的高度
     *
     * @param parentView 整个页面
     */
    protected void setStatusBarHeight(View parentView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                int sbar = getResources().getDimensionPixelSize(x);
                View paddingView = parentView.findViewById(R.id.paddingView);
                RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) paddingView.getLayoutParams(); // 取控件mGrid当前的布局参数
                linearParams.height = sbar;// 当控件的高强制设成状态栏高度
                paddingView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
            } catch (Exception ignored) {
            }
        }
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
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
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
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
    }

}
