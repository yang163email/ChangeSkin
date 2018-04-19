package com.yan.skin.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.view.LayoutInflaterCompat
import android.view.LayoutInflater
import com.yan.skin.core.utils.SkinThemeUtils

/**
 *  @author      : yan
 *  @date        : 2018/4/13 18:13
 *  @description : todo
 */
class SkinActivityLifecycle : Application.ActivityLifecycleCallbacks {

    //容器添加观察者
    private val mLayoutFactoryMap = hashMapOf<Activity, SkinLayoutFactory>()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //状态栏换肤
        SkinThemeUtils.updateStatusBar(activity)
        //字体换肤
        val skinTypeface = SkinThemeUtils.getSkinTypeface(activity)

        val inflater = LayoutInflater.from(activity)
        //获取Activity的布局加载器
        //Android 布局加载器 使用 mFactorySet 标记是否设置过Factory
        val field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
        field.isAccessible = true
        //设置 mFactorySet 标签为false
        field.setBoolean(inflater, false)

        val layoutFactory = SkinLayoutFactory(activity, skinTypeface)
        LayoutInflaterCompat.setFactory2(inflater, layoutFactory)
        //注册观察者
        SkinManager.instance!!.addObserver(layoutFactory)
        mLayoutFactoryMap[activity] = layoutFactory
    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {

    }

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        //删除观察者
        val layoutFactory = mLayoutFactoryMap.remove(activity)
        SkinManager.instance!!.deleteObserver(layoutFactory)
    }

    fun updateSkin(activity: Activity) {
        val skinLayoutFactory = mLayoutFactoryMap[activity]
        skinLayoutFactory?.update(null, null)
    }
}