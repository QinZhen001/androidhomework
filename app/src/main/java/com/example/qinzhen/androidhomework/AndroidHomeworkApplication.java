package com.example.qinzhen.androidhomework;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.squareup.leakcanary.LeakCanary;

/**
 * @anthor qinzhen
 * @time 2017/4/12 19:46
 */

public class AndroidHomeworkApplication extends Application {
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        if(applicationContext == null){
            applicationContext = this;
        }
        //内存泄漏检查
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
          LeakCanary.install(this);
    }

    /**
     * 获取应用的版本号
     * @return 应用版本号，默认返回1
     */
    public static int getAppVersionCode(){
        try {
            PackageInfo packageInfo = applicationContext.getPackageManager().
                    getPackageInfo(applicationContext.getPackageName(),0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 1;

    }



}
