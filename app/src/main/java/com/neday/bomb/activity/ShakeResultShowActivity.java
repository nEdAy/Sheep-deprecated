package com.neday.bomb.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.neday.bomb.R;
import com.neday.bomb.base.BaseActivity;

/**
 * 天天摇结果页
 *
 * @author nEdAy
 */
public class ShakeResultShowActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_shake_result_show;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Integer change = getIntent().getIntExtra("change", 0);
        ImageView iv_shake = (ImageView) findViewById(R.id.iv_shake);
        TextView tv_shake = (TextView) findViewById(R.id.tv_shake);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_value = (TextView) findViewById(R.id.tv_value);
        ImageView iv_close = (ImageView) findViewById(R.id.iv_close);
        if (change > 0) {
            iv_shake.setImageResource(R.drawable.shake_luck);
            tv_shake.setText("中奖");
            tv_title.setText("中奖详情");
            tv_value.setText("恭喜摇到" + change + "个口袋币，完胜" + (change + 4) + "%的口袋用户！再接再厉哦！");
        } else {
            iv_shake.setImageResource(R.drawable.shake_unluck);
            tv_shake.setText("换个姿势，再来一次");
            tv_title.setText("没有中奖");
            tv_value.setText("不增不减，再来一次");
        }
        iv_close.setOnClickListener(v -> finish());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
