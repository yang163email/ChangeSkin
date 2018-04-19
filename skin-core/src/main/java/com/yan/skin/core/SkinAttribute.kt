package com.yan.skin.core

import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.yan.skin.core.utils.SkinResources
import com.yan.skin.core.utils.SkinThemeUtils

/**
 *  @author      : yan
 *  @date        : 2018/4/13 19:33
 *  @description : todo
 */
class SkinAttribute(val skinTypeface: Typeface) {

    companion object {
        private val mAttributes = arrayListOf(
                "background", "src", "textColor", "drawableLeft",
                "drawableTop", "drawableRight", "drawableBottom", "skinTypeface")
    }

    private val mSkinViews = arrayListOf<SkinView>()

    fun load(view: View, attrs: AttributeSet) {
        val skinPairs = arrayListOf<SkinPair>()
        for (i in 0 until attrs.attributeCount) {
            //获取属性名
            val attributeName = attrs.getAttributeName(i)
            //是否符合需要筛选的属性名
            if (attributeName in mAttributes) {
                val attributeValue = attrs.getAttributeValue(i)
                //如果写死，pass
                if (attributeValue.startsWith("#")) continue
                //资源id
                val resId: Int
                if (attributeValue.startsWith("?")) {
                    //attr Id
                    val attrId = attributeValue.substring(1).toInt()
                    resId = SkinThemeUtils.getResId(view.context, intArrayOf(attrId))[0]
                } else {
                    //@134341
                    resId = attributeValue.substring(1).toInt()
                }
                if (resId != 0) {
                    //可以被替换的属性
                    val skinPair = SkinPair(attributeName, resId)
                    skinPairs.add(skinPair)
                }
            }
        }

        if (skinPairs.isNotEmpty() || view is TextView || view is SkinViewSupport) {
            //将view与之对应的可以动态替换的属性集合，放入集合中
            val skinView = SkinView(view, skinPairs)
            skinView.applySkin(skinTypeface)
            mSkinViews.add(skinView)
        }
    }

    fun applySkin(typeface: Typeface) {
        for (skinView in mSkinViews) skinView.applySkin(typeface)
    }

    private class SkinPair(val attributeName: String, val resId: Int)

    private class SkinView(val view: View, val skinPairs: List<SkinPair>) {

        fun applySkin(typeface: Typeface) {
            //先配置皮肤包字体
            applySkinTypeface(typeface)
            //配置自定义控件
            applySkinViewSupport()
            for (skinPair in skinPairs) {
                var left: Drawable? = null
                var top: Drawable? = null
                var right: Drawable? = null
                var bottom: Drawable? = null
                when (skinPair.attributeName) {
                    "background" -> {
                        val background = SkinResources.getInstance().getBackground(skinPair.resId)
                        if (background is Int) {
                            view.setBackgroundColor(background)
                        } else {
                            ViewCompat.setBackground(view, background as Drawable)
                        }
                    }
                    "src" -> {
                        val background = SkinResources.getInstance().getBackground(skinPair.resId)
                        if (background is Int)
                            (view as ImageView).setImageDrawable(ColorDrawable(background))
                        else
                            (view as ImageView).setImageDrawable(background as Drawable)
                    }
                    "textColor" -> (view as TextView).setTextColor(SkinResources.getInstance().getColorStateList(skinPair.resId))
                    "drawableLeft" -> left = SkinResources.getInstance().getDrawable(skinPair.resId)
                    "drawableTop" -> top = SkinResources.getInstance().getDrawable(skinPair.resId)
                    "drawableRight" -> right = SkinResources.getInstance().getDrawable(skinPair.resId)
                    "drawableBottom" -> bottom = SkinResources.getInstance().getDrawable(skinPair.resId)
                    "skinTypeface" -> {
                        val typeface1 = SkinResources.getInstance().getTypeface(skinPair.resId)
                        applySkinTypeface(typeface1)
                    }
                }
                if (left != null || top != null || right != null || bottom != null) {
                    (view as TextView).setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
                }
            }
        }

        private fun applySkinViewSupport() {
            if (view is SkinViewSupport) {
                view.applySkin()
            }
        }

        private fun applySkinTypeface(typeface: Typeface) {
            if (view is TextView) {
                view.typeface = typeface
            }
        }

    }
}