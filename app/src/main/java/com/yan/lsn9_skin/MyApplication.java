package com.yan.lsn9_skin;

import android.app.Application;

import com.yan.skin.core.SkinManager;


/**
 * @author Lance
 * @date 2018/3/8
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
