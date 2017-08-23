package com.neday.bomb.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.flyco.banner.widget.Banner.BaseIndicatorBanner;
import com.neday.bomb.R;
import com.neday.bomb.entity.Advertising;

/**
 * 广告图片轮播
 *
 * @author nEdAy
 */
public class AdImageBanner extends BaseIndicatorBanner<Advertising, AdImageBanner> {

    public AdImageBanner(Context context) {
        this(context, null, 0);
    }

    public AdImageBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.adapter_simple_image, null);
        SimpleDraweeView iv = (SimpleDraweeView) inflate.findViewById(R.id.iv);
        final Advertising item = mDatas.get(position);
        int itemWidth = mDisplayMetrics.widthPixels;
        int itemHeight = (int) (itemWidth * 0.417f);
        iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));
        iv.setImageURI(Uri.parse(item.getPic()));
        return inflate;
    }

}
