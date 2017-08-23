package com.neday.bomb.base;

import android.os.Bundle;

/**
 * BaseActivity 接口
 *
 * @author nEdAy
 */
interface IBaseActivity {
    /**
     * 绑定渲染视图的布局文件
     *
     * @return 布局文件资源id
     */
    int bindLayout();

    /**
     * 初始化控件
     */
    void initView(Bundle savedInstanceState);
}
