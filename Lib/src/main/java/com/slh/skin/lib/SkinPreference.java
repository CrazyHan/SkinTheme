package com.slh.skin.lib;

import com.tencent.mmkv.MMKV;

public class SkinPreference {


    public static final String SKIN_PATH = "SKIN_PATH";
    public static final String IS_DEFAULT = "IS_DEFAULT";


    public static MMKV mmkv = MMKV.defaultMMKV();


    public static boolean isDefaultSkin() {
        return mmkv.getBoolean(IS_DEFAULT, false);
    }


    public static void setDefaultSkin(boolean s) {
        mmkv.putBoolean(IS_DEFAULT, s);
    }

    public static String getSkinPath() {
        return mmkv.getString(SKIN_PATH, "");
    }

    public static void setSkinPath(String path) {
        mmkv.putString(SKIN_PATH, path);
    }

}
