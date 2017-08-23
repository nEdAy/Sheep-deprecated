package com.neday.bomb.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.AlibcTaokeParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.model.ResultType;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.alibaba.baichuan.android.trade.page.AlibcAddCartPage;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.neday.bomb.StaticConfig;
import com.orhanobut.logger.Logger;

/**
 * AliTradeHelper
 * 阿里接口方法
 *
 * @author nEdAy
 */
public final class AliTradeHelper {
    private static final AlibcShowParams alibcShowParams = new AlibcShowParams(OpenType.Auto, false);//页面打开方式，默认，H5，Native
    private static final AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams(StaticConfig.DEFAULT_TAOKE_PID, "", "");
    private final Activity activity;
    private AlibcBasePage alibcBasePage;//页面类型必填，不可为null

    /**
     * 构造阿里服务基础参数
     *
     * @param activity 传入activity
     */
    public AliTradeHelper(Activity activity) {
        this.activity = activity;
    }


    /**
     * 根据商品ID打开商品详情页
     *
     * @param itemId 商品ID
     */
    public void showItemDetailPage(String itemId) {
        //实例化商品详情page
        alibcBasePage = new AlibcDetailPage(itemId);
        //使用百川sdk提供默认的Activity打开detail
        AlibcTrade.show(activity, alibcBasePage, alibcShowParams, alibcTaokeParams, null, new AliTradeCallback());
    }

    /**
     * 根据url打开阿里自带的webView
     *
     * @param itemUrl 需要打开的Url
     */
    public void showItemURLPage(String itemUrl) {
        //实例化URL打开page
        alibcBasePage = new AlibcPage(itemUrl);
        //使用百川sdk提供默认的Activity打开detail
        AlibcTrade.show(activity, alibcBasePage, alibcShowParams, alibcTaokeParams, null, new AliTradeCallback());
    }

    /**
     * 打开用户购物车
     */
    public void showCart() {
        CommonUtils.showToast("升级中...暂时关闭此功能");
//        alibcBasePage = new AlibcMyCartsPage();
//        AlibcTrade.show(activity, alibcBasePage, alibcShowParams, alibcTaokeParams, null, new AliTradeCallback());
    }

    /**
     * 打开购物订单页 参数表示的是待发货、已付款等
     *
     * @param orderType 打开订单的类型
     */
    public void showOrder(int orderType) {
        CommonUtils.showToast("升级中...暂时关闭此功能");
//        alibcBasePage = new AlibcMyOrdersPage(orderType, true);
//        AlibcTrade.show(activity, alibcBasePage, alibcShowParams, alibcTaokeParams, null, new AliTradeCallback());
    }

    /**
     * 添加指定商品ID的商品到购物车
     *
     * @param itemId 商品ID
     */
    public void addToCart(String itemId) {
        alibcBasePage = new AlibcAddCartPage(itemId);
        AlibcTrade.show(activity, alibcBasePage, alibcShowParams, alibcTaokeParams, null, new AliTradeCallback());
    }

    /**
     * 淘宝服务接口方法的回调
     */
    private final class AliTradeCallback implements AlibcTradeCallback {
        @Override
        public void onTradeSuccess(TradeResult tradeResult) {
            if (tradeResult.resultType == ResultType.TYPECART) {
                CommonUtils.showToast("加购成功");
            } else if (tradeResult.resultType == ResultType.TYPEPAY) {
                String orderId = tradeResult.payResult.paySuccessOrders.toString();
                //复制数据到剪切板
                ClipboardManager mClipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData myClip;
                myClip = ClipData.newPlainText("text", orderId);
                mClipboardManager.setPrimaryClip(myClip);
                CommonUtils.showToast("支付成功,已复制订单号" + orderId + "到剪切板");
            }
            //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
            Logger.e("显示商品详情页成功 " + tradeResult.toString());
        }

        @Override
        public void onFailure(int code, String msg) {
            //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
            CommonUtils.showToast("流程出错, 错误码 = " + code + ", 错误信息 = " + msg);
            Logger.e("显示商品详情页失败 " + code + msg);
        }

    }

}
