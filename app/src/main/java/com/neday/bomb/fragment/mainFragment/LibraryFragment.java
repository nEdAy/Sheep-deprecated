package com.neday.bomb.fragment.mainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.neday.bomb.R;
import com.neday.bomb.activity.PortItemListActivity;
import com.neday.bomb.activity.SearchAndTypeActivity;
import com.neday.bomb.base.BaseFragment;

/**
 * 其他商品库
 */
public class LibraryFragment extends BaseFragment {
    private View parentView;
    private View showView;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mMenuListView;//左边栏菜单
    private boolean isDirection_left;//菜单打开/关闭状态

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_main_library, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        setStatusBarHeight(parentView);// 配合状态栏下移
        ImageView iv_search = (ImageView) parentView.findViewById(R.id.btn_search);
        iv_search.setOnClickListener(view -> getOperation().startActivity(SearchAndTypeActivity.class));
        parentView.findViewById(R.id.iv_type_search).setOnClickListener(view -> {
            if (showView == mMenuListView) {
                if (!isDirection_left) { // 左边栏菜单关闭时，打开
                    mDrawerLayout.openDrawer(mMenuListView);
                } else {// 左边栏菜单打开时，关闭
                    mDrawerLayout.closeDrawer(mMenuListView);
                }
            }
        });
        mDrawerLayout = (DrawerLayout) parentView.findViewById(R.id.drawer_layout);
        mMenuListView = (RelativeLayout) parentView.findViewById(R.id.left_drawer);
        this.showView = mMenuListView;
        // 设置抽屉打开时，主要内容区被自定义阴影覆盖
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        mDrawerLayout.setDrawerListener(new DrawerLayoutStateListener());
        initMenu();
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
