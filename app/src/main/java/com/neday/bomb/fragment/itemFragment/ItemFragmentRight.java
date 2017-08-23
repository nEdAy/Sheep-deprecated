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
 * Item 右栏选择页面
 */
public class ItemFragmentRight extends BaseFragment {
    private final static int FIRST_FRAGMENT = 0;
    private final static int SECOND_FRAGMENT = 1;
    private final static int THIRD_FRAGMENT = 2;
    private final static int FOURTH_FRAGMENT = 3;
    private final static int FIFTH_FRAGMENT = 4;
    private final static int SIXTH_FRAGMENT = 5;
    private final static int SEVENTH_FRAGMENT = 6;
    private final static int EIGHTH_FRAGMENT = 7;
    private final ArrayList<Fragment> mFragments = new ArrayList<>();
    private View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_main_item_right, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        ViewPager vp_paper = (ViewPager) parentView.findViewById(R.id.vpItemRightPaper);
        vp_paper.setOffscreenPageLimit(1);
        mFragments.add(ItemFragmentRightInstance.newInstance(FIRST_FRAGMENT));
        mFragments.add(ItemFragmentRightInstance.newInstance(SECOND_FRAGMENT));
        mFragments.add(ItemFragmentRightInstance.newInstance(THIRD_FRAGMENT));
        mFragments.add(ItemFragmentRightInstance.newInstance(FOURTH_FRAGMENT));
        mFragments.add(ItemFragmentRightInstance.newInstance(FIFTH_FRAGMENT));
        mFragments.add(ItemFragmentRightInstance.newInstance(SIXTH_FRAGMENT));
        mFragments.add(ItemFragmentRightInstance.newInstance(SEVENTH_FRAGMENT));
        mFragments.add(ItemFragmentRightInstance.newInstance(EIGHTH_FRAGMENT));
        ((SlidingTabLayout) parentView.findViewById(R.id.tl_item_right))
                .setViewPager(vp_paper, getResources().getStringArray(R.array.item_type_array), getActivity(), mFragments);
    }
}
