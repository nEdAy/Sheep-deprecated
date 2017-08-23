package com.neday.bomb.activity;

import android.os.Bundle;

import com.neday.bomb.R;
import com.neday.bomb.base.BaseActivity;

/**
 * 关于与合作页
 *
 * @author nEdAy
 */
public class AboutActivity extends BaseActivity {
    @Override
    public int bindLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTintManager();
        initTopBarForLeft(getString(R.string.tx_about), getString(R.string.tx_back));
    }
}
