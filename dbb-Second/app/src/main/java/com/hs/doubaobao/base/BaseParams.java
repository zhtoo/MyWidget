package com.hs.doubaobao.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.hs.doubaobao.MyApplication;

import java.io.File;

/**
 * 作者：zhanghaitao on 2017/9/12 16:46
 * 邮箱：820159571@qq.com
 *
 * @describe:
 */

public class BaseParams {


    public static boolean isDebug = true;

    /**
     * 根路径
     */
    public static final  String  ROOT_PATH                = getSDPath() + "/Ultron";
    /**
     * crash文件保存路径
     */
    public static final  String  CRASH_PATH               = ROOT_PATH + "/crashLog";











    /**
     * 获取SD卡的根目录
     */
    public static String getSDPath() {
        File sdDir = null;
        // 判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            // 获取跟目录
            sdDir = Environment.getExternalStorageDirectory();
        }
        if (sdDir == null) {
            return "";
        } else {
            return sdDir.toString();
        }
    }

    /**
     * 获取VersionCode
     */
    public static int getVersion() {
        try {
            Context context = MyApplication.getContext();
            PackageManager pm      = context.getPackageManager();//context为当前Activity上下文
            PackageInfo pi      = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 1;
        }
    }

    /**
     * 获取VersionName
     */
    public static String getVersionName() {
        try {
            Context        context = MyApplication.getContext();
            PackageManager pm      = context.getPackageManager();//context为当前Activity上下文
            PackageInfo    pi      = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "--";
        }
    }
}
