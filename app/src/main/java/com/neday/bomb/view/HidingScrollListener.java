package com.neday.bomb.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.neday.bomb.StaticConfig;

/**
 * RecyclerView滑动监听
 */
public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int visibleItemNum = layoutManager.findLastVisibleItemPosition();
        if (visibleItemNum < StaticConfig.PAGE_SIZE) {
            onRemoveAll();
            controlsVisible = false;
            scrolledDistance = 0;
        } else if (visibleItemNum == StaticConfig.PAGE_SIZE + 1) {
            onStart();
        } else {
            onMove(visibleItemNum);
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide();
                controlsVisible = false;
                scrolledDistance = 0;
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                onShow();
                controlsVisible = true;
                scrolledDistance = 0;
            }
            if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                scrolledDistance += dy;
            }
        }
    }

    public abstract void onStart();

    public abstract void onRemoveAll();

    public abstract void onMove(int visibleItemNum);

    public abstract void onHide();

    public abstract void onShow();
}