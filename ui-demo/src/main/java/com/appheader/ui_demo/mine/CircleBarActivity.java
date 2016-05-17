package com.appheader.ui_demo.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.appheader.ui_demo.R;

/**
 * Created by mayu on 16/5/11,下午3:40.
 */
public class CircleBarActivity extends AppCompatActivity {
    private Circlebar progress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circlebar);
        initProgress(263);

    }

    private void initProgress(int value) {
        progress = (Circlebar) findViewById(R.id.progress);
        progress.setMaxValue(340);
        progress.updateValue(value);
    }
}
