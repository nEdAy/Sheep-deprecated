package com.neday.bomb.base;

import com.neday.bomb.activity.LoginActivity;
import com.neday.bomb.entity.User;
import com.neday.bomb.util.CommonUtils;

/**
 * 必须处于用户登录状态Activity的基类-用于检测用户是否登陆或是否有其他设备登录了同一账号
 *
 * @author nEdAy
 */
public abstract class BaseOnlineActivity extends BaseActivity implements IBaseOnlineActivity {
    protected User currentUser;

    @Override
    public void onResume() {
        super.onResume();
        //锁屏状态下的检测
        if (User.getCurrentUser() == null) {
            CommonUtils.showToast("请先登录");
            getOperation().startActivity(LoginActivity.class);
            finish();
        } else {
            currentUser = User.getCurrentUser();
            onResumeAfter();//检查登录状态良好后操作，此时currentUser不为null
        }
    }
}
