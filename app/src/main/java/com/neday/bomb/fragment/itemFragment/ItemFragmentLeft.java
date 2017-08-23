package com.neday.bomb.fragment.itemFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.neday.bomb.R;
import com.neday.bomb.base.BaseFragment;

import java.util.ArrayList;

/**
 * Item 左栏选择页面
 */
public class ItemFragmentLeft extends BaseFragment {
    private final static int FIRST_FRAGMENT = 0;
    private final static int SECOND_FRAGMENT = 1;
    private final static int THIRD_FRAGMENT = 2;
    private final ArrayList<Fragment> mFragments = new ArrayList<>();
    private View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_main_item_left, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        ViewPager vp_paper = (ViewPager) parentView.findViewById(R.id.vpItemLeftPaper);
        vp_paper.setOffscreenPageLimit(1);
        mFragments.add(ItemFragmentLeftInstance.newInstance(FIRST_FRAGMENT));
        mFragments.add(ItemFragmentLeftInstance.newInstance(SECOND_FRAGMENT));
        mFragments.add(ItemFragmentLeftInstance.newInstance(THIRD_FRAGMENT));
        ((SlidingTabLayout) parentView.findViewById(R.id.tl_library))
                .setViewPager(vp_paper, getResources().getStringArray(R.array.item_mall_array), getActivity(), mFragments);
    }
}
