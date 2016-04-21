package com.appheader.ui_demo.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.appheader.ui_demo.R;
import com.appheader.ui_demo.arclayout.ArcLayoutActivity;
import com.appheader.ui_demo.drapdownmenu.DropDownMenuActivity;
import com.appheader.ui_demo.roundView.RoundViewActivity;
import com.appheader.ui_demo.smoothCheckBox.SmoothCheckBoxActivity;
import com.appheader.ui_demo.sweet_dialog.SweetDialogActivity;
import com.appheader.ui_demo.swipecardview.SwipecardviewActivity;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_base);
        ButterKnife.bind(this);

        service();
    }
    private void service() {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("name", "18722493013");
        properties.put("pwd", "msy649166");

        WebServiceUtil mWebServiceUtil = new WebServiceUtil();
        mWebServiceUtil.setIsDebug(true);
        mWebServiceUtil.setIsDotNet(true);
        SoapObject soapObject = mWebServiceUtil.GetObject( "https://www.chinaytjf.com/YTWS/Login.asmx", "http://WebXml.com.cn/", "UserLogin", properties);
        Log.d("ccc", soapObject + "");
    }
    @OnClick(R.id.rtv_1)
    public void startSweetDialogActivity(){
        startActivity(new Intent(this, SweetDialogActivity.class));
    }

    @OnClick(R.id.rtv_2)
    public void startRoundViewActivity(){
        startActivity(new Intent(this, RoundViewActivity.class));
    }

    @OnClick(R.id.rtv_3)
    public void startDrapDownMenuActivity(){
        startActivity(new Intent(this, DropDownMenuActivity.class));
    }

    @OnClick(R.id.rtv_4)
    public void startSmoothCheckBoxActivity(){
        startActivity(new Intent(this, SmoothCheckBoxActivity.class));
    }

    @OnClick(R.id.rtv_5)
    public void startSwipecardviewActivity(){
        startActivity(new Intent(this, SwipecardviewActivity.class));
    }

    @OnClick(R.id.rtv_6)
    public void startArcLayoutActivity(){
        startActivity(new Intent(this, ArcLayoutActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
