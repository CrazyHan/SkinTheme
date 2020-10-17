package com.slh.skin.lib;

import android.app.Activity;
import android.app.Application;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;

import com.slh.skin.lib.utils.SkinThemeUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Observable;

/**
 * 在Android P之后mFactorySet已经不能使用反射，根据LayoutInflaterCompat里面直接反射mFactory2就行了
 */
public class ActivityLifecycleCallbacksManager implements Application.ActivityLifecycleCallbacks {

    HashMap<Activity, SkinFactory> skinFactories = new HashMap<>();

    private static final String TAG = ActivityLifecycleCallbacksManager.class.getName();

    Observable observable;

    /**
     * 我们把{@link SkinManager}当做管理类
     * @param observable
     */
    public ActivityLifecycleCallbacksManager(Observable observable) {
        this.observable = observable;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        SkinThemeUtils.updateStatusBarColor(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        SkinFactory factory2 = new SkinFactory(activity);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {//28及以前的版本
            try {
                Field mFactorySet = LayoutInflater.class.getDeclaredField("mFactorySet");
                mFactorySet.setAccessible(true);
                mFactorySet.setBoolean(layoutInflater,true);
                layoutInflater.setFactory2(factory2);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }else{
            try {
                Field mFactory2 = LayoutInflater.class.getDeclaredField("mFactory2");
                Field[] fields = LayoutInflater.class.getDeclaredFields();
                for (Field field : fields) {
                    Log.d(TAG, "onCreate: field=" +field.getName());
                }

                mFactory2.setAccessible(true);
                Log.d(TAG, "mFactory2 可以得到");
                mFactory2.set(layoutInflater,factory2);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                Log.e(TAG,"找不到factory2");
                e.printStackTrace();
            }
        }
        skinFactories.put(activity, factory2);
        observable.addObserver(factory2);


    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        SkinFactory factory = skinFactories.remove(activity);
        SkinManager.getInstance().deleteObserver(factory);
    }
}
