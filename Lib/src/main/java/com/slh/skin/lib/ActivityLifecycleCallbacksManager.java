package com.slh.skin.lib;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Observable;

public class ActivityLifecycleCallbacksManager implements Application.ActivityLifecycleCallbacks {

    HashMap<Activity, SkinFactory> skinFactories = new HashMap<>();



    Observable observable;

    public ActivityLifecycleCallbacksManager(Observable observable) {
        this.observable = observable;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        Field mFactorySet = null;
        try {
            mFactorySet = LayoutInflater.class.getDeclaredField("mFactorySet");
            mFactorySet.setAccessible(true);
            mFactorySet.set(layoutInflater, false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        SkinFactory factory2 = new SkinFactory();
        LayoutInflaterCompat.setFactory2(layoutInflater, factory2);
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

    }
}
