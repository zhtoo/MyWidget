package com.hs.doubaobao;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *  全局单例
 * Created by zhanghaitao on 2017/5/18.
 */

public class MyApplication extends Application {

    private static Context mContext;
    private static Handler mMainThreadHandler;
    private static int mMainThreadId;
    private Map<String, String> mMemProtocolCacheMap = new HashMap<>();

    /**
     * 获取MEM协议缓存集合
     * @return
     */
    public Map<String, String> getMemProtocolCacheMap() {
        return mMemProtocolCacheMap;
    }

    /**
     * 得到上下文
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 得到主线程里面的创建的一个hanlder
     * @return
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 得到主线程的线程id
     * @return
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    @Override
    public void onCreate() {//程序的入口方法
        //上下文
        mContext = getApplicationContext();

        //主线程的Handler
        mMainThreadHandler = new Handler();

        //主线程的线程id
        mMainThreadId = android.os.Process.myTid();
        /**
         myTid:Thread
         myPid:Process
         myUid:User
         */
        super.onCreate();
    }

    /**
     * 存放Activity的对象
     */
    private static Map<String,Activity> destoryMap = new HashMap<>();

    /**
     * 添加到销毁队列
     *
     * @param activity 要销毁的activity
     */

    public static void addDestoryActivity(Activity activity,String activityName) {
        destoryMap.put(activityName,activity);
    }
    /**
     *销毁指定Activity
     */
    public static void destoryActivity(String activityName) {
        Set<String> keySet=destoryMap.keySet();
        for (String key:keySet){
            destoryMap.get(key).finish();
        }
    }

    public static void addActivity(Activity activity,String activityName) {
        destoryMap.put(activityName,activity);
    }

    public static Activity getActivity(String activityName) {
        Set<String> keySet=destoryMap.keySet();
        for (String key:keySet){
          return   destoryMap.get(key);
        }
        return null;
    }

}

