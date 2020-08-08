package com.cs.cs_base_module.utils;

import android.app.Application;

public class AppManager {

    private static Application sApplication;
    private static AppManager instance;
    private static boolean isInit = false;

    private AppManager(Application context) {
        if (context == null) {
            throw new UnsupportedOperationException("u can't instantiate me...");
        }
        sApplication = context;
    }

    /**
     * 单例模式 在application里面初始化
     *
     * @return AppManager
     */
    public static AppManager init(Application context) {
        if (instance == null) {
            instance = new AppManager(context);
        }
        //flag 表示已经初始化
        isInit = true;
        return instance;
    }

    public static Application getsApplication() {
        if (sApplication == null) {
            throw new IllegalArgumentException("Application is null");
        }
        return sApplication;
    }

    public static AppManager getInstance() {
        if (!isInit) {
            throw new IllegalArgumentException("AppManage class not init");
        }
        return instance;
    }

}
