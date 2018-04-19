package com.yan.skin.core

import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import com.yan.skin.core.utils.SkinPreference
import com.yan.skin.core.utils.SkinResources
import java.util.*

/**
 *  @author      : yan
 *  @date        : 2018/4/13 17:54
 *  @description : 换肤管理器，供外部调用
 */
class SkinManager private constructor(private val application: Application) : Observable() {

    private val skinActivityLifecycle: SkinActivityLifecycle

    init {
        SkinPreference.init(application)
        SkinResources.init(application)
        skinActivityLifecycle = SkinActivityLifecycle()
        //注册Activity声明周期回调
        application.registerActivityLifecycleCallbacks(skinActivityLifecycle)
        //当皮肤包放在外部存储时，不能调用，因为没有sdcard权限。
        //使用应用存储时，可以调用
        //使用策略：1. 当放入应用存储中，需要调用
        //         2. 放入sdcard中，不要在Application中加载，在通过权限后加载
//        loadSkin(SkinPreference.instance!!.skin)
    }

    /**
     * 加载皮肤包并更新
     */
    fun loadSkin(path: String?) {
        //还原默认皮肤包
        if (path.isNullOrEmpty()) {
            SkinPreference.instance!!.skin = ""
            SkinResources.getInstance().reset()
        } else {
            val assetManager = AssetManager::class.java.newInstance()
            //添加资源到资源管理器
            val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
            addAssetPath.isAccessible = true
            addAssetPath.invoke(assetManager, path)

            val resources = application.resources
            //横竖、语言
            val skinResources = Resources(assetManager, resources.displayMetrics, resources.configuration)

            //获取外部apk（皮肤包）包名
            val packageManager = application.packageManager
            val info = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
            val packageName = info.packageName
            SkinResources.getInstance().applySkin(skinResources, packageName)
            //保存当前使用的皮肤包
            SkinPreference.instance!!.skin = path
        }
        //应用皮肤包
        setChanged()
        //通知观察者
        notifyObservers()
    }

    /**
     * 对于一些自定义view，有些无法做到更新完全，则采用手动在onCreate中，该自定义view设置完成后，
     * 调用此方法的方式进行更新
     */
    fun updateSkin(activity: Activity) {
        skinActivityLifecycle.updateSkin(activity)
    }

    companion object {

        @JvmStatic
        var instance: SkinManager? = null
            private set

        @JvmStatic
        fun init(application: Application) {
            synchronized(SkinManager::class) {
                if (instance == null) {
                    instance = SkinManager(application)
                }
            }
        }

    }

}