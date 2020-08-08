package com.cs.cs_base_module.mvvm

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager

interface IHost {
    /**
     * 初始化界面传递参数
     */
    fun initParams()

    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 初始化UI观察者监听
     */
    fun initViewObservable()

    fun startActivity(cls: Class<*>)

    fun startActivity(cls: Class<*>, bundle: Bundle?)

    fun startActivityForResult(cls: Class<*>, bundle: Bundle?, requestCode: Int)

    /**
     * 获取Context
     *
     * @return Context
     */
    fun getContextWithinHost(): Context

    /**
     * 获取Activity
     *
     * @return BaseActivity
     */
    fun getActivityWithinHost(): BaseActivity<*,*>

    /**
     * 获取FragmentManager
     *
     * @return FragmentManager
     */
    fun getFragmentManagerWithinHost(): FragmentManager

    /**
     * 获取Resources
     *
     * @return Resources
     */
    fun getResourcesWithinHost(): Resources

    /**
     * 获取String
     *
     * @param resId int
     * @return String
     */
    fun getStringWithinHost(@StringRes resId: Int): String

    fun isAlive(): Boolean
}