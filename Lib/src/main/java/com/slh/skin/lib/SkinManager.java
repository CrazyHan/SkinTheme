package com.slh.skin.lib;

import android.app.Application;
import android.content.Context;

/**
 * 提供给全局使用的管理类
 */
public class SkinManager {

    String mSkinPath;
    boolean mIsUseDefault;


    public static SkinManager instance;

    private SkinManager() {
    }

    public static SkinManager getInstance() {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager();
                }
            }
        }
        return instance;
    }


}
