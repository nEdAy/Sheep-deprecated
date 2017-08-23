package com.neday.bomb.activity;

import android.os.Bundle;

import com.neday.bomb.CustomApplication;
import com.neday.bomb.R;
import com.neday.bomb.base.BaseActivity;
import com.neday.bomb.util.SharedPreferencesHelper;
import com.neday.bomb.view.GuideBanner;

import java.util.ArrayList;

/**
 * 导航页
 *
 * @author nEdAy
 */
public class GuideActivity extends BaseActivity {
    private GuideBanner banner;

    private static ArrayList<Integer> getGuides() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.drawable.guide_img_1);
        list.add(R.drawable.guide_img_2);
        list.add(R.drawable.guide_img_3);
        return list;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        banner = (GuideBanner) findViewById(R.id.banner);
        banner.setIndicatorWidth(6)
                .setIndicatorHeight(6)
                .setIndicatorGap(12)
                .setIndicatorCornerRadius(3.5f)
                .setBarColor(R.color.gray)
                .barPadding(0, 10, 0, 10)
                .setSource(getGuides())
                .startScroll();
        banner.setOnJumpClickL(() -> {
            SharedPreferencesHelper sharedPreferencesHelper = CustomApplication.getInstance().getSpHelper();
            sharedPreferencesHelper.setAllowFirstEnable(false);
            getOperation().startActivity(MainActivity.class);
            finish();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.goOnScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.pauseScroll();
    }
}
