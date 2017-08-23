package com.neday.bomb.adapter;

import android.content.res.Resources;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.neday.bomb.R;
import com.neday.bomb.entity.CreditsHistory;

import java.util.List;

/**
 * CreditDetails 适配器
 *
 * @author nEdAy
 */
public class CreditsDetailsAdapter extends BaseQuickAdapter<CreditsHistory, BaseViewHolder> {

    public CreditsDetailsAdapter(List<CreditsHistory> items) {
        super(R.layout.adapter_credits_history_item, items);
    }

    @Override
    protected void convert(BaseViewHolder helper, CreditsHistory creditsHistory) {
        helper.setText(R.id.tv_creditsHistory_type, getType(creditsHistory.getType()))
                .setText(R.id.tv_creditsHistory_time, creditsHistory.getCreatedAt());
        TextView tv_creditsHistory_change = helper.getView(R.id.tv_creditsHistory_change);
        Integer change = creditsHistory.getChange();
        Resources resources = mContext.getResources();
        if (change > 0) {
            tv_creditsHistory_change.setTextColor(resources.getColor(R.color.green));
            tv_creditsHistory_change.setText("+" + change);
        } else {
            tv_creditsHistory_change.setTextColor(resources.getColor(R.color.red));
            tv_creditsHistory_change.setText("" + change);
        }
    }


    /**
     * 获取积分变更原因类型
     *
     * @param type 积分记录原因类型
     * @return 对应字符串
     */
    private String getType(Integer type) {
        switch (type) {
            case -2:
                return "订单提交奖励";
            case -1:
                return "天天摇";
            case 0:
                return "稳健型签到";
            case 1:
                return "任性型签到";
            case 2:
                return "冒险型签到";
            case 3:
                return "邀请奖励";
            case 4:
                return "注册奖励";
            case 5:
                return "兑换代取券";
            case 6:
                return "系统修正";
            case 7:
                return "支付宝";
            case 8:
                return "优惠券";
            case 9:
                return "实物";
            case 10:
                return "话费";
            case 11:
                return "流量";
            case 12:
                return "虚拟商品";
            case 13:
                return "大转盘";
            case 14:
                return "单品抽奖";
            case 15:
                return "活动抽奖";
            case 16:
                return "手动开奖";
            case 17:
                return "游戏";
            default:
                return "其他";
        }
    }


}
