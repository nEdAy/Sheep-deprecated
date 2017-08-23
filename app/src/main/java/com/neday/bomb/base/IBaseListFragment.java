package com.neday.bomb.base;

/**
 * BaseListFragment 接口
 *
 * @author nEdAy
 */
interface IBaseListFragment {
    /**
     * 绑定渲染适配器
     */
    void initAdapter();

    /**
     * 上拉加载
     */
    void LoadMoreItem();

    /**
     * 下拉刷新
     */
    void RefreshItem();

}
