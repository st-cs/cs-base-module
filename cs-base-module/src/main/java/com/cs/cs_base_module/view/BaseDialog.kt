package com.cs.cs_base_module.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.cs.cs_base_module.R

abstract class BaseDialog : Dialog {
    protected var tag: String? = null

    private val rootView: View? = null

    constructor(context: Context) : this(context, R.style.dialog_loading)

    constructor(context: Context,themeResId: Int) : super(context, themeResId) {
        initView()
    }

    fun setContentLayout(layoutId: Int) {
        tag = javaClass.simpleName
        setContentView(layoutId)
    }

    protected fun setDialogAttributes() {
        val window = window
        val lp = window!!.attributes
        window.decorView.setPadding(0, 0, 0, 0)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp
    }

    protected fun showFromBottom() {
        val window = window
        window!!.setGravity(Gravity.BOTTOM)
    }

    open fun initView(){}

    open fun initialize() {}

    open fun onDestroyFragment() {}
}