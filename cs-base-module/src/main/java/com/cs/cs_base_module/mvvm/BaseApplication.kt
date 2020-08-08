package com.cs.cs_base_module.mvvm

import android.app.Application
import android.text.TextUtils
import com.cs.cs_base_module.utils.AppManager
import com.cs.cs_base_module.utils.PackageUtils

abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //对进程做判断，推送或者有其他的进程，也会重新走onCreate
        if (TextUtils.equals(
                PackageUtils.getCurProcessName(this),
                PackageUtils.getPackageName(this)
            )
        ) {
            //AppManager工具初始化
            AppManager.init(this)
            //Log初始化
//            Timber.plant(object : Timber.Tree() {
//                protected fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//                    LogHelper.log(priority, tag, message, t)
//                }
//            })

            init()
        }
    }

    /**
     * 所有的初始化都放在这里执行
     */
    abstract fun init()

}