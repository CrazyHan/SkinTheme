package com.slh.skin.lib;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.slh.skin.lib.utils.SkinThemeUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.StringJoiner;

public class SkinFactory implements LayoutInflater.Factory2 , Observer {
    //系统自带的View前缀可以根据自己的需求写死
    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.webkit.",
            "android.app.",
            "android.view."
    };
    private Activity activity;

    private SkinAttribute attribute;

    private static final String TAG = SkinFactory.class.getName();

    static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<String, Constructor<? extends View>>();

    public SkinFactory(Activity activity) {
        this.activity = activity;
        attribute = new SkinAttribute();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attributeSet) {

        return onCreateView(name, context, attributeSet);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        View view = createOriginalView(name, context, attributeSet);
        if (view == null) {
            view = createView(context, name, attributeSet);
        }
        if (view != null) {
            attribute.storeSkinAttr(view, attributeSet);
        }
        return view;
    }

    public View createOriginalView(String name, Context context, AttributeSet attrs) {

        View view = null;


        if (-1 != name.indexOf('.')) {
            view = createView(context, name, attrs);
        }else{
            for (int i = 0; i < mClassPrefixList.length; i++) {
                String fullClassName = mClassPrefixList[i] + name;
                view = createView(context, fullClassName, attrs);
                if (view != null) {
                    return view;
                }
            }
        }

        return view;

    }

    public View createView( Context context, String name,  AttributeSet attrs) {
        Constructor<? extends View> constructor = findConstructor(context, name);
        try {
            if(constructor!=null) {
                return constructor.newInstance(context, attrs);
            }else{
                Log.d(TAG, "createView: 失败"+name);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Constructor<? extends View> findConstructor(Context context,String name){
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (constructor == null) {
            try {
                Class<? extends View> clazz =context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                sConstructorMap.put(name, constructor);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return constructor;
    }

    @Override
    public void update(Observable o, Object arg) {
        SkinThemeUtils.updateStatusBarColor(activity);
        attribute.applySkin();
    }
}
