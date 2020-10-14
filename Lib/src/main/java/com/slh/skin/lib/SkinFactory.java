package com.slh.skin.lib;

import android.content.Context;
import android.os.Build;
import android.os.Trace;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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



    private List<SkinAttribute.SkinView> skinViews = new ArrayList<>();

    final Object[] mConstructorArgs = new Object[2];

    static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<String, Constructor<? extends View>>();

    @Nullable
    @Override
    public View onCreateView(@Nullable View view, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        View result ;
        if (-1 == name.indexOf('.')) {
            result = createOriginalView(view, name, context, attributeSet);
        } else {
            result = createView(context, name,null,  attributeSet);
        }

        return result;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String s, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        return null;
    }

    public View createOriginalView(View parent, String name, Context context, AttributeSet attrs) {

        View view = null;

        for (int i = 0; i < mClassPrefixList.length; i++) {
            String fullClassName = mClassPrefixList[i] + name;
            view = createView(context, fullClassName, null, attrs);
            if (view != null) {
                return view;
            }
        }

        return null;

    }

    public View createView(@NonNull Context viewContext, @NonNull String name,
                           @Nullable String prefix, @Nullable AttributeSet attrs) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (constructor != null) {
            constructor = null;
            sConstructorMap.remove(name);
        }
        Class<? extends View> clazz = null;

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                clazz = Class.forName(prefix != null ? (prefix + name) : name, false,
                        viewContext.getClassLoader()).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(name, constructor);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Object lastContext = mConstructorArgs[0];
        mConstructorArgs[0] = viewContext;
        Object[] args = mConstructorArgs;
        args[1] = attrs;

        try {
            final View view = constructor.newInstance(args);
            if (view instanceof ViewStub) {
                // Use the same context when inflating ViewStub later.
                final ViewStub viewStub = (ViewStub) view;
                viewStub.setLayoutInflater(LayoutInflater.from(viewContext));
            }
            List<SkinAttribute.SkinAttrPair> attrPairs = new ArrayList<>();

            SkinAttribute.SkinView skinView = new SkinAttribute.SkinView(view);
            skinView.storeSkinAttr(attrs);
            skinViews.add(skinView);

            return view;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            mConstructorArgs[0] = lastContext;
        }
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        for (SkinAttribute.SkinView skinView : skinViews) {
            skinView.applySkin();
        }
    }
}
