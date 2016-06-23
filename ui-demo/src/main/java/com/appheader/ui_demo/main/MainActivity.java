package com.appheader.ui_demo.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.appheader.ui_demo.R;
import com.appheader.ui_demo.arclayout.ArcLayoutActivity;
import com.appheader.ui_demo.drapdownmenu.DropDownMenuActivity;
import com.appheader.ui_demo.marqueenView.MarqueeView;
import com.appheader.ui_demo.mine.CircleBarActivity;
import com.appheader.ui_demo.mine.QQHealthViewActivity;
import com.appheader.ui_demo.roundView.RoundViewActivity;
import com.appheader.ui_demo.smoothCheckBox.SmoothCheckBoxActivity;
import com.appheader.ui_demo.sweet_dialog.SweetDialogActivity;
import com.appheader.ui_demo.swipecardview.SwipecardviewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.mv)
    MarqueeView mv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_base);
        ButterKnife.bind(this);
        final List<String> info = new ArrayList<>();
        info.add("1. 大家好，我是孙福生。");
        info.add("2. 欢迎大家关注我哦！");
        info.add("3. GitHub帐号：sfsheng0322");
        info.add("4. 新浪微博：孙福生微博");
        info.add("5. 个人博客：sunfusheng.com");
        info.add("6. 微信公众号：孙福生");
        mv.startWithList(info);
        mv.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Toast.makeText(MainActivity.this, info.get(mv.getPosition()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.rtv_1)
    public void startSweetDialogActivity() {
        startActivity(new Intent(this, SweetDialogActivity.class));
    }

    @OnClick(R.id.rtv_2)
    public void startRoundViewActivity() {
        startActivity(new Intent(this, RoundViewActivity.class));
    }

    @OnClick(R.id.rtv_3)
    public void startDrapDownMenuActivity() {
        startActivity(new Intent(this, DropDownMenuActivity.class));
    }

    @OnClick(R.id.rtv_4)
    public void startSmoothCheckBoxActivity() {
        startActivity(new Intent(this, SmoothCheckBoxActivity.class));
    }

    @OnClick(R.id.rtv_5)
    public void startSwipecardviewActivity() {
        startActivity(new Intent(this, SwipecardviewActivity.class));
    }

    @OnClick(R.id.rtv_6)
    public void startArcLayoutActivity() {
        startActivity(new Intent(this, ArcLayoutActivity.class));
    }

    @OnClick(R.id.rtv_7)
    public void startCircleBarActivity() {
        startActivity(new Intent(this, CircleBarActivity.class));
    }

    @OnClick(R.id.rtv_8)
    public void startQQHealthViewActivity() {
        startActivity(new Intent(this, QQHealthViewActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
