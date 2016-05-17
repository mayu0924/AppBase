package com.appheader.ui_demo.mine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.appheader.ui_demo.R;

public class QQHealthViewActivity extends AppCompatActivity {

    private SeekBar mSeekBar;
    private QQHealthView mQQHealthView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq_health);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mQQHealthView = (QQHealthView) findViewById(R.id.qqHealthView);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mQQHealthView.setSteps(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
