package com.neday.bomb.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.neday.bomb.R;

/**
 * 自定义头部布局
 *
 * @author nEdAy
 */
public class HeaderLayout extends RelativeLayout {
    private RelativeLayout mHeader;
    private RelativeLayout mLayoutLeftContainer;
    private TextSwitcher mHtvSubTitle;
    private TextView mLeftTextButton, mRightTextButton;
    private ImageView mLeftImageButton;
    private onLeftButtonClickListener mLeftButtonClickListener;
    private onRightButtonClickListener mRightButtonClickListener;

    public HeaderLayout(Context context) {
        super(context);
        init(context);
    }

    public HeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressLint("InflateParams")
    private void init(Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeader = (RelativeLayout) mInflater.inflate(R.layout.include_top_title_bar, null);
        addView(mHeader);
        initViews(context);
    }

    private void initViews(Context context) {
        mLayoutLeftContainer = (RelativeLayout) findViewByHeaderId(R.id.title_left);
        mHtvSubTitle = (TextSwitcher) findViewByHeaderId(R.id.title_text);
        mLeftTextButton = (TextView) findViewByHeaderId(R.id.left_text_btn);
        mRightTextButton = (TextView) findViewByHeaderId(R.id.right_text_btn);
        mLeftImageButton = (ImageView) findViewById(R.id.left_image_btn);
        mHtvSubTitle.setFactory(() -> {
            TextView textView = new TextView(context);
            textView.setTextAppearance(context, R.style.Title_Text);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setSelected(true);
//            textView.postDelayed(() -> textView.setSelected(true), 1738);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            textView.setLayoutParams(lp);
            return textView;
        });
    }

    private View findViewByHeaderId(int id) {
        return mHeader.findViewById(id);
    }

    public void init(HeaderStyle hStyle) {
        switch (hStyle) {
            case DEFAULT_TITLE:
                defaultTitle();
                break;
            case TITLE_LIFT_IMAGE_BUTTON:
                defaultTitle();
                titleLeftImageButton();
                break;
            case TITLE_DOUBLE_IMAGE_BUTTON:
                defaultTitle();
                titleLeftImageButton();
                titleRightTextButton();
                break;
        }
    }

    // 默认文字标题
    private void defaultTitle() {
        mHtvSubTitle.setVisibility(VISIBLE);
    }

    // 左侧自定义按钮
    private void titleLeftImageButton() {
        mLayoutLeftContainer.setOnClickListener(arg0 -> {
            if (mLeftButtonClickListener != null) {
                mLeftButtonClickListener.onClick();
            }
        });
    }

    // 右侧自定义按钮
    private void titleRightTextButton() {
        mRightTextButton.setOnClickListener(arg0 -> {
            if (mRightButtonClickListener != null) {
                mRightButtonClickListener.onClick();
            }
        });
    }


    // title
    public void setDefaultTitle(CharSequence title) {
        if (title != null) {
            mHtvSubTitle.setText(title);
        }
    }

    // back+title
    public void setTitleAndLeftImageButton(CharSequence title, int id, String leftText,
                                           onLeftButtonClickListener onLeftButtonClickListener) {
        setDefaultTitle(title);
        if (mLeftImageButton != null && id > 0) {
            mLeftImageButton.setImageResource(id);
        }
        if (mLeftTextButton != null && !leftText.isEmpty()) {
            mLeftTextButton.setText(leftText);
        }
        setOnLeftButtonClickListener(onLeftButtonClickListener);
        mLayoutLeftContainer.setVisibility(View.VISIBLE);
    }

    // back+title+右文字
    public void setTitleAndRightTextButton(CharSequence title, String text,
                                           onRightButtonClickListener onRightButtonClickListener) {
        setDefaultTitle(title);
        mRightTextButton.setVisibility(View.VISIBLE);
        if (mRightTextButton != null && !text.isEmpty()) {
            mRightTextButton.setText(text);
            setOnRightButtonClickListener(onRightButtonClickListener);
        }
    }


    private void setOnLeftButtonClickListener(onLeftButtonClickListener listener) {
        mLeftButtonClickListener = listener;
    }

    private void setOnRightButtonClickListener(onRightButtonClickListener listener) {
        mRightButtonClickListener = listener;
    }

    public enum HeaderStyle {// 头部整体样式
        DEFAULT_TITLE, TITLE_LIFT_IMAGE_BUTTON, TITLE_DOUBLE_IMAGE_BUTTON
    }

    public interface onLeftButtonClickListener {
        void onClick();
    }

    public interface onRightButtonClickListener {
        void onClick();
    }


}
