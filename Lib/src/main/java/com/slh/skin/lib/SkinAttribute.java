package com.slh.skin.lib;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.nfc.cardemulation.CardEmulation;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slh.skin.lib.utils.SkinResources;
import com.slh.skin.lib.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

public class SkinAttribute {

    static String TAG = SkinAttribute.class.getName();



    public static List<String> attributes = new ArrayList<>();

    static {
        attributes.add("textColor");
        attributes.add("background");
        attributes.add("src");
        attributes.add("drawableLeft");
        attributes.add("drawableTop");
        attributes.add("drawableRight");
        attributes.add("drawableBottom");

    }


    public SkinAttribute() {
    }

    private List<SkinView> skinViews = new ArrayList<>();

    public void storeSkinAttr(View view,AttributeSet attributeSet) {
        int count = attributeSet.getAttributeCount();
        List<SkinAttrPair> pairs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String attrName = attributeSet.getAttributeName(i);
            if (attributes.contains(attrName)) {
                String attrValue = attributeSet.getAttributeValue(i);
                Log.i(TAG, "attibuteName ="+attrName);
                Log.i(TAG, "attributeValue = "+attrValue);
                if (attrValue.startsWith("#")) {
                    continue;
                }
                int resId;
                if(attrValue.startsWith("?")){//主题里面有的一些值
                    String value = attrValue.substring(1);
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{Integer.valueOf(value)})[0];

                }else {
                    resId = Integer.parseInt(attrValue.substring(1));
                }
                pairs.add(new SkinAttrPair(resId, attrName));
            }
        }
        SkinView skinView = new SkinView(view,pairs);
        skinView.applySkin();

        skinViews.add(skinView);

    }

    public void applySkin() {
        for (SkinView skinView : skinViews) {
            skinView.applySkin();
        }
    }

    public static class SkinView {

        public View view;

        public  List<SkinAttrPair> pairs = new ArrayList<>();

        public SkinView(View view, List<SkinAttrPair> pairs) {
            this.view = view;
            this.pairs = pairs;
        }

        public void applySkin(){

            for (int i = 0; i < pairs.size(); i++) {
                SkinAttrPair attrPair = pairs.get(i);

                Drawable left = null, top = null, right = null, bottom = null;
                Object background;
                switch (attrPair.attrName) {
                    case "background":
                        background = SkinResources.getInstance().getBackground(attrPair.resId);
                        if (background instanceof Drawable) {
                            view.setBackground((Drawable) background);
                        }else{
                            view.setBackgroundColor((int) background);
                        }
                        break;
                    case "textColor":
                        int color = SkinResources.getInstance().getColor(attrPair.resId);
                        ((TextView) view).setTextColor(color);
                        break;

                    case "src":
                        background = SkinResources.getInstance().getDrawable(attrPair.resId);
                        if (background instanceof Integer) {
                            ((ImageView) view).setImageDrawable((ColorDrawable) background);
                        }else {
                            ((ImageView) view).setImageDrawable((Drawable)background);
                        }
                        break;
                    case "drawableLeft":
                        left = SkinResources.getInstance().getDrawable(attrPair.resId);
                        break;
                    case "drawableTop":
                        top = SkinResources.getInstance().getDrawable(attrPair.resId);
                        break;
                    case "drawableRight":
                        right = SkinResources.getInstance().getDrawable(attrPair.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResources.getInstance().getDrawable(attrPair.resId);
                        break;
                    default:
                        break;

                }
                if (null != left || null != right || null != bottom || null != top) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right,
                            bottom);
                }
            }

        }

        public void applySkinSupport() {
            if (view instanceof SkinViewSupport) {
                ((SkinViewSupport) view).applySkin();
            }
        }
    }

    public static class SkinAttrPair{
        public int resId;
        public String attrName;

        public SkinAttrPair(int resId, String attrName) {
            this.resId = resId;
            this.attrName = attrName;
        }

    }

}
