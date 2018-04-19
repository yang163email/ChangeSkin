package com.yan.skin.core.utils

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import com.yan.skin.core.R

/**
 * Created by Administrator on 2018/3/16 0016.
 */
object SkinThemeUtils {

    private val APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = intArrayOf(android.support.v7.appcompat.R.attr.colorPrimaryDark)
    private val STATUSBAR_COLOR_ATTRS = intArrayOf(
            android.R.attr.statusBarColor,
            android.R.attr.navigationBarColor)
    private val TYPEFACE_ATTR = intArrayOf(R.attr.skinTypeface)

    @JvmStatic
    fun getResId(context: Context, attrs: IntArray): IntArray {
        val resIds = IntArray(attrs.size)
        val typedArray = context.obtainStyledAttributes(attrs)
        for (i in 0 until typedArray.length()) {
            resIds[i] = typedArray.getResourceId(i, 0)
        }
        typedArray.recycle()
        return resIds
    }

    /**
     * 更新状态栏
     */
    @JvmStatic
    fun updateStatusBar(activity: Activity) {
        //5.0以上才能修改
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        val resIds = getResId(activity, STATUSBAR_COLOR_ATTRS)
        //修改状态栏。 statusBarColor属性优先于colorPrimaryDark
        if (resIds[0] == 0) {
            //如果没有配置statusBarColor属性，则获得0
            val statusBarColorId = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0]
            if (statusBarColorId != 0) {
                activity.window.statusBarColor = SkinResources.getInstance().getColor(statusBarColorId)
            }
        } else {
            activity.window.statusBarColor = SkinResources.getInstance().getColor(resIds[0])
        }

        //修改导航栏
        if (resIds[1] != 0) {
            activity.window.navigationBarColor = SkinResources.getInstance().getColor(resIds[1])
        }
    }

    @JvmStatic
    fun getSkinTypeface(activity: Activity): Typeface {
        val skinTypefaceId = getResId(activity, TYPEFACE_ATTR)[0]
        return SkinResources.getInstance().getTypeface(skinTypefaceId)
    }
}
