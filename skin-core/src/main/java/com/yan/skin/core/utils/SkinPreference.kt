package com.yan.skin.core.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * @author Lance
 * @date 2018/3/8
 */
class SkinPreference private constructor(context: Context) {
    private val mPref: SharedPreferences

    var skin: String?
        get() = mPref.getString(KEY_SKIN_PATH, null)
        set(skinPath) = mPref.edit().putString(KEY_SKIN_PATH, skinPath).apply()

    init {
        mPref = context.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE)
    }

    companion object {
        private const val SKIN_SHARED = "skins"
        private const val KEY_SKIN_PATH = "skin-path"

        var instance: SkinPreference? = null
            private set

        fun init(context: Context) {
            if (instance == null) {
                synchronized(SkinPreference::class.java) {
                    if (instance == null) {
                        instance = SkinPreference(context.applicationContext)
                    }
                }
            }
        }
    }

}
