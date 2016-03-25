package com.appheader.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.appheader.base.common.utils.LogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mayu on 16/3/25,上午10:41.
 * 获取短信验证码，并自动填写
 */
public class SmsCodeReceiver extends BroadcastReceiver {
    private static final String TAG = SmsCodeReceiver.class.getSimpleName();

    public interface ISMSListener {
        public void onSmsReceive(String verifyCode);
    }

    private static ISMSListener mSMSListener;

    public SmsCodeReceiver(ISMSListener ismsListener) {
        mSMSListener = ismsListener;
    }

    //android.provider.Telephony.Sms.Intents

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override

    public void onReceive(Context context, Intent intent) {
        LogUtil.debug("", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            SmsMessage[] messages = getMessagesFromIntent(intent);

            for (SmsMessage message : messages) {

                LogUtil.debug(TAG, message.getOriginatingAddress() + " : " +

                        message.getDisplayOriginatingAddress() + " : " +

                        message.getDisplayMessageBody() + " : " +

                        message.getTimestampMillis());


                String msg = message.getDisplayMessageBody();
                LogUtil.debug(TAG, "MSG: " + msg);
                String verifyCode = null;
                Pattern p = Pattern.compile("\\d{4}");
                Matcher m = p.matcher(msg);
                while (m.find()) {
                    verifyCode = m.group();
                    break;
                }
                LogUtil.debug(TAG, "verifyCode " + verifyCode);
                if (mSMSListener != null) {
                    mSMSListener.onSmsReceive(verifyCode);
                }
            }
        }
    }

    public final SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");

        byte[][] pduObjs = new byte[messages.length][];
        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }

        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }
}
