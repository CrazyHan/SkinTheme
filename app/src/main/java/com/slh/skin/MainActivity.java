package com.slh.skin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.slh.skin.lib.SkinManager;


public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    String TAG = MainActivity.class.getName();
    boolean d = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(d){
                    SkinManager.getInstance().loadSkin("data/data/com.slh.skin/skin/skin-debug.apk");
                }else{
                    SkinManager.getInstance().loadSkin("");
                }
                d = !d;
            }
        });

    }
}