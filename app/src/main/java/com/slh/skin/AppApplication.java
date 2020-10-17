package com.slh.skin;

import android.app.Application;

import com.slh.skin.lib.SkinManager;
import com.slh.skin.lib.utils.SkinResources;
import com.tencent.mmkv.MMKV;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        MMKV.initialize(this);
        SkinManager.init(this);

    }
}
