package com.neday.bomb.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.banner.widget.Banner.BaseIndicatorBanner;
import com.neday.bomb.R;

/**
 * 导航滚动翻页
 *
 * @author nEdAy
 */
public class GuideBanner extends BaseIndicatorBanner<Integer, GuideBanner> {
    private OnJumpClickL onJumpClickL;

    public GuideBanner(Context context) {
        this(context, null, 0);
    }

    public GuideBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBarShowWhenLast(false);
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.adapter_simple_guide, null);
        ImageView iv = (ImageView) inflate.findViewById(R.id.iv);
        TextView tv_jump = (TextView) inflate.findViewById(R.id.tv_jump);
        final Integer resId = mDatas.get(position);
        tv_jump.setVisibility(position == mDatas.size() - 1 ? VISIBLE : GONE);
        iv.setImageResource(resId);
        tv_jump.setOnClickListener(v -> {
            if (onJumpClickL != null)
                onJumpClickL.onJumpClick();
        });
        return inflate;
    }

    public void setOnJumpClickL(OnJumpClickL onJumpClickL) {
        this.onJumpClickL = onJumpClickL;
    }

    public interface OnJumpClickL {
        void onJumpClick();
    }
}
