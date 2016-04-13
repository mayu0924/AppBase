package com.appheader.ui_demo.smoothCheckBox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.appheader.ui_demo.R;
import com.appheader.ui_demo.smoothCheckBox.lib.SmoothCheckBox;

/**
 * https://github.com/andyxialm/SmoothCheckBox
 */
public class SmoothCheckBoxActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox);

        final SmoothCheckBox scb = (SmoothCheckBox) findViewById(R.id.scb);
        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                Log.d("SmoothCheckBox", String.valueOf(isChecked));
            }
        });
    }

}
