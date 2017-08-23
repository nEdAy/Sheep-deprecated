package com.neday.bomb.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.neday.bomb.CustomApplication;
import com.neday.bomb.R;
import com.neday.bomb.base.BaseActivity;
import com.neday.bomb.network.RxFactory;
import com.neday.bomb.util.CommonUtils;
import com.neday.bomb.util.IMMLeaks;
import com.neday.bomb.util.StringUtils;
import com.neday.bomb.view.ClearEditText;
import com.neday.bomb.view.loading.CatLoadingView;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册页-第一步
 *
 * @author nEdAy
 */
public class RegisterActivity extends BaseActivity {
    private final static String TAG = "RegisterActivity";
    private CatLoadingView catLoadingView;
    private ClearEditText et_phone;
    private String phone;

    @Override
    public int bindLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTintManager();
        initTopBarForLeft(getString(R.string.tx_register), getString(R.string.tx_back));
        catLoadingView = new CatLoadingView();
        et_phone = (ClearEditText) findViewById(R.id.et_phone);
        findViewById(R.id.btn_submit).setOnClickListener(v -> checkRegister());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            IMMLeaks.fixFocusedViewLeak(CustomApplication.getInstance());
        }
    }

    /**
     * 校验第一步注册信息并传入下一步
     */
    private void checkRegister() {
        phone = et_phone.getText().toString().trim().replace(" ", "");
        if (TextUtils.isEmpty(phone)) {
            et_phone.requestFocus();
            CommonUtils.setShakeAnimation(et_phone);
            CommonUtils.showToast(R.string.toast_error_phone_null);
            return;
        }
        if (!StringUtils.isPhoneNumberValid(phone)) {
            et_phone.requestFocus();
            CommonUtils.setShakeAnimation(et_phone);
            CommonUtils.showToast(R.string.toast_error_phone_error);
            return;
        }
        //隐藏软键盘
        View _view = getWindow().peekDecorView();
        if (_view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(_view.getWindowToken(), 0);
        }
        if (!CommonUtils.isNetworkAvailable()) {
            CommonUtils.showToast(R.string.network_tips);
            return;
        }
        Map<String, Object> queryMap = new HashMap<>();
        toSubscribe(RxFactory.getUserServiceInstance(null)
                        .queryUser(queryMap)
                        .map(user -> Integer.parseInt(user.getCount()) > 0),
                () -> {
                    catLoadingView.show(getSupportFragmentManager(), TAG);
                    String where = "[{\"key\":\"username\",\"value\":\"" + phone + "\",\"operation\":\"=\",\"relation\":\"\"}]";
                    queryMap.put("where", where);
                    queryMap.put("count", "1");
                    queryMap.put("limit", "0");
                },
                isRegistered -> {
                    catLoadingView.dismissAllowingStateLoss();
                    if (isRegistered) {
                        CommonUtils.showToast("手机号已被注册，请尝试登录或通过忘记密码找回");
                        finish();
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(mContext, RegisterNextActivity.class);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                        finish();
                    }
                },
                throwable -> {
                    catLoadingView.dismissAllowingStateLoss();
                    CommonUtils.showToast(getString(R.string.register_error) + throwable.getMessage());
                    Logger.e(throwable.getMessage());
                });
    }

}
