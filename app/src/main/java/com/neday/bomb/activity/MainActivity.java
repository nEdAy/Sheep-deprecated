package com.neday.bomb.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.neday.bomb.CustomApplication;
import com.neday.bomb.R;
import com.neday.bomb.base.BaseActivity;
import com.neday.bomb.fragment.mainFragment.IndexFragment;
import com.neday.bomb.fragment.mainFragment.ItemFragment;
import com.neday.bomb.fragment.mainFragment.LibraryFragment;
import com.neday.bomb.fragment.mainFragment.MeFragment;
import com.neday.bomb.util.CommonUtils;
import com.neday.bomb.util.IMMLeaks;
import com.neday.bomb.view.TabEntity;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;

/**
 * 主页
 *
 * @author nEdAy
 */
public class MainActivity extends BaseActivity {
    private final ArrayList<Fragment> mFragments = new ArrayList<>();
    private final ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private static final int[] mIconUnSelectIds = {
            R.drawable.tab_index_unselect, R.drawable.tab_item_unselect
            , R.drawable.tab_library_unselect, R.drawable.tab_me_unselect};
    private static final int[] mIconSelectIds = {
            R.drawable.tab_index_select, R.drawable.tab_item_select
            , R.drawable.tab_library_select, R.drawable.tab_me_select};
    private CommonTabLayout mTabLayout;
    //连续按两次返回键就退出标记位
    private long firstTime;
    //记录Fragment的位置
    private int position = 0;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mFragments.add(new IndexFragment());//精选首页
        mFragments.add(new ItemFragment());//优惠快爆
        mFragments.add(new LibraryFragment());//好货专题
        mFragments.add(new MeFragment());//我的页面
        String[] mTitles = getResources().getStringArray(R.array.tab_bottom_array);
        int length = mTitles.length;
        for (int i = 0; i < length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnSelectIds[i]));
        }
        mTabLayout = (CommonTabLayout) findViewById(R.id.tl_tab);
        mTabLayout.setTabData(mTabEntities, this, R.id.fl_content, mFragments);
        setCurrentTab(position);//恢复显示Fragment呈现位置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            IMMLeaks.fixFocusedViewLeak(CustomApplication.getInstance());
        }

    }

    /**
     * 根据onSaveInstanceState回调的状态，恢复当前Fragment state
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);   //将这一行注释掉，阻止Activity没事瞎恢复Fragment的状态
        position = savedInstanceState.getInt("position");
    }

    /**
     * 根据onSaveInstanceState回调的状态，保存当前Fragment state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);   //将这一行注释掉，阻止Activity没事瞎保存Fragment的状态
        outState.putInt("position", position); //记录当前的position
    }

    /**
     * 切换前端显示的页面
     *
     * @param position 页面序数
     */
    private void setCurrentTab(int position) {
        this.position = position;//记录position
        mTabLayout.setCurrentTab(position);//更改底部导航栏按钮状态
    }

    /**
     * 截获Back键动作
     */
    @Override
    public void onBackPressed() {
        if (firstTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            CommonUtils.showToast("再按一次退出程序");
        }
        firstTime = System.currentTimeMillis();
    }

    /**
     * 截获Menu键动作
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            showActionSheet();// 调用弹出菜单
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 弹出Menu菜单
     */
    private void showActionSheet() {
        final String[] stringItems = {"版本更新", "关于与合作", "退出应用"};
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, stringItems, null);
        dialog.isTitleShow(false).layoutAnimation(null).show();
        dialog.setOnOperItemClickL((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    Beta.checkUpgrade();
                    break;
                case 1:
                    getOperation().startActivity(AboutActivity.class);
                    break;
                case 2:
                    finish();
                    break;
                default:
                    break;
            }
            dialog.dismiss();
        });
    }
}
