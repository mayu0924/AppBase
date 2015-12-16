package com.appheader.base.sdk.wxpay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.Toast;

import com.appheader.base.common.assit.MD5;
import com.appheader.base.sdk.umeng.UMSdkManager;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.WXPayConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mayu on 15/8/11,下午1:54.
 * 微信支付
 */
public class WeixinPayMent {
    private Activity mAct;
    private static WeixinPayMent mWeixinPayment;
    private StringBuffer sb;

    public static WeixinPayMent getInstance(Activity act){
        if(mWeixinPayment == null){
            mWeixinPayment = new WeixinPayMent(act);
        }
        return mWeixinPayment;
    }

    public WeixinPayMent(Activity act) {
        this.mAct = act;
    }

    public void sendPay(String prepayid, String timestamp) {
    	if(!UMSdkManager.isWXAppInstalledAndSupported(mAct)){
    		Toast.makeText(mAct, "您还没有安装微信", Toast.LENGTH_SHORT).show();
    		return;
    	}
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(mAct, null);
        PayReq req;
        req = new PayReq();
        this.sb = new StringBuffer();
        msgApi.registerApp(WXPayConfig.APP_ID);
        // 公众账号ID【微信分配的公众账号ID】
        req.appId = WXPayConfig.APP_ID;
        // 商户号【微信支付分配的商户号】
        req.partnerId = WXPayConfig.MCH_ID;
        // 预支付交易会话ID【微信返回的支付交易会话ID】
        req.prepayId = prepayid;
        // 扩展字段【暂填写固定值Sign=WXPay】
        req.packageValue = "Sign=WXPay";
        // 随机字符串【随机字符串，不长于32位。推荐随机数生成算法】
        req.nonceStr = MD5.encrypt(timestamp);
        // 时间戳【时间戳，请见接口规则-参数规定】
        req.timeStamp = timestamp;
        // 签名【签名，详见签名生成算法】
        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        // 签名【签名，详见签名生成算法】
        req.sign = genAppSign(signParams);
        msgApi.registerApp(WXPayConfig.APP_ID);
        msgApi.sendReq(req);
    }

    /**
     * 获取签名算法
     * @param params
     * @return
     */
    @SuppressLint("DefaultLocale")
	public String genAppSign (List < NameValuePair > params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(WXPayConfig.API_KEY);
        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = net.sourceforge.simcpux.MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }
}
