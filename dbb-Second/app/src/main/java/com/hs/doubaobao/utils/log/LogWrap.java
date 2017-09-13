package com.hs.doubaobao.utils.log;

import android.util.Log;

import com.hs.doubaobao.base.BaseParams;

/**
 * 日志打印包装类
 * 作者：zhanghaitao on 2017/8/16 17:44
 * 邮箱：820159571@qq.com
 */

public class LogWrap {

    //每次截取的长度
    private static int printLength = 2000;
    //打印类型
    private static int printD = 0;
    private static int printE = 1;
    private static int printI = 2;
    private static int printV = 3;
    private static int printW = 4;

    public static void d(String tag, String msg) {
        if (BaseParams.isDebug) {
            piecewisePrint(printD, tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (BaseParams.isDebug) {
            piecewisePrint(printE, tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (BaseParams.isDebug) {
            piecewisePrint(printI, tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (BaseParams.isDebug) {
            piecewisePrint(printV, tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (BaseParams.isDebug) {
            piecewisePrint(printW, tag, msg);
        }
    }

    /**
     * 分段打印
     *
     * @param type
     * @param tag
     * @param msg
     */
    private static void piecewisePrint(int type, String tag, String msg) {
        if (msg.length() > printLength) {
            for (int i = 0; i < msg.length(); i += printLength) {
                if (i + printLength < msg.length()) {
                    checkPrintType(type, tag + i + "-" + (i + printLength), msg.substring(i, i + printLength));
                } else {
                    checkPrintType(type, tag + i + "-" + msg.length(), msg.substring(i, msg.length()));
                }
            }
        } else {
            checkPrintType(type, tag, msg);
        }
    }

    /**
     * 打印类型
     *
     * @param tag
     * @param msg
     */
    private static void checkPrintType(int type, String tag, String msg) {
        switch (type) {
            case 0:
                Log.d(tag, msg);
                break;
            case 1:
                Log.e(tag, msg);
                break;
            case 2:
                Log.i(tag, msg);
                break;
            case 3:
                Log.v(tag, msg);
                break;
            case 4:
                Log.w(tag, msg);
                break;
            default:
                Log.d(tag, msg);
                break;
        }
    }
}
