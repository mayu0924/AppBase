package com.appheader.base.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.appheader.base.common.utils.LogUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.WXPayConfig;

/**
 * 微信支付
 *
 * @author mayu
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = WXPayEntryActivity.class.getSimpleName();

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(null);
        api = WXAPIFactory.createWXAPI(this, WXPayConfig.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    /**
     * 【支付操作结果回调】
     * resp.errCode
     * 0 成功 展示成功页面
     * -1 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
     * -2 用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
     */
    @Override
    public void onResp(BaseResp resp) {
    	LogUtil.debug(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
            	Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                dealSuccess();
            } else if (resp.errCode == -1) {
            	Toast.makeText(this, "支付失败-1", Toast.LENGTH_SHORT).show();
            } else if (resp.errCode == -2) {
            	Toast.makeText(this, "支付失败-2", Toast.LENGTH_SHORT).show();
            } else {
            	Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
            }
            Intent pay = new Intent("pay_result");
        	pay.putExtra("code", resp.errCode);
        	this.sendBroadcast(pay);
        }
        finish();
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq resp) {
        LogUtil.debug(TAG, resp.toString());
    }

    private void dealSuccess(){
        // TODO ...支付成功后的操作
    }
}