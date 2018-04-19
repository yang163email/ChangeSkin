package com.yan.skin.core

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.yan.skin.core.utils.SkinThemeUtils
import java.lang.reflect.Constructor
import java.util.*

/**
 * Created by Administrator on 2018/3/16 0016.
 */

class SkinLayoutFactory(private val activity: Activity, skinTypeface: Typeface) : LayoutInflater.Factory2, Observer {

    private val mClassPrefixList = arrayOf("android.widget.", "android.view.", "android.webkit.")
    private val mConstructorSignature = arrayOf(Context::class.java, AttributeSet::class.java)
    private val sConstructorMap = HashMap<String, Constructor<out View>>()

    // 属性处理类
    internal var skinAttribute: SkinAttribute

    init {
        skinAttribute = SkinAttribute(skinTypeface)
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        //反射 classloader
        var view = createViewFromTag(name, context, attrs)
        // 自定义View
        if (null == view) {
            view = createView(name, context, attrs)
        }
        //筛选符合属性的View
        skinAttribute.load(view!!, attrs)

        return view
    }

    private fun createViewFromTag(name: String, context: Context, attrs: AttributeSet): View? {
        //包含了 . 自定义控件
        if (-1 != name.indexOf(".")) {
            return null
        }
        var view: View? = null
        for (pre in mClassPrefixList) {
            view = createView(pre + name, context, attrs)
            if (null != view) {
                break
            }
        }
        return view
    }

    private fun createView(name: String, context: Context, attrs: AttributeSet): View? {
        var constructor: Constructor<out View>? = sConstructorMap[name]
        if (null == constructor) {
            try {
                val aClass = context.classLoader.loadClass(name).asSubclass(View::class.java)
                constructor = aClass.getConstructor(*mConstructorSignature)
                sConstructorMap[name] = constructor
            } catch (e: Exception) {
            }

        }
        if (null != constructor) {
            try {
                return constructor.newInstance(context, attrs)
            } catch (e: Exception) {
            }

        }
        return null
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }

    override fun update(o: Observable?, arg: Any?) {
        //状态栏、导航栏更新
        SkinThemeUtils.updateStatusBar(activity)
        val skinTypeface = SkinThemeUtils.getSkinTypeface(activity)
        // 更换皮肤
        skinAttribute.applySkin(skinTypeface)
    }

}
