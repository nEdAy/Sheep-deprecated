package com.neday.bomb.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.neday.bomb.CustomApplication;
import com.neday.bomb.R;
import com.neday.bomb.base.BaseActivity;
import com.neday.bomb.util.IMMLeaks;
import com.neday.bomb.view.ClearEditText;

/**
 * 分类&搜索&热门主题
 *
 * @author nEdAy
 */
public class SearchAndTypeActivity extends BaseActivity {
    private ClearEditText et_search;
    private TextView tv_search;
    //    private LinearLayout mLinearLayout;
//    private ScrollView sv_load;
    private final TextWatcher mSearchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                tv_search.setEnabled(true);
            }
        }
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_search_and_type;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTintManager();
        initTopBarForLeft("分类搜索", getString(R.string.tx_back));
//        sv_load = (ScrollView) findViewById(R.id.sv_load);
//        mLinearLayout = (LinearLayout) findViewById(R.id.my_gallery);
        et_search = (ClearEditText) findViewById(R.id.et_pass_search);
        tv_search = (TextView) findViewById(R.id.tv_search);
        et_search.addTextChangedListener(mSearchWatcher);
        tv_search.setOnClickListener(v -> searchItem());
        initMenu();
//        // 主动请求热门主题数据
//        QueryItem();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            IMMLeaks.fixFocusedViewLeak(CustomApplication.getInstance());
        }
    }

    /**
     * 点击搜索按钮，跳转并传关键字给搜索页面
     */
    private void searchItem() {
        String searchWhat = et_search.getText().toString().trim().replace(" ", "");
        if (!TextUtils.isEmpty(searchWhat)) {
            // 隐藏软键盘
            View _view = getWindow().peekDecorView();
            if (_view != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(_view.getWindowToken(), 0);
            }
            // 主动刷新数据
            Intent intent = new Intent();
            intent.setClass(mContext, SearchResultShowActivity.class);
            intent.putExtra("searchWhat", searchWhat);
            startActivity(intent);
        }
    }

    /**
     * 初始化类别按钮监听器
     */
    private void initMenu() {
        findViewById(R.id.ll_1).setOnClickListener(view -> openPortItemList(1));
        findViewById(R.id.ll_2).setOnClickListener(view -> openPortItemList(2));
        findViewById(R.id.ll_3).setOnClickListener(view -> openPortItemList(3));
        findViewById(R.id.ll_4).setOnClickListener(view -> openPortItemList(4));
        findViewById(R.id.ll_5).setOnClickListener(view -> openPortItemList(5));
        findViewById(R.id.ll_6).setOnClickListener(view -> openPortItemList(6));
        findViewById(R.id.ll_7).setOnClickListener(view -> openPortItemList(7));
        findViewById(R.id.ll_8).setOnClickListener(view -> openPortItemList(8));
    }

//    /**
//     * 请求热门主题数据
//     */
//    private void QueryItem() {
//        Map<String, Object> queryMap = new HashMap<>();
//        toSubscribe(RxFactory.getFavoriteServiceInstance(null)
//                        .queryFavoriteList(queryMap)
//                        .map(Favorite::getResult),
//                () ->
//                        queryMap.put("page", 0),
//                favorites -> {
//                    if (favorites.isEmpty()) {
//                        sv_load.setVisibility(View.GONE);
//                    } else {
//                        for (int i = 0; i < favorites.size(); i++) {
//                            sv_load.setVisibility(View.VISIBLE);
//                            mLinearLayout.addView(getView(favorites.get(i)));
//                        }
//                    }
//                },
//                throwable -> {
//                    sv_load.setVisibility(View.GONE);
//                    Logger.e(throwable.getMessage());
//                });
//    }
//
//    /**
//     * 构造热门主题显示View
//     *
//     * @param favorite 传入热门主题信息
//     * @return 获取构造出来的view
//     */
//    private View getView(Favorite favorite) {
//        LayoutInflater inflater = getLayoutInflater();// 创建inflater
//        View view = inflater.inflate(R.layout.adapter_favorite, null);
//        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
//        tv_title.setText(favorite.getFavorites_title());
//        tv_title.setTextSize(9L);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(525, 240);
//        layoutParams.setMargins(12, 0, 12, 0);
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        tv_title.setLayoutParams(layoutParams);
//        String img_url = favorite.getImage();
//        SimpleDraweeView iv_img = (SimpleDraweeView) view.findViewById(R.id.iv_img_shower);
//        iv_img.setLayoutParams(layoutParams);
//        Uri uri;
//        if (img_url != null && !img_url.equals("")) {
//            uri = Uri.parse(img_url);
//        } else {
//            uri = Uri.parse("http://com-xpple-sheep.image.alimmdn.com/TbkFavorite_Image/" + favorite.getFavorites_id() + ".png");
//        }
//        iv_img.setImageURI(uri);
//        view.setOnClickListener(view1 -> {
//            Intent item = new Intent(mContext,
//                    FavoriteItemListActivity.class);
//            item.putExtra(FavoriteItemListActivity.getExtra(), favorite);
//            startActivity(item);
//        });
//        return view;
//    }

    /**
     * 根据类目打开相关类目商品列表
     *
     * @param index 类目序数
     */
    private void openPortItemList(int index) {
        Intent item = new Intent(mContext,
                PortItemListActivity.class);
        item.putExtra(PortItemListActivity.getExtra(), index);
        startActivity(item);
    }
}
