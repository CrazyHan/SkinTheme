package com.slh.skin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import com.slh.skin.lib.SkinManager;

public class SkinActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
boolean defaultSkin = false;
    public void skinSelect(View view){
        if (defaultSkin) {
            SkinManager.getInstance().loadSkin("/data/data/com.slh.skin/skin/skin-debug.apk");
        }else{
            SkinManager.getInstance().loadSkin(null);
        }
        defaultSkin = !defaultSkin;
    }

}
