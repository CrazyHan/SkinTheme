package com.slh.skin.lib;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SkinHolder {

    View view;

    List<SkinPair> skinPairs = new ArrayList<>();


    public static class SkinPair{
        public String attrValue;
        public String attrName;
    }



}
