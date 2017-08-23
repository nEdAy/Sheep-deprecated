package com.neday.bomb.fragment.mainFragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.neday.bomb.R;
import com.neday.bomb.StaticConfig;
import com.neday.bomb.activity.LoginActivity;
import com.neday.bomb.activity.PortItemDetailsActivity;
import com.neday.bomb.activity.PortItemListActivity;
import com.neday.bomb.activity.SearchAndTypeActivity;
import com.neday.bomb.activity.ShakeActivity;
import com.neday.bomb.activity.SignInActivity;
import com.neday.bomb.activity.WebShopActivity;
import com.neday.bomb.adapter.PortItemAdapter;
import com.neday.bomb.base.BaseFragment;
import com.neday.bomb.entity.Advertising;
import com.neday.bomb.entity.PortItem;
import com.neday.bomb.entity.User;
import com.neday.bomb.network.RxFactory;
import com.neday.bomb.util.AliTradeHelper;
import com.neday.bomb.util.CommonUtils;
import com.neday.bomb.view.AdImageBanner;
import com.neday.bomb.view.HidingScrollListener;
import com.neday.bomb.view.loading.CatLoadingView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页
 */
public class IndexFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener {
    private final static String TAG = "IndexFragment";
    private CatLoadingView catLoadingView;
    private View parentView;
    private RecyclerView mRecyclerView;
    private PortItemAdapter mQuickAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View notLoadingView;
    private int mCurrentCounter;
    private AdImageBanner banner;
    private int curPage;
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
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mMenuListView;// 左边栏菜单
    private boolean isDirection_left = false;//菜单打开/关闭状态
    private View showView;
    private AliTradeHelper aliTradeUtils;
    private final SimpleClickListener SimpleClickListener = new SimpleClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            Intent item = new Intent(getActivity(),
                    PortItemDetailsActivity.class);
            item.putExtra(PortItemDetailsActivity.getExtra(), (PortItem) baseQuickAdapter.getItem(i));
            startActivity(item);
        }

        @Override
        public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            PortItem item = mQuickAdapter.getItem(i);
            AliTradeHelper aliTradeUtils = new AliTradeHelper(getActivity());
            aliTradeUtils.addToCart(item.getGoodsID());
        }

        @Override
        public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            PortItem item = mQuickAdapter.getItem(i);
            switch (view.getId()) {
                case R.id.ll_get:
                    aliTradeUtils.showItemURLPage(item.getQuan_link());
                    getOperation().changePressedViewBg(view, R.drawable.ll_get_bg, R.drawable.ll_get_bg_pressed);
                    break;
                case R.id.tx_buy_url:
                    if (item.getAli_click() != null && item.getAli_click().length() > 33) {
                        aliTradeUtils.showItemDetailPage(item.getGoodsID());
                    } else {
                        aliTradeUtils.showItemURLPage(item.getAli_click());
                    }
                    getOperation().changePressedViewBg(view, R.drawable.ll_buy_bg, R.drawable.ll_buy_bg_pressed);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            baseQuickAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_main_index, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        setStatusBarHeight(parentView); // 配合状态栏下移
        catLoadingView = new CatLoadingView();
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
        parentView.findViewById(R.id.ll_search).setOnClickListener(view ->
                getOperation().startActivity(SearchAndTypeActivity.class));
        parentView.findViewById(R.id.iv_type_search).setOnClickListener(view -> {
            if (showView == mMenuListView) {
                if (!isDirection_left) { // 左边栏菜单关闭时，打开
                    mDrawerLayout.openDrawer(mMenuListView);
                } else {// 左边栏菜单打开时，关闭
                    mDrawerLayout.closeDrawer(mMenuListView);
                }
            }
        });
        parentView.findViewById(R.id.btn_search).setOnClickListener(view ->
                getOperation().startActivity(SearchAndTypeActivity.class));
        mRecyclerView.addOnScrollListener(HidingScrollListener);
        initMenu();
        initAdapter();
        addHeadView();
        aliTradeUtils = new AliTradeHelper(getActivity());
        mDrawerLayout = (DrawerLayout) parentView.findViewById(R.id.drawer_layout);
        mMenuListView = (RelativeLayout) parentView.findViewById(R.id.left_drawer);
        this.showView = mMenuListView;
        // 设置抽屉打开时，主要内容区被自定义阴影覆盖
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setDrawerListener(new DrawerLayoutStateListener());
        // 主动刷新数据
        onRefresh();
    }

    /**
     * 初始化类别按钮监听器
     */
    private void initMenu() {
        parentView.findViewById(R.id.ll_1).setOnClickListener(view -> openPortItemList(1));
        parentView.findViewById(R.id.ll_2).setOnClickListener(view -> openPortItemList(2));
        parentView.findViewById(R.id.ll_3).setOnClickListener(view -> openPortItemList(3));
        parentView.findViewById(R.id.ll_4).setOnClickListener(view -> openPortItemList(4));
        parentView.findViewById(R.id.ll_5).setOnClickListener(view -> openPortItemList(5));
        parentView.findViewById(R.id.ll_6).setOnClickListener(view -> openPortItemList(6));
        parentView.findViewById(R.id.ll_7).setOnClickListener(view -> openPortItemList(7));
        parentView.findViewById(R.id.ll_8).setOnClickListener(view -> openPortItemList(8));
    }

    private void initAdapter() {
        mQuickAdapter = new PortItemAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mQuickAdapter);
        mQuickAdapter.setOnLoadMoreListener(this);
        mQuickAdapter.openLoadMore(StaticConfig.PAGE_SIZE);
        mQuickAdapter.setAutoLoadMoreSize(StaticConfig.AUTO_SIZE);
        mRecyclerView.addOnItemTouchListener(SimpleClickListener);
        // 一行代码搞定（默认为渐显效果）
        mQuickAdapter.openLoadAnimation();
        // 默认提供5种方法（渐显、缩放、从下到上，从左到右、从右到左）
        // mQuickAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
    }

    /**
     * 添加头部
     */
    private void addHeadView() {
        View headView = getActivity().getLayoutInflater().inflate(R.layout.include_index_header,
                (ViewGroup) mRecyclerView.getParent(), false);
        setUpIcon(headView);
        initAd(headView);
        mQuickAdapter.addHeaderView(headView);
    }

    /**
     * 添加按钮组到头部
     *
     * @param headView 头部
     */
    private void setUpIcon(View headView) {
        headView.findViewById(R.id.ll_shake).setOnClickListener(view -> getOperation().startActivity(ShakeActivity.class));
        headView.findViewById(R.id.ll_ask).setOnClickListener(view -> CommonUtils.joinQQGroup(getActivity()));
        headView.findViewById(R.id.rl_shop).setOnClickListener(view -> getOperation().startActivity(SignInActivity.class));
        headView.findViewById(R.id.ll_integration_shop).setOnClickListener(view -> showIntegrationShop());
    }

    /**
     * 添加轮播界面框架到头部
     *
     * @param headView 头部
     */
    private void initAd(View headView) {
        banner = (AdImageBanner) headView.findViewById(R.id.banner);
        banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
            }

            private void enableDisableSwipeRefresh(boolean b) {
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setEnabled(b);
                }
            }
        });
    }

    /**
     * 获取轮播广告数据
     */
    private void addAdData() {
        Map<String, Object> queryMap = new HashMap<>();
        toSubscribe(RxFactory.getAdvertisingServiceInstance(null)
                        .queryAdvertising(queryMap)
                        .map(Advertising::getElements),
                () -> {
                    queryMap.put("order", "-updatedAt");
                    queryMap.put("where", "{\"state\":true}");
                },
                advertisings -> {
                    banner.setSource(advertisings).startScroll();
                    banner.setOnItemClickL(position -> aliTradeUtils.showItemURLPage(advertisings.get(position).getUrl()));
                },
                throwable ->
                        Logger.e(throwable.getMessage()));
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.goOnScroll();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                // handle back button
                if (showView == mMenuListView && isDirection_left) {
                    // 左边栏菜单打开时，关闭
                    mDrawerLayout.closeDrawer(mMenuListView);
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.pauseScroll();
    }

    @Override
    public void onLoadMoreRequested() {
        mRecyclerView.post(this::QueryItem);
    }

    @Override
    public void onRefresh() {
        addAdData();
        RefreshItem();
    }

    private void RefreshItem() {
        Map<String, Object> queryMap = new HashMap<>();
        toSubscribe(RxFactory.getPortItemServiceInstance(null)
                        .queryPortItem(queryMap)
                        .map(PortItem::getResult),
                () -> {
                    curPage = 0;//重置页码
                    queryMap.put("page", curPage);
                    queryMap.put("type", "4");
                },
                portItems -> {
                    if (!portItems.isEmpty()) {
                        curPage++;
                    }
                    mQuickAdapter.setNewData(portItems);
                    mQuickAdapter.removeAllFooterView();
                    mCurrentCounter = mQuickAdapter.getData().size();
                    mSwipeRefreshLayout.setRefreshing(false);
                },
                throwable -> {
                    mCurrentCounter = 0;
                    mQuickAdapter.removeAllFooterView();
                    mQuickAdapter.getData().clear();
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
                    queryMap.put("type", "4");
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
                                notLoadingView = getActivity().getLayoutInflater().inflate(R.layout.include_index_not_loading,
                                        (ViewGroup) mRecyclerView.getParent(), false);
                                notLoadingView.setOnClickListener(v -> CommonUtils.joinQQGroup(getActivity()));
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

    /**
     * 点击类别打开对应类别的商品列表
     *
     * @param index 类别序数
     */
    private void openPortItemList(int index) {
        if (showView == mMenuListView && isDirection_left) {
            mDrawerLayout.closeDrawer(mMenuListView);// 左边栏菜单打开时，关闭
        }
        Intent item = new Intent(getActivity(), PortItemListActivity.class);
        item.putExtra(PortItemListActivity.getExtra(), index);
        startActivity(item);
    }

    /**
     * 查询当前用户积分用以构造积分商城免登录url并跳转
     */
    private void showIntegrationShop() {
        User currentUser = User.getCurrentUser();
        if (currentUser != null) {
            String objectId = currentUser.getObjectId();
            toSubscribe(RxFactory.getUserServiceInstance(null)//查询当前用户积分
                            .getUser(objectId, "_User[credit]")
                            .map(User::getCredit),
                    () ->
                            catLoadingView.show(getFragmentManager(), TAG)
                    ,
                    credit ->
                            getAutoLoginUrl(objectId, credit)
                    ,
                    throwable -> {
                        catLoadingView.dismissAllowingStateLoss();
                        CommonUtils.showToast("获取用户口袋币失败");
                        Logger.e(throwable.getMessage());
                    });
        } else {
            getOperation().startActivity(LoginActivity.class);
        }
    }

    /**
     * 访问服务器获取真正的积分商城免登陆URL
     *
     * @param objectId 用户ID
     * @param credits  积分
     */
    private void getAutoLoginUrl(String objectId, Integer credits) {
        toSubscribe(RxFactory.getPublicServiceInstance(null)//构造免登陆Url
                        .getAutoLoginUrl(objectId, credits),
                url -> {
                    catLoadingView.dismissAllowingStateLoss();
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WebShopActivity.class);
                    intent.putExtra("url", url); //配置自动登陆地址，每次需根据服务端时间动态生成。
                    startActivity(intent);
                },
                throwable -> {
                    catLoadingView.dismissAllowingStateLoss();
                    CommonUtils.showToast("免登录url获取成功失败");
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
    private class DrawerLayoutStateListener extends DrawerLayout.SimpleDrawerListener {
        /**
         * 当导航菜单滑动的时候被执行
         */
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            showView = drawerView;
        }

        /**
         * 当导航菜单打开时执行
         */
        @Override
        public void onDrawerOpened(View drawerView) {
            isDirection_left = true;
        }

        /**
         * 当导航菜单关闭时执行
         */
        @Override
        public void onDrawerClosed(View drawerView) {
            isDirection_left = false;
        }
    }
}
