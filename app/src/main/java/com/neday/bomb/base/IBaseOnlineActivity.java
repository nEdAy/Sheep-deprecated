package com.neday.bomb.base;

/**
 * BaseOnlineActivity 接口
 *
 * @author nEdAy
 */
interface IBaseOnlineActivity {
    /**
     * 检测登录状态正常后Resume中继续进行的工作
     */
    void onResumeAfter();
}
