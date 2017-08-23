package com.neday.bomb.activity;


import android.os.Bundle;

import com.neday.bomb.R;
import com.neday.bomb.base.BaseActivity;

/**
 * 搜索商品页
 *
 * @author nEdAy
 */
public class SearchResultShowActivity extends BaseActivity {
//    private final ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_search_result_show;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTintManager();
        String searchWhat = getIntent().getStringExtra("searchWhat");
        initTopBarForLeft(searchWhat, getString(R.string.tx_back));

//        ViewPager vp_paper = (ViewPager) findViewById(R.id.vpItemSearchPaper);
//        vp_paper.setOffscreenPageLimit(1);
//        mFragments.add(SearchResultInstanceA.newInstance(searchWhat));
//        mFragments.add(SearchResultInstanceB.newInstance(searchWhat));
//        ((SlidingTabLayout) findViewById(R.id.tl_search)).
//                setViewPager(vp_paper, getResources().getStringArray(R.array.item_search_array), this, mFragments);
    }
}