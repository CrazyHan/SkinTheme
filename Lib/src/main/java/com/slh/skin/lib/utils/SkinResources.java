package com.slh.skin.lib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SkinResources {

    private String TAG = SkinResources.class.getName();

    Context context;

    boolean isUseDefault = true;

    String mSkinPath;

    Resources mAppResources;

    Resources mSkinResources;
    private String mPkgName;

    public void setSkinResources(Resources mSkinResources) {
        this.mSkinResources = mSkinResources;
    }

    private SkinResources(Context context) {
        this.context = context;
        mAppResources = context.getResources();

    }

    private volatile static SkinResources instance;

    /**
     * 只初始化
     */
    public static void init(Context context) {
        if (instance == null) {
            synchronized (SkinResources.class) {
                if (instance == null) {
                    instance = new SkinResources(context);
                }
            }
        }
    }

    public void reset() {
        isUseDefault = true;
        mSkinPath = "";
        mPkgName = "";
        mSkinResources = null;
    }

    public static SkinResources getInstance() {
        return instance;
    }

    //设置了路径还要更新所有的 UI
    public void applySkin(String skinPath,String pkgName){

        this.mSkinPath = skinPath;
        isUseDefault = false;
        mPkgName = pkgName;

    }

    public int getIdentifier(int resId) {
        if (isUseDefault) {
            return resId;
        }

        String resName = mAppResources.getResourceEntryName(resId);
        String resType = mAppResources.getResourceTypeName(resId);

        Log.d(TAG, "getIdentifier: 类型="+resType+"名字="+resName);

        int skinId = mSkinResources.getIdentifier(resName,resType,mPkgName);
        return skinId;

    }

    public Drawable getDrawable(int resId) {
        if (isUseDefault) {
            return mAppResources.getDrawable(resId);
        }
        int drawableId = getIdentifier(resId);
        if (drawableId == 0) {
            return mAppResources.getDrawable(resId);
        }
        return mSkinResources.getDrawable(drawableId);
    }


    public Object getBackground(int resId) {
        if ("color".equals(mAppResources.getResourceTypeName(resId))) {
            return getColor(resId);
        }else{
            return getDrawable(resId);
        }
    }

    public ColorStateList getColorStateList(int resId) {

        if (isUseDefault) {
            return mAppResources.getColorStateList(resId);
        }
        int colorListId = getIdentifier(resId);
        if (colorListId == 0) {
            return mAppResources.getColorStateList(resId);
        }
        return mSkinResources.getColorStateList(colorListId);

    }

    public int getColor(int resId) {
        if (isUseDefault) {
            return mAppResources.getColor(resId);
        }
        int colorId = getIdentifier(resId);
        if (resId <= 0) {
            return mAppResources.getColor(resId);
        }
        return mSkinResources.getColor(colorId);
    }



}
