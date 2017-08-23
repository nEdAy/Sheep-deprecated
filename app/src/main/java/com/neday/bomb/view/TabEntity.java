package com.neday.bomb.view;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * 主页底部Tab
 *
 * @author nEdAy
 */
public class TabEntity implements CustomTabEntity {
    private final String title;
    private final int selectedIcon;
    private final int unSelectedIcon;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
