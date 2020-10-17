package com.slh.skin.lib;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.slh.skin.lib.utils.SkinResources;

import org.w3c.dom.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * 提供给全局使用的管理类
 * 1、初始化全集中在这里
 * 2、使用皮肤用这个
 * 3、设计成一个被观察者，这样就可以调用方法通知到所有的工厂类去更新UI
 */
public class SkinManager extends Observable {

    String mSkinPath;
    boolean mIsUseDefault;

    public static SkinManager instance;

    Application application;

    private SkinManager(Application application) {
        this.application = application;

        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksManager(this));

        SkinResources.init(application);
        mIsUseDefault = SkinPreference.isDefaultSkin();
        mSkinPath = SkinPreference.getSkinPath();
    }

    public static SkinManager getInstance() {
        return instance;
    }


    public static void init(Application application) {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager(application);
                }
            }
        }
    }

    public void loadSkin(String skinPath) {

        String pkgName = null;
        if (TextUtils.isEmpty(skinPath)) {
            SkinPreference.setDefaultSkin(true);
            SkinPreference.setSkinPath("");
            SkinResources.getInstance().reset();
        }else{
            Resources resources = application.getResources();
            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                Method method = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
                method.invoke(assetManager, skinPath);
                Resources skinResource = new Resources(assetManager, resources.getDisplayMetrics(),
                        resources.getConfiguration());
                PackageManager packageManager = application.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageArchiveInfo(skinPath, PackageManager
                        .GET_ACTIVITIES);
                pkgName = packageInfo.packageName;
                SkinResources.getInstance().setSkinResources(skinResource);
                SkinResources.getInstance().applySkin(skinPath,pkgName);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }


        setChanged();
        notifyObservers();
    }

}
