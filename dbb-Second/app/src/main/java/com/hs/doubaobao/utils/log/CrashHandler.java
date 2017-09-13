package com.hs.doubaobao.utils.log;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.hs.doubaobao.MyApplication;
import com.hs.doubaobao.base.BaseParams;
import com.hs.doubaobao.utils.PermissionCheck;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description: 异常退出管理类
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG              = "CrashHandler";
    private static final boolean DEBUG            = true;
    private static final String PATH             = BaseParams.CRASH_PATH;
    private static final String FILE_NAME        = "crash-";
    // log文件的后缀名
    private static final String FILE_NAME_SUFFIX = ".trace";
    // 系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    // 构造方法私有，防止外部构造多个实例，即采用单例模式
    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return CrashHandlerInstance.instance;
    }

    private static class CrashHandlerInstance {
        static CrashHandler instance = new CrashHandler();
    }

    // 这里主要完成初始化工作
    public void init(Context context) {
        // 获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        // 获取Context，方便内部使用
        mContext = context.getApplicationContext();
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // 打印出当前调用栈信息
        ex.printStackTrace();

        // 如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
        if (!handleException(ex) && mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Logger.e(TAG, "error : ", e);
            }
            // 退出程序
           // ActivityUtils.onExit();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        // 导出异常信息到SD卡中
        try {
            dumpExceptionToSDCard(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug
        uploadExceptionToServer();
        return true;
    }

    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
        Context context = MyApplication.getContext();
        // 如果没有读写SD卡的权限，则不写入文件
        if (null != context && !PermissionCheck.getInstance().checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return;
        }

        // 如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Logger.w(TAG, "sdcard unmounted,skip dump exception");
                return;
            }
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long   current = System.currentTimeMillis();
        String time    = new SimpleDateFormat(DateFormatter.SS.getValue()).format(new Date(current));
        // 以当前时间创建log文件
        File file = new File(PATH + File.separator + FILE_NAME + time + FILE_NAME_SUFFIX);

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            // 导出发生异常的时间
            pw.println(time);

            // 导出手机信息
            dumpPhoneInfo(pw);
            collectDeviceInfo(pw);

            pw.println();
            // 导出异常的调用栈信息
            ex.printStackTrace(pw);

            pw.close();
        } catch (Exception e) {
            Logger.e(TAG, "dump crash info failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws NameNotFoundException {
        // 应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        // 应用版本
        pw.print("App Version Name: ");
        pw.println(pi.versionName);
        // 应用版本号
        pw.print("App Version Code: ");
        pw.println(pi.versionCode);

        // android版本
        pw.print("Android OS Version Name: ");
        pw.println(Build.VERSION.RELEASE);
        // android版本号
        pw.print("Android OS Version Code: ");
        pw.println(Build.VERSION.SDK_INT);

        // 手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        // 手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
    }

    /**
     * 收集设备参数信息
     */
    public void collectDeviceInfo(PrintWriter pw) {
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                pw.print(field.getName() + ": ");
                pw.println(field.get(null).toString());
                Logger.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Logger.e(TAG, "an error occurred when collect crash info", e);
            }
        }
    }

    /**
     * 上传log到服务端
     */
    private void uploadExceptionToServer() {
        // TODO Upload Exception Message To Your Web Server
    }
}
