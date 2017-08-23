package com.neday.bomb.fragment.mainFragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.facebook.drawee.view.SimpleDraweeView;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.widget.NormalDialog;
import com.neday.bomb.CustomApplication;
import com.neday.bomb.R;
import com.neday.bomb.activity.AboutActivity;
import com.neday.bomb.activity.AccountActivity;
import com.neday.bomb.activity.CreditsHistoryActivity;
import com.neday.bomb.activity.LoginActivity;
import com.neday.bomb.activity.UpdateInfoActivity;
import com.neday.bomb.activity.VipActivity;
import com.neday.bomb.base.BaseFragment;
import com.neday.bomb.entity.User;
import com.neday.bomb.network.RxFactory;
import com.neday.bomb.util.AliTradeHelper;
import com.neday.bomb.util.CommonUtils;
import com.neday.bomb.util.SharedPreferencesHelper;
import com.neday.bomb.view.DampView;
import com.neday.bomb.view.RiseNumberTextView;
import com.neday.bomb.view.ShareDialog;
import com.orhanobut.logger.Logger;

/**
 * 我的页面
 */
public class MeFragment extends BaseFragment implements DampView.IRefreshListener {
    private static final int[] selfcenter_bg = {R.drawable.selfcenter_bg_0, R.drawable.selfcenter_bg_1,
            R.drawable.selfcenter_bg_2, R.drawable.selfcenter_bg_3, R.drawable.selfcenter_bg_4};
    private final AliTradeHelper aliTradeUtils = new AliTradeHelper(getActivity());
    private View parentView;
    private RiseNumberTextView tv_credits_value;
    private User currentUser;
    private SimpleDraweeView riv_avatar;
    private TextView tv_nickname;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private ImageView iv_damp;
    private RelativeLayout rl_top;
    private RelativeLayout rl_me;
    private TextView tv_l_and_r;
    private RelativeLayout rl_credits;
    private ImageView iv_vip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_main_me, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        setStatusBarHeight(parentView);//配合状态栏下移
        iv_vip = (ImageView) parentView.findViewById(R.id.iv_vip);
        rl_top = (RelativeLayout) parentView.findViewById(R.id.rl_top);
        rl_me = (RelativeLayout) parentView.findViewById(R.id.rl_me);
        rl_credits = (RelativeLayout) parentView.findViewById(R.id.rl_credits);
        tv_l_and_r = (TextView) parentView.findViewById(R.id.tv_l_and_r);
        iv_damp = (ImageView) parentView.findViewById(R.id.iv_damp);
        riv_avatar = (SimpleDraweeView) parentView.findViewById(R.id.riv_avatar);
        tv_nickname = (TextView) parentView.findViewById(R.id.tv_nickname);
        tv_credits_value = (RiseNumberTextView) parentView.findViewById(R.id.tv_credits_value);
        DampView dampView = (DampView) parentView.findViewById(R.id.dampView);
        dampView.setImageView(iv_damp);
        dampView.setOnRefreshListener(this);
        parentView.findViewById(R.id.ll_option).setOnClickListener(view -> getOperation().startActivity(AccountActivity.class));
        parentView.findViewById(R.id.iv_level).setOnClickListener(v -> getOperation().startActivity(VipActivity.class));
        parentView.findViewById(R.id.rl_me).setOnClickListener(v -> getOperation().startActivity(AccountActivity.class));
        parentView.findViewById(R.id.ll_encourage).setOnClickListener(v -> encourageWe());
        parentView.findViewById(R.id.ll_about).setOnClickListener(v -> getOperation().startActivity(AboutActivity.class));
        parentView.findViewById(R.id.ll_feedback).setOnClickListener(v -> CommonUtils.joinQQGroup(getActivity()));
        parentView.findViewById(R.id.ll_attention).setOnClickListener(v -> attentionWe());
//        parentView.findViewById(R.id.ll_express).setOnClickListener(v -> getOperation().startActivity(ExpressActivity.class));
//        parentView.findViewById(R.id.ll_voucher).setOnClickListener(v -> getOperation().startActivity(ExpressVoucherActivity.class));
        parentView.findViewById(R.id.ll_share).setOnClickListener(v -> new ShareDialog(getActivity()).
                builder(getString(R.string.app_name), "口袋快爆-每天千款优惠券秒杀，一折限时疯抢！",
                        "http://app-10046956.cos.myqcloud.com/toAvatar.png",
                        "http://a.app.qq.com/o/simple.jsp?pkgname=com.neday.bomb").show());
        parentView.findViewById(R.id.rl_credits).setOnClickListener(view -> {
            if (currentUser != null) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CreditsHistoryActivity.class);
                intent.putExtra("userId", currentUser.getObjectId());
                startActivity(intent);
            }
        });
        tv_l_and_r.setOnClickListener(v -> getOperation().startActivity(LoginActivity.class));
        sharedPreferencesHelper = CustomApplication.getInstance().getSpHelper();
        parentView.findViewById(R.id.rl_0).setOnClickListener(v -> aliTradeUtils.showCart());
        parentView.findViewById(R.id.rl_1).setOnClickListener(v -> aliTradeUtils.showOrder(1));
        parentView.findViewById(R.id.rl_2).setOnClickListener(v -> aliTradeUtils.showOrder(2));
        parentView.findViewById(R.id.rl_3).setOnClickListener(v -> aliTradeUtils.showOrder(3));
        parentView.findViewById(R.id.rl_4).setOnClickListener(v -> aliTradeUtils.showOrder(4));
    }


    @Override
    public void onResume() {
        super.onResume();
        initUserInfoAndChangeSkin();
    }


    @Override
    public void onRefresh() {
        if (currentUser != null && CommonUtils.isNetworkAvailable()) {
            getUserByObjectId(currentUser.getObjectId());
        }
    }


    /**
     * 更新用户信息、点击状态、换皮肤
     */
    private void initUserInfoAndChangeSkin() {
        currentUser = User.getCurrentUser();
        if (currentUser != null && CommonUtils.isNetworkAvailable()) {
            getUserByObjectId(currentUser.getObjectId());
            rl_top.setVisibility(View.VISIBLE);
            rl_me.setVisibility(View.VISIBLE);
            rl_credits.setVisibility(View.VISIBLE);
            tv_l_and_r.setVisibility(View.GONE);
        } else {
            rl_top.setVisibility(View.GONE);
            rl_me.setVisibility(View.GONE);
            rl_credits.setVisibility(View.GONE);
            tv_l_and_r.setVisibility(View.VISIBLE);
        }
        int centerBg = sharedPreferencesHelper.getCenterBg();
        iv_damp.setImageResource(selfcenter_bg[centerBg]);
    }

    /**
     * 获取用户信息
     *
     * @param objectId 用户ID
     */
    private void getUserByObjectId(String objectId) {
        toSubscribe(RxFactory.getUserServiceInstance(null)
                        .getUser(objectId),
                user -> {
                    tv_credits_value.withNumber(user.getCredit());
                    tv_credits_value.start();
                    refreshUser(user);
                },
                throwable ->
                        Logger.e(throwable.getMessage()));
    }

    /**
     * 更新用户信息前端显示
     *
     * @param user 用户信息
     */
    private void refreshUser(User user) {
        refreshAvatar(user.getAvatar());
        String nickname = user.getNickname();
        if (TextUtils.isEmpty(nickname) || nickname.equals(getString(R.string.default_nickname))) {
            tv_nickname.setText(getString(R.string.default_nickname));
            tv_nickname.setOnClickListener(view -> getOperation().startActivity(UpdateInfoActivity.class));
        } else {
            tv_nickname.setText(nickname);
        }
        int credit = user.getCredit();
        if (credit >= 200000) {
            iv_vip.setImageResource(R.drawable.level_10);
        } else if (credit >= 100000) {
            iv_vip.setImageResource(R.drawable.level_9);
        } else if (credit >= 50000) {
            iv_vip.setImageResource(R.drawable.level_8);
        } else if (credit >= 15000) {
            iv_vip.setImageResource(R.drawable.level_7);
        } else if (credit >= 5000) {
            iv_vip.setImageResource(R.drawable.level_6);
        } else if (credit >= 2000) {
            iv_vip.setImageResource(R.drawable.level_5);
        } else if (credit >= 1000) {
            iv_vip.setImageResource(R.drawable.level_4);
        } else if (credit >= 500) {
            iv_vip.setImageResource(R.drawable.level_3);
        } else if (credit >= 200) {
            iv_vip.setImageResource(R.drawable.level_2);
        } else if (credit >= 100) {
            iv_vip.setImageResource(R.drawable.level_1);
        } else {
            iv_vip.setImageResource(R.drawable.level_0);
        }
    }

    /**
     * 更新头像 refreshAvatar
     */
    private void refreshAvatar(String avatar) {
        if (avatar != null && !avatar.equals("")) {
            Uri uri = Uri.parse(avatar);
            riv_avatar.setImageURI(uri);
        } else {
            riv_avatar.setImageResource(R.drawable.avatar_default);
        }
    }

    /**
     * 关注微信
     */
    private void attentionWe() {
        final NormalDialog dialog = new NormalDialog(getActivity());
        dialog.content("跳转微信—通讯录-添加朋友-查找公众号—搜索\"神马快爆订阅号\"(点击跳转微信可以直接粘贴公众号哦)")//
                .style(NormalDialog.STYLE_TWO)//
                .btnNum(3)
                .btnText(getString(R.string.tx_cancel), getString(R.string.tx_determine), "跳转微信")//
                .showAnim(new BounceTopEnter())//
                .dismissAnim(new SlideBottomExit())//
                .show();
        dialog.setOnBtnClickL(
                dialog::dismiss,
                dialog::dismiss,
                () -> {
                    //复制数据到剪切板
                    ClipboardManager mClipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData myClip;
                    myClip = ClipData.newPlainText("text", getString(R.string.app_name));
                    mClipboardManager.setPrimaryClip(myClip);
                    try {
                        Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                        startActivity(intent);
                    } catch (Exception ignored) {
                        CommonUtils.showToast("您尚未安装微信APP");
                    }
                    dialog.superDismiss();
                });
    }


    /**
     * 鼓励我们----打开应用商店
     */
    private void encourageWe() {
        final NormalDialog dialog = new NormalDialog(getActivity());
        dialog.content("袋王亲，如果您觉得我们做的还不错，请给我一些鼓励吧！")//
                .style(NormalDialog.STYLE_TWO)//
                .btnNum(3)
                .btnText(getString(R.string.tx_cancel), getString(R.string.tx_determine), "跳转商店")//
                .showAnim(new BounceTopEnter())//
                .dismissAnim(new SlideBottomExit())//
                .show();
        dialog.setOnBtnClickL(
                dialog::dismiss,
                dialog::dismiss,
                () -> {
                    CommonUtils.launchAppDetail(getActivity(), "");
                    dialog.superDismiss();
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        AlibcTradeSDK.destory();
    }

}
