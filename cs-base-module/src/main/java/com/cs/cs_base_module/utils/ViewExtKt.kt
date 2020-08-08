package com.cs.cs_base_module.utils

import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.marginTop


fun View.setTopMargin(i: Int) {
    if(this.layoutParams is ViewGroup){
        (this.layoutParams as ViewGroup).setTopMargin(i)
    }
}

fun View.setBottomMargin(i: Int) {
    if(this.layoutParams is ViewGroup){
        (this.layoutParams as ViewGroup).setBottomMargin(i)
    }
}