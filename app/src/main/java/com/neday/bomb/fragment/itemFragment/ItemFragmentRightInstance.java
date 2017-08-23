package com.neday.bomb.fragment.itemFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.neday.bomb.R;
import com.neday.bomb.StaticConfig;
import com.neday.bomb.activity.PortItemDetailsActivity;
import com.neday.bomb.adapter.PortItemAdapter;
import com.neday.bomb.base.BaseListFragment;
import com.neday.bomb.entity.PortItem;
import com.neday.bomb.network.RxFactory;
import com.neday.bomb.util.AliTradeHelper;
import com.neday.bomb.util.CommonUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Item 右栏实现页面
 */
public class ItemFragmentRightInstance extends BaseListFragment implements BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener {
    private static final String FRAGMENT_INDEX = "fragment_index";
    private PortItemAdapter mQuickAdapter;
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
            AliTradeHelper aliTradeUtils = new AliTradeHelper(getActivity());
            switch (view.getId()) {
                case R.id.ll_get:
                    aliTradeUtils.showItemURLPage(item.getQuan_link());
                    changePressedViewBg(view, R.drawable.ll_get_bg, R.drawable.ll_get_bg_pressed);
                    break;
                case R.id.tx_buy_url:
                    if (item.isCommission_check()) {
                        aliTradeUtils.showItemURLPage("http://www.neday.cn/index_.php?r=p/d&id=" + item.getID());
                    } else {
                        aliTradeUtils.showItemDetailPage(item.getGoodsID());
                    }
                    changePressedViewBg(view, R.drawable.ll_buy_bg, R.drawable.ll_buy_bg_pressed);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        }
    };

    /**
     * 创建新实例
     */
    public static ItemFragmentRightInstance newInstance(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_INDEX, index);
        ItemFragmentRightInstance fragment = new ItemFragmentRightInstance();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void initAdapter() {
        //获得索引值
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCurIndex = bundle.getInt(FRAGMENT_INDEX);
        }
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
     * 当SwipeRefreshLayout下拉刷新时触发
     */
    public void RefreshItem() {
        Map<String, Object> queryMap = new HashMap<>();
        toSubscribe(RxFactory.getPortItemServiceInstance(null)
                        .queryPortItem(queryMap)
                        .map(PortItem::getResult),
                () -> {
                    //隐藏无网络和无数据界面
                    rl_no_network.setVisibility(View.GONE);
                    rl_no_data.setVisibility(View.GONE);
                    curPage = 0;//重置页码
                    queryMap.put("page", curPage);
                    queryMap.put("type", "1");
                    int cid = mCurIndex + 1;//页面序数0-7，而类别1-8（服饰，母婴，化妆品，居家，鞋包配饰，美食，文体车品，数码家电），故+1;
                    queryMap.put("where", "[{\"key\":\"Cid\",\"value\":\"" + cid + "\",\"operation\":\"=\",\"relation\":\"\"}]");
                },
                portItems -> {
                    if (portItems.isEmpty()) {
                        rl_no_data.setVisibility(View.VISIBLE);
                        rl_no_data.setOnClickListener(v -> RefreshItem());
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

    /**
     * 当mRecyclerView上拉加载时触发
     */
    public void LoadMoreItem() {
        Map<String, Object> queryMap = new HashMap<>();
        toSubscribe(RxFactory.getPortItemServiceInstance(null)
                        .queryPortItem(queryMap),
                () -> {
                    queryMap.put("count", "1");
                    queryMap.put("page", curPage);
                    queryMap.put("type", "1");
                    int cid = mCurIndex + 1;//页面序数0-7，而类别1-8（服饰，母婴，化妆品，居家，鞋包配饰，美食，文体车品，数码家电），故+1;
                    queryMap.put("where", "[{\"key\":\"Cid\",\"value\":\"" + cid + "\",\"operation\":\"=\",\"relation\":\"\"}]");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        AlibcTradeSDK.destory();
    }
}

