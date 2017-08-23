package com.neday.bomb.base;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.neday.bomb.R;
import com.neday.bomb.view.HidingScrollListener;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * ListFragment基类
 *
 * @author nEdAy
 */
public abstract class BaseListFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener, IBaseListFragment {
    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected LinearLayout rl_no_data, rl_no_network;
    protected TextView tv_total;
    protected int mCurrentCounter;
    protected View notLoadingView;
    protected int curPage;
    protected int mCurIndex = -1;
    private View parentView;
    private TextView tv_now;
    private RelativeLayout rl_top_bar;
    private ImageView fab;
    /**
     * RecyclerView滑动监听
     */
    private final HidingScrollListener HidingScrollListener = new HidingScrollListener() {
        @Override
        public void onStart() {
            rl_top_bar.setVisibility(View.VISIBLE);
            rl_top_bar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        }

        @Override
        public void onRemoveAll() {
            Resources resources = getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            fab.animate()
                    .translationY(dm.heightPixels - fab.getHeight())
                    .setInterpolator(new AccelerateInterpolator(2))
                    .start();
            rl_top_bar.animate()
                    .translationY(dm.heightPixels - rl_top_bar.getHeight())
                    .setInterpolator(new AccelerateInterpolator(2))
                    .start();
        }

        @Override
        public void onMove(int visibleItemNum) {
            tv_now.setText(String.valueOf(visibleItemNum));
        }

        @Override
        public void onHide() {
            rl_top_bar.setVisibility(View.VISIBLE);
            rl_top_bar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            Resources resources = getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            fab.animate()
                    .translationY(dm.heightPixels - fab.getHeight())
                    .setInterpolator(new AccelerateInterpolator(2))
                    .start();
        }

        @Override
        public void onShow() {
            fab.setVisibility(View.VISIBLE);
            fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            Resources resources = getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            rl_top_bar.animate()
                    .translationY(dm.heightPixels - rl_top_bar.getHeight())
                    .setInterpolator(new AccelerateInterpolator(2))
                    .start();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.include_anything_list, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        rl_no_data = (LinearLayout) parentView.findViewById(R.id.rl_no_data);
        rl_no_network = (LinearLayout) parentView.findViewById(R.id.rl_no_network);
        mRecyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) parentView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red,
                R.color.orange, R.color.green,
                R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fab = (ImageView) parentView.findViewById(R.id.fab);
        fab.setOnClickListener(v -> mRecyclerView.scrollToPosition(0));
        rl_top_bar = (RelativeLayout) parentView.findViewById(R.id.rl_top_bar);
        tv_now = (TextView) parentView.findViewById(R.id.tv_now);
        tv_total = (TextView) parentView.findViewById(R.id.tv_total);
        mRecyclerView.addOnScrollListener(HidingScrollListener);
        initAdapter();
        // 主动刷新数据
        onRefresh();
    }

    /**
     * 当mRecyclerView上拉加载时触发，具体实现在子类
     */
    @Override
    public void onLoadMoreRequested() {
        mRecyclerView.post(this::LoadMoreItem);
    }

    /**
     * 当SwipeRefreshLayout下拉刷新时触发，具体实现在子类
     */
    @Override
    public void onRefresh() {
        RefreshItem();
    }

    /**
     * 点击时修改子控件背景样式并在0.5s后恢复
     *
     * @param view       要修改的子控件
     * @param bg         要恢复的背景
     * @param bg_pressed 要变化的背景
     */
    protected void changePressedViewBg(View view, int bg, int bg_pressed) {
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
