package com.cs.cs_base_module.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ConvertUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

object ImageLoader {
    /**
     * 是否是图片类型
     */
    fun isImageContentType(contentType:String) : Boolean {
        return Regex("^(image/)(jpeg|png|jpg|webp|tiff|bmp)$").matches(contentType)
                || Regex("^(.)+(jpeg|png|jpg|webp|tiff|bmp)$").matches(contentType)
    }

    fun isValidContext(context: Context): Boolean {
        if (context == null) {
            return false
        }
        when(context) {
            is Fragment -> {
                if(context.isDetached || context.isRemoving) {
                    return false
                }
            }
            is Activity -> {
                if(context.isDestroyed || context.isFinishing) {
                    return false
                }
            }
        }
        return true
    }

    @JvmOverloads
    fun loadImage(view:ImageView, url:String?, radius:Float = 5f, placeHolder:Int = 0, errorId:Int = 0) {
        if(url.isNullOrEmpty()) {
            return
        }
        //TODO:拼接url
        val newUrl = if(!url.startsWith("http://") && !url.startsWith("https://")) {  url } else url
        diplayImage(view, newUrl, radius, placeHolder, errorId)
    }

    @JvmOverloads
    fun diplayImage(view: ImageView, newUrl: String?, radius: Float = 5f, placeHolder: Int = 0, errorId: Int = 0) {
        val requestOptions: RequestOptions = if (radius > 0) {
            RequestOptions().transform(CenterCrop(), RoundedCorners(ConvertUtils.dp2px(radius)))
        } else {
            RequestOptions()
        }
        if (placeHolder != 0) {
            requestOptions.placeholder(placeHolder)
        }
        if (errorId != 0) {
            requestOptions.error(errorId)
        }
        var context:Context? = view.context
        context?.let {
            Glide.with(it)
                .load(newUrl)
                .apply(requestOptions)
                .into(view)
        }
    }

    @JvmOverloads
    fun diplayImage(view: ImageView, newUrl: Uri?, radius: Float = 5f, placeHolder: Int = 0, errorId: Int = 0) {
        val requestOptions: RequestOptions = if (radius > 0) {
            RequestOptions().transform(CenterCrop(), RoundedCorners(ConvertUtils.dp2px(radius)))
        } else {
            RequestOptions()
        }
        if (placeHolder != 0) {
            requestOptions.placeholder(placeHolder)
        }
        if (errorId != 0) {
            requestOptions.error(errorId)
        }
        var context:Context? = view.context
        context?.let {
            Glide.with(it)
                .load(newUrl)
                .apply(requestOptions)
                .into(view)
        }
    }
}