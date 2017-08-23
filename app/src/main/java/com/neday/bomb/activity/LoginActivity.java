package com.neday.bomb.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.neday.bomb.CustomApplication;
import com.neday.bomb.R;
import com.neday.bomb.StaticConfig;
import com.neday.bomb.base.BaseActivity;
import com.neday.bomb.entity.User;
import com.neday.bomb.network.RxFactory;
import com.neday.bomb.util.CommonUtils;
import com.neday.bomb.util.IMMLeaks;
import com.neday.bomb.util.SharedPreferencesHelper;
import com.neday.bomb.util.StringUtils;
import com.neday.bomb.view.ClearEditText;
import com.neday.bomb.view.loading.CatLoadingView;
import com.orhanobut.logger.Logger;

import net.nashlegend.anypref.AnyPref;

import cn.sharesdk.framework.ShareSDK;


/**
 * 登录页
 *
 * @author nEdAy
 */
public class LoginActivity extends BaseActivity {
    private final static String TAG = "LoginActivity";
    private ClearEditText et_phone;
    private EditText et_password;
    private CatLoadingView catLoadingView;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private ImageView iv_password_see;
    private boolean password_saw;

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTintManager();
        RxBus.get().register(this);
        initTopBarForLeft(getString(R.string.tx_login), getString(R.string.tx_back));
        catLoadingView = new CatLoadingView();
        et_phone = (ClearEditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        iv_password_see = (ImageView) findViewById(R.id.iv_password_see);
        sharedPreferencesHelper = CustomApplication.getInstance().getSpHelper();
        String oldPhone = sharedPreferencesHelper.getUserPhone();
        if (!TextUtils.isEmpty(oldPhone)) {
            et_phone.setText(oldPhone);
            et_password.setFocusable(true);
            et_password.setFocusableInTouchMode(true);
            et_password.requestFocus();
            et_password.requestFocusFromTouch();
        }
        //initial ShareSDK
        ShareSDK.initSDK(CustomApplication.getInstance());
        findViewById(R.id.tv_register).setOnClickListener(v -> getOperation().startActivity(RegisterActivity.class));
        findViewById(R.id.tv_lostPassword).setOnClickListener(v -> getOperation().startActivity(LostPasswordActivity.class));
        findViewById(R.id.btn_login).setOnClickListener(v -> login());
        iv_password_see.setOnClickListener(v -> {
            if (password_saw) {//false
                password_saw = false;
                iv_password_see.setImageResource(R.drawable.ic_see_normal);
                et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                password_saw = true;
                iv_password_see.setImageResource(R.drawable.ic_see_pressed);
                et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
            et_password.setSelection(et_password.getText().length());
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            IMMLeaks.fixFocusedViewLeak(CustomApplication.getInstance());
        }
    }

    private void login() {
        String phone = et_phone.getText().toString().trim().replace(" ", "");
        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            CommonUtils.showToast(R.string.toast_error_phone_null);
            et_phone.requestFocus();
            CommonUtils.setShakeAnimation(et_phone);
            return;
        }
        if (!StringUtils.isPhoneNumberValid(phone)) {
            CommonUtils.showToast(R.string.toast_error_phone_error);
            et_phone.requestFocus();
            CommonUtils.setShakeAnimation(et_phone);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            CommonUtils.showToast(R.string.toast_error_password_null);
            et_password.requestFocus();
            CommonUtils.setShakeAnimation(et_password);
            return;
        }
        if (!StringUtils.isValidPassword(password)) {
            CommonUtils.showToast(R.string.toast_error_password_error);
            et_password.requestFocus();
            CommonUtils.setShakeAnimation(et_password);
            return;
        }
        /**隐藏软键盘**/
        View _view = getWindow().peekDecorView();
        if (_view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(_view.getWindowToken(), 0);
        }
        if (!CommonUtils.isNetworkAvailable()) {
            CommonUtils.showToast(R.string.network_tips);
            return;
        }
        userLogin(phone, password);
    }

    /**
     * 用户登录
     *
     * @param phone    手机号
     * @param password 原始密码
     */
    private void userLogin(String phone, String password) {
        toSubscribe(RxFactory.getUserServiceInstance(null)
                        .login(phone, StringUtils.getMD5(password)),
                () ->
                        catLoadingView.show(getSupportFragmentManager(), TAG),
                user -> {
                    catLoadingView.dismissAllowingStateLoss();
                    sharedPreferencesHelper.setUserPhone(phone);
                    AnyPref.put(user, "_CurrentUser");// 将私有token保存
//                    loginCyan(user);
//                    PushManager.getInstance().bindAlias(CustomApplication.getInstance(), user.getObjectId());// 绑定推送别名
                    finish();
                },
                throwable -> {
                    catLoadingView.dismissAllowingStateLoss();
                    CommonUtils.showToast("你输入的密码和账户名不匹配，请重新输入后重试");
                    Logger.e(throwable.getMessage());
                });
    }

//    private void loginCyan(User user) {
//        AccountInfo account = new AccountInfo();
//        //应用自己的用户id
//        account.isv_refer_id = user.getObjectId();
//        account.nickname = user.getNickname();
//        account.img_url = user.getAvatar();
//        CyanSdk.getInstance(LoginActivity.this).setAccountInfo(account, new CallBack() {
//            @Override
//            public void success() {
//                Logger.i("成功");
//            }
//
//            @Override
//            public void error(CyanException e) {
//                Logger.e(e.error_msg);
//            }
//        });
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(mContext);
        RxBus.get().unregister(this);
    }

    /**
     * 接受事件，请求登录
     *
     * @param user 用户登录信息
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(StaticConfig.ACTION_REGISTER_SUCCESS_FINISH)
            }
    )
    public void userLogin(User user) {
        userLogin(user.getUsername(), user.getPassword());
    }

}
