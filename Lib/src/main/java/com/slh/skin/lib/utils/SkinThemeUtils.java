package com.slh.skin.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;

public class SkinThemeUtils {
    private static int[] APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = {
            androidx.appcompat.R.attr.colorPrimaryDark
    };
    private static int[] STATUSBAR_COLOR_ATTRS = {android.R.attr.statusBarColor, android.R.attr
            .navigationBarColor
    };
    public static int[] getResId(Context context,int[] attrs){
        TypedArray a = context.obtainStyledAttributes(attrs);
        int[] result = new int[attrs.length];
        for (int i = 0; i < attrs.length; i++) {
            result[i] = a.getResourceId(i, 0);
        }
        a.recycle();
        return result;
    }


    public static void updateStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        int[] resIds = getResId(activity, STATUSBAR_COLOR_ATTRS);
        int statusBarColorId = resIds[0];
        int navigationBarColorId = resIds[1];

        //修改statusBarColor
        if (statusBarColorId != 0) {
            int statusBarColor = SkinResources.getInstance().getColor(statusBarColorId);
            activity.getWindow().setStatusBarColor(statusBarColor);
        }else{//如果得到的属性值为0 那么就从兼容属性里面拿
            int compatResId = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0];
            if (compatResId != 0) {
                int statusBarColor = SkinResources.getInstance().getColor(compatResId);
                activity.getWindow().setStatusBarColor(statusBarColor);
            }
        }
        //修改navigationBarColor

        if (navigationBarColorId != 0) {
            int navigationBarColor = SkinResources.getInstance().getColor(navigationBarColorId);
            activity.getWindow().setNavigationBarColor(navigationBarColor);

        }

    }

}
