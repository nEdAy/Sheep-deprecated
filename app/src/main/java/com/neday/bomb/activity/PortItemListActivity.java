package com.neday.bomb.activity;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.neday.bomb.R;
import com.neday.bomb.StaticConfig;
import com.neday.bomb.adapter.PortItemAdapter;
import com.neday.bomb.base.BaseActivity;
import com.neday.bomb.entity.PortItem;
import com.neday.bomb.network.RxFactory;
import com.neday.bomb.util.AliTradeHelper;
import com.neday.bomb.util.CommonUtils;
import com.neday.bomb.view.HidingScrollListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.neday.bomb.R.id.ll_get;

/**
 * 分类展示商品页
 *
 * @author nEdAy
 */
public class PortItemListActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private final static int FIRST_FRAGMENT = 1;//服装
    private final static int SECOND_FRAGMENT = 2;//母婴
    private final static int THIRD_FRAGMENT = 3;//化妆品
    private final static int FOURTH_FRAGMENT = 4;//居家
    private final static int FIFTH_FRAGMENT = 5;//鞋包配饰
    private final static int SIXTH_FRAGMENT = 6;//美食
    private final static int SEVENTH_FRAGMENT = 7;//文体车品
    private final static int EIGHTH_FRAGMENT = 8;//数码家电
    private RecyclerView mRecyclerView;
    private PortItemAdapter mQuickAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View notLoadingView;
    private int mCurrentCounter;
    private int curPage;
    private LinearLayout rl_no_data, rl_no_network;
    private TextView noDataText;
    private TextView tv_total, tv_now;
    private RelativeLayout rl_top_bar;
    private ImageView fab;
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
    //侧边栏
    private DrawerLayout mDrawerLayout;
    //左边栏菜单
    private RelativeLayout mMenuListView;
    //菜单打开/关闭状态
    private boolean isDirection_left = false;
    private View showView;
    private int mCurIndex = -1;

    public static String getExtra() {
        return "portItem";
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_prot_item_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTintManager();
        mCurIndex = getIntent().getIntExtra(getExtra(), 8);
        initTopBarForLeft(getCidName(mCurIndex), getString(R.string.tx_back));
        rl_no_data = (LinearLayout) findViewById(R.id.rl_no_data);
        noDataText = (TextView) findViewById(R.id.noDataText);
        rl_no_network = (LinearLayout) findViewById(R.id.rl_no_network);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red,
                R.color.orange, R.color.green,
                R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(v -> mRecyclerView.scrollToPosition(0));
        rl_top_bar = (RelativeLayout) findViewById(R.id.rl_top_bar);
        tv_now = (TextView) findViewById(R.id.tv_now);
        tv_total = (TextView) findViewById(R.id.tv_total);
        mRecyclerView.addOnScrollListener(HidingScrollListener);
        initMenu();
        initAdapter();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mMenuListView = (RelativeLayout) findViewById(R.id.left_drawer);
        showView = mMenuListView;
        // 设置抽屉打开时，主要内容区被自定义阴影覆盖
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        mDrawerLayout.setDrawerListener(new DrawerLayoutStateListener());
        onRefresh();
    }

    /**
     * 根据序数获取商品类目
     *
     * @param mCurIndex 商品类别序数
     * @return 类目字符串
     */
    private String getCidName(int mCurIndex) {
        String cidString = "潮电街";
        switch (mCurIndex) {
            case FIRST_FRAGMENT:
                cidString = "爱逛街";//服装
                break;
            case SECOND_FRAGMENT:
                cidString = "亲宝贝";//母婴
                break;
            case THIRD_FRAGMENT:
                cidString = "美妆秀";//化妆品
                break;
            case FOURTH_FRAGMENT:
                cidString = "生活汇";//居家
                break;
            case FIFTH_FRAGMENT:
                cidString = "格调派";//鞋包配饰
                break;
            case SIXTH_FRAGMENT:
                cidString = "好吃嘴";//美食
                break;
            case SEVENTH_FRAGMENT:
                cidString = "质生活";//文体车品
                break;
            case EIGHTH_FRAGMENT:
                cidString = "潮电街";//数码家电
                break;
            default:
                break;
        }
        return cidString;
    }

    /**
     * 注册类目点击监听
     */
    private void initMenu() {
        findViewById(R.id.ll_1).setOnClickListener(view -> refreshPortItemList(1));
        findViewById(R.id.ll_2).setOnClickListener(view -> refreshPortItemList(2));
        findViewById(R.id.ll_3).setOnClickListener(view -> refreshPortItemList(3));
        findViewById(R.id.ll_4).setOnClickListener(view -> refreshPortItemList(4));
        findViewById(R.id.ll_5).setOnClickListener(view -> refreshPortItemList(5));
        findViewById(R.id.ll_6).setOnClickListener(view -> refreshPortItemList(6));
        findViewById(R.id.ll_7).setOnClickListener(view -> refreshPortItemList(7));
        findViewById(R.id.ll_8).setOnClickListener(view -> refreshPortItemList(8));
    }

    /**
     * 重新请求新类目，刷新列表信息
     *
     * @param index 新类目cid
     */
    private void refreshPortItemList(int index) {
        if (showView == mMenuListView && isDirection_left) {
            // 左边栏菜单打开时，关闭
            mDrawerLayout.closeDrawer(mMenuListView);
        }
        mCurIndex = index;
        initTopBarForLeft(getCidName(mCurIndex), getString(R.string.tx_back));
        onRefresh();
    }

    private void initAdapter() {
        mQuickAdapter = new PortItemAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mQuickAdapter);
        mQuickAdapter.setOnLoadMoreListener(this);
        mQuickAdapter.openLoadMore(StaticConfig.PAGE_SIZE);
        mQuickAdapter.setAutoLoadMoreSize(StaticConfig.AUTO_SIZE);
        mRecyclerView.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent item = new Intent(mContext,
                        PortItemDetailsActivity.class);
                item.putExtra(PortItemDetailsActivity.getExtra(), (PortItem) baseQuickAdapter.getItem(i));
                startActivity(item);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                PortItem item = mQuickAdapter.getItem(i);
                AliTradeHelper aliTradeUtils = new AliTradeHelper(PortItemListActivity.this);
                aliTradeUtils.addToCart(item.getGoodsID());
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                PortItem item = mQuickAdapter.getItem(i);
                AliTradeHelper aliTradeUtils = new AliTradeHelper(PortItemListActivity.this);
                switch (view.getId()) {
                    case ll_get:
                        aliTradeUtils.showItemURLPage(item.getQuan_link());
                        getOperation().changePressedViewBg(view, R.drawable.ll_get_bg, R.drawable.ll_get_bg_pressed);
                        break;
                    case R.id.tx_buy_url:
                        if (item.isCommission_check()) {
                            aliTradeUtils.showItemURLPage("http://www.neday.cn/index_.php?r=p/d&id=" + item.getID());
                        } else {
                            aliTradeUtils.showItemDetailPage(item.getGoodsID());
                        }
                        getOperation().changePressedViewBg(view, R.drawable.ll_buy_bg, R.drawable.ll_buy_bg_pressed);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            }
        });
        // 一行代码搞定（默认为渐显效果）
        mQuickAdapter.openLoadAnimation();
        // 默认提供5种方法（渐显、缩放、从下到上，从左到右、从右到左）
        // mQuickAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
    }

    public void onLoadMoreRequested() {
        mRecyclerView.post(this::QueryItem);
    }

    @Override
    public void onRefresh() {
        // 主动查询
        RefreshItem();
    }

    private void RefreshItem() {
        Map<String, Object> queryMap = new HashMap<>();
        toSubscribe(RxFactory.getPortItemServiceInstance(null)
                        .queryPortItem(queryMap)
                        .map(PortItem::getResult),
                () -> {
                    //隐藏无网络和无数据界面
                    rl_no_network.setVisibility(View.GONE);
                    rl_no_data.setVisibility(View.GONE);
                    curPage = 0;//重置页码
                    queryMap.put("type", "1");
                    queryMap.put("page", curPage);
                    queryMap.put("where", "[{\"key\":\"Cid\",\"value\":\"" + mCurIndex + "\",\"operation\":\"=\",\"relation\":\"\"}]");
                },
                portItems -> {
                    if (portItems.isEmpty()) {
                        rl_no_data.setVisibility(View.VISIBLE);
                        noDataText.setText(getResources().getString(R.string.tx_no_data_search));
                        rl_no_data.setOnClickListener(v -> CommonUtils.joinQQGroup(PortItemListActivity.this));
                    } else {
                        curPage++;
                    }
                    mQuickAdapter.setNewData(portItems);
                    mCurrentCounter = mQuickAdapter.getData().size();
                    mQuickAdapter.removeAllFooterView();
                    mSwipeRefreshLayout.setRefreshing(false);
                },
                throwable -> {
                    rl_no_network.setVisibility(View.VISIBLE);
                    rl_no_network.setOnClickListener(v -> RefreshItem());
                    mQuickAdapter.getData().clear();
                    mCurrentCounter = 0;
                    mQuickAdapter.removeAllFooterView();
                    mSwipeRefreshLayout.setRefreshing(false);
                    Logger.e(throwable.getMessage());
                });
    }

    private void QueryItem() {
        Map<String, Object> queryMap = new HashMap<>();
        toSubscribe(RxFactory.getPortItemServiceInstance(null)
                        .queryPortItem(queryMap),
                () -> {
                    queryMap.put("count", "1");
                    queryMap.put("page", curPage);
                    queryMap.put("type", "1");
                    queryMap.put("where", "[{\"key\":\"Cid\",\"value\":\"" + mCurIndex + "\",\"operation\":\"=\",\"relation\":\"\"}]");
                },
                portItem -> {
                    int TOTAL_COUNTER = portItem.getTotal_num();
                    tv_total.setText(String.valueOf(TOTAL_COUNTER));
                    // 一定要在mRecyclerView.post里面更新数据。
                    mRecyclerView.post(() -> {
                        // 如果有下一页则调用addData，不需要把下一页数据add到list里面，直接新的数据给adapter即可。
                        mQuickAdapter.addData(portItem.getResult());
                        mCurrentCounter = mQuickAdapter.getData().size();
                        if (mCurrentCounter >= TOTAL_COUNTER) {
                            // 数据全部加载完毕就调用 loadComplete
                            mQuickAdapter.loadComplete();
                            if (notLoadingView == null) {
                                notLoadingView = getLayoutInflater().inflate(R.layout.include_index_not_loading,
                                        (ViewGroup) mRecyclerView.getParent(), false);
                                notLoadingView.setOnClickListener(v -> CommonUtils.joinQQGroup(PortItemListActivity.this));
                            }
                            mQuickAdapter.addFooterView(notLoadingView);
                        } else {
                            curPage++;
                        }
                    });
                },
                throwable -> {
                    mQuickAdapter.showLoadMoreFailedView();
                    Logger.e(throwable.getMessage());
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        AlibcTradeSDK.destory();
    }

    /**
     * DrawerLayout状态变化监听
     */
    private class DrawerLayoutStateListener extends
            DrawerLayout.SimpleDrawerListener {
        //当导航菜单滑动的时候被执行
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            showView = drawerView;
        }

        //当导航菜单打开时执行
        @Override
        public void onDrawerOpened(View drawerView) {
            isDirection_left = true;
        }

        //当导航菜单关闭时执行
        @Override
        public void onDrawerClosed(View drawerView) {
            isDirection_left = false;
        }
    }
}
