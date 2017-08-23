package com.neday.bomb.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.neday.bomb.CustomApplication;
import com.orhanobut.logger.Logger;

/**
 * 运行环境信息管理工具
 *
 * @author nEdAy
 */
public final class SysEnvUtils {
    /**
     * 上下文
     **/
    private static final Context mContext = CustomApplication.getInstance();

    /**
     * 获取应用程序版本（versionName）
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            Logger.e("获取应用程序版本失败，原因：" + e.getMessage());
            return "";
        }
    }
}
