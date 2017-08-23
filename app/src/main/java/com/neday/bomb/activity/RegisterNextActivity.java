package com.neday.bomb.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.widget.NormalDialog;
import com.hwangjr.rxbus.RxBus;
import com.neday.bomb.CustomApplication;
import com.neday.bomb.R;
import com.neday.bomb.StaticConfig;
import com.neday.bomb.base.BaseActivity;
import com.neday.bomb.entity.User;
import com.neday.bomb.network.RxFactory;
import com.neday.bomb.util.AliTradeHelper;
import com.neday.bomb.util.CommonUtils;
import com.neday.bomb.util.IMMLeaks;
import com.neday.bomb.util.StringUtils;
import com.neday.bomb.view.loading.CatLoadingView;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 注册页-第二步
 *
 * @author nEdAy
 */
public class RegisterNextActivity extends BaseActivity {
    private final static String TAG = "RegisterTActivity";
    private final static String country = "86";
    private CatLoadingView catLoadingView;
    private EditText et_password, et_sms, et_invite;
    private ImageView iv_password_see, iv_agreement;
    private TextView tv_sms;
    private boolean isVoice;
    private boolean password_saw = false, agreement = true;
    private String phone;
    private TextView tv_top_message;
    private TimeCount timeCount;
    private final EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //走短信验证
                    runOnUiThread(() -> tv_sms.setClickable(false));
                } else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
                    //请求发送语音验证码，无返回
                    runOnUiThread(() -> {
                        tv_sms.setText(" 致电中...请稍等 ");
                        tv_sms.setEnabled(false);
                    });
                }
            } else {
                runOnUiThread(() -> {
                    timeCount.cancel();
                    tv_sms.setEnabled(true);
                    tv_sms.setText(" 异常，请重试 ");
                });
                try {
                    Throwable throwable = (Throwable) data;
                    throwable.printStackTrace();
                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");//错误描述
                    int status = object.optInt("status");//错误代码
                    if (status > 0 && !TextUtils.isEmpty(des)) {
                        CommonUtils.showToast(des);
                    }
                } catch (Exception ignored) {
                }
            }
        }
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_register_next;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTintManager();
        RxBus.get().register(this);
        initTopBarForLeft(getString(R.string.tx_register), getString(R.string.tx_back));
        catLoadingView = new CatLoadingView();
        et_password = (EditText) findViewById(R.id.et_password);
        et_sms = (EditText) findViewById(R.id.et_sms);
        et_invite = (EditText) findViewById(R.id.et_invite);
        tv_sms = (TextView) findViewById(R.id.tv_sms);
        iv_password_see = (ImageView) findViewById(R.id.iv_password_see);
        iv_agreement = (ImageView) findViewById(R.id.iv_agreement);
        phone = getIntent().getStringExtra("phone");
        tv_top_message = (TextView) findViewById(R.id.tv_top_message);
        ((TextView) findViewById(R.id.tv_top_phone)).setText("+86 " + phone);
        tv_sms.setOnClickListener(v -> requestVerificationCode());
        iv_password_see.setOnClickListener(v -> changePasswordSee());
        findViewById(R.id.rl_agreement_iv).setOnClickListener(v -> changeAgreementIv());
        findViewById(R.id.btn_submit).setOnClickListener(v -> register());
        findViewById(R.id.tv_agreement).setOnClickListener(v -> {
            AliTradeHelper aliTradeUtils = new AliTradeHelper(this);
            aliTradeUtils.showItemURLPage(StaticConfig.KZ_YHSYXY);
        });
        initSms();
        sendSms();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            IMMLeaks.fixFocusedViewLeak(CustomApplication.getInstance());
        }
    }

    /**
     * 初始化 MobSMS SDK
     */
    private void initSms() {
        SMSSDK.initSDK(getBaseContext(), StaticConfig.MOB_APP_KEY, StaticConfig.MOB_APP_SECRET);
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    /**
     * 改变密码隐藏按钮样式和输入框类型
     */
    private void changePasswordSee() {
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
    }

    /**
     * 改变同意合同样式
     */
    private void changeAgreementIv() {
        if (agreement) {//true
            agreement = false;
            iv_agreement.setImageResource(R.drawable.check_normal);
        } else {
            agreement = true;
            iv_agreement.setImageResource(R.drawable.check_pressed);
        }
    }

    /**
     * 重新请求短信验证码
     */
    private void requestVerificationCode() {
        //先短信验证码，闲置30s后切换语音验证码
        if (isVoice) {
            final NormalDialog dialog = new NormalDialog(mContext);
            dialog.content("确定后将致电您的手机号并语音播报验证码，如不希望被来点打扰请返回。")
                    .style(NormalDialog.STYLE_TWO)
                    .titleTextSize(23)
                    .showAnim(new BounceTopEnter())
                    .dismissAnim(new SlideBottomExit())
                    .show();
            dialog.setOnBtnClickL(
                    dialog::dismiss,
                    () -> {
                        dialog.dismiss();
                        if (!CommonUtils.isNetworkAvailable()) {
                            CommonUtils.showToast(R.string.network_tips);
                        } else {
                            SMSSDK.getVoiceVerifyCode(country, phone);
                            tv_top_message.setText("我们正在致电语音播报验证码到您的手机号");
                        }
                    });
        }
    }

    /**
     * 请求验证码
     */
    private void sendSms() {
        timeCount = new TimeCount(30000, 1000);//30秒倒计时
        timeCount.start();
        tv_top_message.setText("我们已发送验证码短信到您的手机号");
        SMSSDK.getVerificationCode(country, phone);
    }

    /**
     * 校验注册信息，并开始请求注冊
     */
    private void register() {
        if (!agreement) {
            CommonUtils.showToast("您尚未同意《用户使用协议》");
            return;
        }
        String password = et_password.getText().toString().trim().replace(" ", "");
        String sms = et_sms.getText().toString().trim().replace(" ", "");
        String invite = et_invite.getText().toString().trim().replace(" ", "");
        if (!StringUtils.isValidPassword(password)) {
            et_password.requestFocus();
            CommonUtils.setShakeAnimation(et_password);
            CommonUtils.showToast(R.string.toast_error_password_error);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            et_password.requestFocus();
            CommonUtils.setShakeAnimation(et_password);
            CommonUtils.showToast(R.string.toast_error_password_null);
            return;
        }
        if (TextUtils.isEmpty(sms)) {
            et_sms.requestFocus();
            CommonUtils.setShakeAnimation(et_sms);
            CommonUtils.showToast(R.string.toast_error_sms_null);
            return;
        }
        if (!CommonUtils.isNetworkAvailable()) {
            CommonUtils.showToast(R.string.network_tips);
            return;
        }
        if (TextUtils.isEmpty(invite)) {
            signUpWithNoInvite(password, sms);
        } else {
            signUpWithInvite(password, sms, invite);
        }
    }

    /**
     * 注册（不带邀请人）
     *
     * @param password 密码（MD5后）
     * @param sms      验证码
     */
    private void signUpWithNoInvite(String password, String sms) {
        User user = new User();
        toSubscribe(RxFactory.getUserServiceInstance(null)
                        .signUp(user, sms),
                () -> {
                    catLoadingView.show(getSupportFragmentManager(), TAG);
                    user.setUsername(phone);
                    user.setPassword(StringUtils.getMD5(password));
                },
                baseObject -> {
                    catLoadingView.dismissAllowingStateLoss();
                    CommonUtils.showToast("注册成功");
                    // 发送事件，通知登陆页面退出
                    User user_login = new User();
                    user_login.setUsername(phone);
                    user_login.setPassword(password);
                    RxBus.get().post(StaticConfig.ACTION_REGISTER_SUCCESS_FINISH, user_login);
                    finish();
                },
                throwable -> {
                    CommonUtils.showToast("注册失败:" + throwable.getMessage());
                    Logger.e(throwable.getMessage());
                });
    }

    /**
     * 注册（带邀请人）
     *
     * @param password 密码（MD5后）
     * @param sms      验证码
     * @param invite   邀请人手机号
     */
    private void signUpWithInvite(String password, String sms, String invite) {
        User user = new User();
        toSubscribe(RxFactory.getUserServiceInstance(null)
                        .signUp(user, sms, invite),
                () -> {
                    catLoadingView.show(getSupportFragmentManager(), TAG);
                    user.setUsername(phone);
                    user.setPassword(StringUtils.getMD5(password));
                },
                baseObject -> {
                    catLoadingView.dismissAllowingStateLoss();
                    CommonUtils.showToast("注册成功");
                    // 发送事件，通知登陆页面退出
                    User user_login = new User();
                    user_login.setUsername(phone);
                    user_login.setPassword(password);
                    RxBus.get().post(StaticConfig.ACTION_REGISTER_SUCCESS_FINISH, user_login);
                    finish();
                },
                throwable -> {
                    catLoadingView.dismissAllowingStateLoss();
                    CommonUtils.showToast("注册失败:" + throwable.getMessage());
                    Logger.e(throwable.getMessage());
                });
    }

    /**
     * 用户back按键回馈
     */
    @Override
    public void onBackPressed() {
        final NormalDialog dialog = new NormalDialog(mContext);
        dialog.content("验证码短信可能略有延迟，确定返回并重新开始？")//
                .style(NormalDialog.STYLE_TWO)//
                .titleTextSize(23)//
                .showAnim(new BounceTopEnter())//
                .dismissAnim(new SlideBottomExit())//
                .show();
        dialog.setOnBtnClickL(
                dialog::dismiss,
                () -> {
                    dialog.superDismiss();
                    finish();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlibcTradeSDK.destory();
        if (timeCount != null) {
            timeCount.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SMSSDK.registerEventHandler(eh);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxBus.get().unregister(this);
        SMSSDK.unregisterEventHandler(eh);
    }

    class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_sms.setText(String.format(getString(R.string.countdown_number), millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            tv_sms.setClickable(true);
            isVoice = true;
            tv_sms.setText(" 发送语音验证 ");
        }
    }
}
