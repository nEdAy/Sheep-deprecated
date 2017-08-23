package com.neday.bomb.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.neday.bomb.CustomApplication;
import com.neday.bomb.R;
import com.neday.bomb.base.BaseActivity;
import com.neday.bomb.entity.PortItem;
import com.neday.bomb.util.AliTradeHelper;
import com.neday.bomb.util.CommonUtils;
import com.neday.bomb.util.SharedPreferencesHelper;
import com.neday.bomb.view.ShareDialog;


/**
 * 商品详情页
 *
 * @author nEdAy
 */
public class PortItemDetailsActivity extends BaseActivity {
    private PortItem portItem;
    private final AliTradeHelper aliTradeUtils = new AliTradeHelper(this);//构造阿里百川服务
//    private CyanSdk cyanSdk;

    public static String getExtra() {
        return "portItem";
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_port_item_details;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTintManager();
        //获取商品信息
        portItem = getIntent().getParcelableExtra(getExtra());
        //注册点击事件监听
        findViewById(R.id.ll_get).setOnClickListener(view1 -> getQuan());
        findViewById(R.id.ll_buy).setOnClickListener(view1 -> buyItem());
        findViewById(R.id.ll_add).setOnClickListener(v -> aliTradeUtils.addToCart(portItem.getGoodsID()));
        //初始化标题栏
        String img_url = portItem.getPic();
        String cms_url = "http://www.neday.cn/index_.php?r=p/d&id=" + portItem.getID();
        initTopBarForBoth("精选详情", getString(R.string.tx_back), "分享", () ->
                new ShareDialog(mContext).builder("口袋快爆", "您的好友向您推荐了一款商品", img_url, cms_url).show());
//        initCyan();
//        cyanSdk.addCommentToolbar(this, String.valueOf(portItem.getGoodsID()), portItem.getTitle(), cms_url);
        //初始化商品主图
        SimpleDraweeView iv_img_shower = (SimpleDraweeView) findViewById(R.id.iv_img_shower);
        SharedPreferencesHelper sharedPreferencesHelper = CustomApplication.getInstance().getSpHelper();
        if (img_url != null && !img_url.equals("")) {
            Uri uri = Uri.parse(img_url);
            if (CommonUtils.isMobile()) {
                if (sharedPreferencesHelper.isAllowProvinceFlowModel()) {
                    iv_img_shower.setImageURI(uri + getString(R.string._120x120_jpg));
                } else {
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setLowResImageRequest(ImageRequest.fromUri(uri + getString(R.string._120x120_jpg)))
                            .setImageRequest(ImageRequest.fromUri(uri + getString(R.string._300x300_jpg)))
                            .setOldController(iv_img_shower.getController())
                            .build();
                    iv_img_shower.setController(controller);
                }
            } else {
                iv_img_shower.setImageURI(uri + getString(R.string._300x300_jpg));
            }
        } else {
            iv_img_shower.setImageResource(R.drawable.icon_stub);
        }
        //显示标题
        ((TextView) findViewById(R.id.tv_title)).setText(portItem.getTitle());
        //显示券后价
        String price = portItem.getPrice();
        ((TextView) findViewById(R.id.tv_money)).setText(price);
        //判断是否是天猫
        String mall_name = portItem.getIsTmall() == 1 ? "天猫商城" : "淘宝";
        ((TextView) findViewById(R.id.tv_mall_name)).setText(mall_name);
        //显示销量和评分
        ((TextView) findViewById(R.id.tv_sales_num_and_dsr))
                .setText("目前销量：" + portItem.getSales_num() + " | 评分：" + portItem.getDsr());
        //显示介绍
        ((TextView) findViewById(R.id.tv_introduce)).setText(portItem.getIntroduce());
        //显示券详情信息
        ((TextView) findViewById(R.id.tv_quan))
                .setText(mall_name + portItem.getOrg_Price() + "元在售，袋友们可领取" + portItem.getQuan_price()
                        + "元优惠券（剩余数量" + portItem.getQuan_surplus() + "/"
                        + (portItem.getQuan_surplus() + portItem.getQuan_receive()) + "）"
                        + "，实付" + price + "元包邮到手，价格很不错，喜欢的袋友速速入手了！（有效期："
                        + portItem.getQuan_time() + "，使用条件：" + portItem.getQuan_condition() + "）");
    }

//    private void initCyan() {
//        Config config = new Config();
//        config.ui.toolbar_bg = Color.WHITE;
//        config.ui.toolbar_border = Color.RED;
//        config.ui.toolbar_btn = Color.RED;
//        config.ui.list_title = Color.GRAY;
//        config.ui.before_clk = Color.GRAY;
//        config.ui.after_clk = Color.RED;
//        //config.ui.edit_cmt_bg
//        config.ui.style = "indent";
//        config.ui.depth = 1;
//        config.ui.sub_size = 20;
//
//        config.comment.showScore = true;
//        config.comment.uploadFiles = false;
//        config.comment.anonymous_token = "lRTU3LghBcOtwGzEapYEsDtX8JgkPNPWgoqJvpvetw0";
//        config.comment.useFace = true;
//
//        config.login.SSO_Assets_ICon = "ico31.png";
//        config.login.SSOLogin = true;
//        config.login.loginActivityClass = LoginActivity.class;
//
//        try {
//            CyanSdk.register(this, "cysurKntY", "7f5537ac5a67f85093dc0270da4659d8",
//                    "http://10.2.58.251:8081/login-success.html", config);
//        } catch (CyanException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        // 启用消息推送
//        //PushManager.setUseService(getActivity(), true);
//        // 设置通知图标
//        //PushManager.setNotificationIcon(getActivity(), R.drawable.ic_launcher);
//        cyanSdk = CyanSdk.getInstance(this);
//    }


    /**
     * 点击获取券
     */
    private void getQuan() {
        aliTradeUtils.showItemURLPage(portItem.getQuan_link());
    }

    /**
     * 点击购买
     */
    private void buyItem() {
        if (portItem.getAli_click() != null) {
            if (portItem.getAli_click().length() > 33) {
                aliTradeUtils.showItemDetailPage(portItem.getGoodsID());
            } else {
                aliTradeUtils.showItemURLPage(portItem.getAli_click());
            }
        } else {
            if (portItem.isCommission_check()) {
                aliTradeUtils.showItemURLPage("http://www.neday.cn/index_.php?r=p/d&id=" + portItem.getID());
            } else {
                aliTradeUtils.showItemDetailPage(portItem.getGoodsID());
            }
        }

    }

    @Override
    protected void onDestroy() {
        AlibcTradeSDK.destory();
//        cyanSdk = null;
        super.onDestroy();
    }
}
