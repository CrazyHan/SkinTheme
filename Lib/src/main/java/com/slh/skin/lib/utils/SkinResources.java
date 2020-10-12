package com.slh.skin.lib.utils;

import android.content.res.Resources;

public class SkinResources {


    boolean isUseDefault;

    String mSkinPath;

    Resources mAppResources;

    Resources mSkinResources;
    private String mPkgName;


    public SkinResources() {
    }

    private volatile static SkinResources instance;

    /**
     * 初始化
     */
    public static void init() {

    }

    public static SkinResources getInstance() {
        return null;
    }

    public void applySkin(Resources resources,String skinPath){
        mSkinPath = skinPath;

    }

    public int getIdentifier(int resId) {
        if (isUseDefault) {
            return resId;
        }

        String resName = mAppResources.getResourceEntryName(resId);
        String resType = mAppResources.getResourceTypeName(resId);

        int skinId = mSkinResources.getIdentifier(resName,resType,mPkgName);
        return skinId;

    }

}
