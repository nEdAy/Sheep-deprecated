package com.neday.bomb.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neday.bomb.R;
import com.neday.bomb.base.BaseOnlineActivity;

import java.util.Stack;

/**
 * 口袋币商城网页
 *
 * @author nEdAy
 */
public class WebShopActivity extends BaseOnlineActivity {
    private static final String VERSION = "1.0.8";
    private static String ua;
    private static Stack<WebShopActivity> activityStack;
    private static boolean IS_WAKEUP_LOGIN = false;
    private String url;
    private Boolean ifRefresh = false;
    private WebView mWebView;
    private LinearLayout rl_webView, mErrorFrame;
    private CreditsListener creditsListener = new CreditsListener() {
        /**
         * 当点击登录
         *
         * @param webView    用于登录成功后返回到当前的webview并刷新。
         * @param currentUrl 当前页面的url
         */
        public void onLoginClick(WebView webView, String currentUrl) {
            //当未登录的用户点击去登录时，会调用此处代码。
            //为了用户登录后能回到之前未登录前的页面。
            //当用户登录成功后，需要重新动态生成一次自动登录url，需包含redirect参数，将currentUrl放入redirect参数。
            new AlertDialog.Builder(webView.getContext())
                    .setTitle("跳转登录")
                    .setMessage("跳转到登录页面？")
                    .setPositiveButton("是", (dialog, which) ->
                            getOperation().startActivity(LoginActivity.class))
                    .setNegativeButton("否", null)
                    .show();
        }

        /**
         * 当点击“复制”按钮时，触发该方法，回调获取到券码code
         *
         * @param webView webview对象。
         * @param code    复制的券码
         */
        public void onCopyCode(WebView webView, String code) {
            new AlertDialog.Builder(webView.getContext())
                    .setTitle("复制券码")
                    .setMessage("已复制，券码为：" + code)
                    .setPositiveButton("是", null)
                    .setNegativeButton("否", null)
                    .show();
        }

        /**
         * 口袋币商城返回首页刷新口袋币时，触发该方法。
         */
        public void onLocalRefresh(WebView mWebView, String credits) {
            //String credits为口袋币商城返回的最新口袋币，不保证准确。
            //触发更新本地口袋币，这里建议用ajax向自己服务器请求口袋币值，比较准确
//            UserProxy userProxy = new UserProxy();
//            userProxy.getUserCredit();
//                activity.getOperation().showToast("触发本地刷新口袋币：" + credits);
        }
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_webview;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView(Bundle savedInstanceState) {
        setTintManager();
        rl_webView = (LinearLayout) findViewById(R.id.rl_webView);
        mErrorFrame = (LinearLayout) findViewById(R.id.errNetLayout);
        TextView network_load = (TextView) findViewById(R.id.network_load);
        network_load.setOnClickListener(v -> refresh());
        mWebView = new WebView(getApplicationContext());
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        mWebView.removeJavascriptInterface("accessibility");
        mWebView.removeJavascriptInterface("accessibilityTraversal");
        rl_webView.addView(mWebView);
        url = getIntent().getStringExtra("url");
        if (url == null) {
            url = "http://www.neday.cn/";
        }
        // 管理匿名类栈，用于模拟原生应用的页面跳转。
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.push(this);
        // api11以上的系统隐藏系统默认的ActionBar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
        // 初始化WebView配置
        initWebView();
    }

    @Override
    public void onResumeAfter() {

    }

    //初始化WebView配置
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        // User settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
        settings.setJavaScriptEnabled(true);    //设置webview支持javascript
        settings.setLoadsImagesAutomatically(true);    //支持自动加载图片
        settings.setUseWideViewPort(true);    //设置webview推荐使用的窗口，使html界面自适应屏幕
        settings.setLoadWithOverviewMode(true);
        settings.setSaveFormData(true);    //设置webview保存表单数据
        settings.setSavePassword(false);    //设置webview不保存密码
        settings.setDefaultZoom(ZoomDensity.MEDIUM);    //设置中等像素密度，medium=160dpi
        settings.setSupportZoom(true);    //支持缩放
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT > 8) {
            settings.setPluginState(PluginState.ON_DEMAND);
        }
        // Technical settings
        settings.setSupportMultipleWindows(true);
        mWebView.setLongClickable(true);
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setDrawingCacheEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);

        settings.setAllowFileAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        //js调java代码接口。

        mWebView.addJavascriptInterface(new Object() {
            //用于跳转用户登录页面事件。
            @JavascriptInterface
            public void login() {
                if (creditsListener != null) {
                    mWebView.post(() -> creditsListener.onLoginClick(mWebView, mWebView.getUrl()));
                }
            }

            //复制券码
            @JavascriptInterface
            public void copyCode(final String code) {
                if (creditsListener != null) {
                    mWebView.post(() -> creditsListener.onCopyCode(mWebView, code));
                }
            }

            //客户端本地触发刷新口袋币。
            @JavascriptInterface
            public void localRefresh(final String credits) {
                if (creditsListener != null) {
                    mWebView.post(() -> creditsListener.onLocalRefresh(mWebView, credits));
                }
            }
        }, "duiba_app");
        if (ua == null) {
            ua = mWebView.getSettings().getUserAgentString() + " Duiba/" + VERSION;
        }
        mWebView.getSettings().setUserAgentString(ua);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return shouldOverrideUrlByDuiba(view, url);
            }

            @Override
            // 转向错误时的处理
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mErrorFrame.setVisibility(View.VISIBLE);
            }
        });
        mWebView.loadUrl(url);
    }

    private void onBackClick() {
        Intent intent = new Intent();
        setResult(99, intent);
        finishActivity(this);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        initTopBarForBoth(title.toString(), getString(R.string.tx_back), this::onBackClick, "口袋币明细", () ->
                {
                    if (currentUser != null) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, CreditsHistoryActivity.class);
                        intent.putExtra("userId", currentUser.getObjectId());
                        startActivity(intent);
                    }
                }
        );
    }

    /**
     * 拦截url请求，根据url结尾执行相应的动作。 （重要）
     * 用途：模仿原生应用体验，管理页面历史栈。
     */
    private boolean shouldOverrideUrlByDuiba(WebView view, String url) {
        Uri uri = Uri.parse(url);
        if (this.url.equals(url)) {
            view.loadUrl(url);
            return true;
        }
        // 处理电话链接，启动本地通话应用。
        if (url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent);
            return true;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return false;
        }
        // 截获页面唤起登录请求。（目前暂时还是用js回调的方式，这里仅作预留。）
        if ("/client/dblogin".equals(uri.getPath())) {
            if (creditsListener != null) {
                mWebView.post(() -> creditsListener.onLoginClick(mWebView, mWebView.getUrl()));
            }
            return true;
        }
        int requestCode = 100;
        if (url.contains("dbnewopen")) { // 新开页面
            Intent intent = new Intent();
            intent.setClass(WebShopActivity.this, WebShopActivity.this.getClass());
            url = url.replace("dbnewopen", "none");
            intent.putExtra("url", url);
            startActivityForResult(intent, requestCode);
        } else if (url.contains("dbbackrefresh")) { // 后退并刷新
            url = url.replace("dbbackrefresh", "none");
            Intent intent = new Intent();
            intent.putExtra("url", url);
            setResult(requestCode, intent);
            finishActivity(this);
        } else if (url.contains("dbbackrootrefresh")) { // 回到口袋币商城首页并刷新
            url = url.replace("dbbackrootrefresh", "none");
            if (activityStack.size() == 1) {
                finishActivity(this);
            } else {
                activityStack.get(0).ifRefresh = true;
                finishUpActivity();
            }
        } else if (url.contains("dbbackroot")) { // 回到口袋币商城首页
            url = url.replace("dbbackroot", "none");
            if (activityStack.size() == 1) {
                finishActivity(this);
            } else {
                finishUpActivity();
            }
        } else if (url.contains("dbback")) { // 后退
            url = url.replace("dbback", "none");
            finishActivity(this);
        } else {
            if (url.endsWith(".apk") || url.contains(".apk?")) { // 支持应用链接下载
                Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(viewIntent);
                return true;
            }
            view.loadUrl(url);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == 100) {
            if (intent.getStringExtra("url") != null) {
                this.url = intent.getStringExtra("url");
                mWebView.loadUrl(this.url);
                ifRefresh = false;
            }
        }
    }

    private void refresh() {
        mErrorFrame.setVisibility(View.GONE);
        mWebView.reload();
    }

    @Override
    public void onResume() {
        super.onResume();
        String INDEX_URI = "/chome/index";
        if (ifRefresh) {
            this.url = getIntent().getStringExtra("url");
            mWebView.loadUrl(this.url);
            ifRefresh = false;
            //如果首页含有登录的入口，返回时需要同时刷新首页的话，
            // 需要把下面判断语句中的 && this.url.indexOf(INDEX_URI) > 0 去掉。
        } else if (IS_WAKEUP_LOGIN && this.url.indexOf(INDEX_URI) > 0) {
            refresh();
            IS_WAKEUP_LOGIN = false;
        } else {
            // 返回页面时，如果页面含有onDBNewOpenBack()方法,则调用该js方法。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.evaluateJavascript("if(window.onDBNewOpenBack){onDBNewOpenBack()}", value -> {
                });
            } else {
                mWebView.loadUrl("javascript:if(window.onDBNewOpenBack){onDBNewOpenBack()}");
            }
        }
    }

    //--------------------------------------------以下为工具方法----------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            creditsListener = null;
            mWebView.removeAllViews();
            rl_webView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackClick();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 结束除了最底部一个以外的所有Activity
     */
    private void finishUpActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size - 1; i++) {
            activityStack.pop().finish();
        }
    }

    /**
     * 结束指定的Activity
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    private void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    public interface CreditsListener {
        /**
         * 当点击登录
         *
         * @param webView    用于登录成功后返回到当前的webView并刷新。
         * @param currentUrl 当前页面的url
         */
        void onLoginClick(WebView webView, String currentUrl);

        /**
         * 当点复制券码时
         *
         * @param mWebView mWebView对象。
         * @param code     复制的券码
         */
        void onCopyCode(WebView mWebView, String code);

        /**
         * 通知本地，刷新口袋币
         *
         * @param mWebView mWebView对象
         * @param credits  口袋币
         */
        void onLocalRefresh(WebView mWebView, String credits);
    }
}
