package com.slh.skin;

import android.app.Application;

import com.slh.skin.lib.utils.SkinResources;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        SkinResources.init(this);

    }
}
