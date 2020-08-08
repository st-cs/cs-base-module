package com.cs.cs_base_module.view

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.cs.cs_base_module.R

class LoadingDialog(context: Context, themeResId: Int = R.style.dialog_loading) : BaseDialog(context, themeResId) {

    override fun initView() {
        setContentLayout(R.layout.dialog_loading_layout)
    }

    fun setTvProgress(progressTip: String) {
        var tvText = findViewById<TextView>(R.id.tv_progress)
        tvText.text = progressTip
        tvText.visibility = if (TextUtils.isEmpty(progressTip)) View.GONE else View.VISIBLE
    }

    override fun show() {
        try {
            super.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun dismiss() {
        try {
            super.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}