package com.appheader.base.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.appheader.base.application.GlobalVars;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 手机信息 & MAC地址 & 开机时间
 *
 * @author MaTianyu
 * @date 2014-09-25
 */
public class AndroidUtil {
    private static final String TAG = AndroidUtil.class.getSimpleName();

    /**
     * 获取 MAC 地址
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     */
    public static String getMacAddress(Context context) {
        //wifi mac地址
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        if (GlobalVars.isDebug) {
            Log.i(TAG, " MAC：" + mac);
        }
        return mac;
    }

    /**
     * 获取 开机时间
     */
    public static String getBootTimeString() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        int h = (int) ((ut / 3600));
        int m = (int) ((ut / 60) % 60);
        if (GlobalVars.isDebug) {
            Log.i(TAG, h + ":" + m);
        }
        return h + ":" + m;
    }

    @SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	public static String printSystemInfo() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        StringBuilder sb = new StringBuilder();
        sb.append("_______  系统信息  ").append(time).append(" ______________");
        sb.append("\nID                 :").append(Build.ID);
        sb.append("\nBRAND              :").append(Build.BRAND);
        sb.append("\nMODEL              :").append(Build.MODEL);
        sb.append("\nRELEASE            :").append(Build.VERSION.RELEASE);
        sb.append("\nSDK                :").append(Build.VERSION.SDK);

        sb.append("\n_______ OTHER _______");
        sb.append("\nBOARD              :").append(Build.BOARD);
        sb.append("\nPRODUCT            :").append(Build.PRODUCT);
        sb.append("\nDEVICE             :").append(Build.DEVICE);
        sb.append("\nFINGERPRINT        :").append(Build.FINGERPRINT);
        sb.append("\nHOST               :").append(Build.HOST);
        sb.append("\nTAGS               :").append(Build.TAGS);
        sb.append("\nTYPE               :").append(Build.TYPE);
        sb.append("\nTIME               :").append(Build.TIME);
        sb.append("\nINCREMENTAL        :").append(Build.VERSION.INCREMENTAL);

        sb.append("\n_______ CUPCAKE-3 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            sb.append("\nDISPLAY            :").append(Build.DISPLAY);
        }

        sb.append("\n_______ DONUT-4 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            sb.append("\nSDK_INT            :").append(Build.VERSION.SDK_INT);
            sb.append("\nMANUFACTURER       :").append(Build.MANUFACTURER);
            sb.append("\nBOOTLOADER         :").append(Build.BOOTLOADER);
            sb.append("\nCPU_ABI            :").append(Build.CPU_ABI);
            sb.append("\nCPU_ABI2           :").append(Build.CPU_ABI2);
            sb.append("\nHARDWARE           :").append(Build.HARDWARE);
            sb.append("\nUNKNOWN            :").append(Build.UNKNOWN);
            sb.append("\nCODENAME           :").append(Build.VERSION.CODENAME);
        }

        sb.append("\n_______ GINGERBREAD-9 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sb.append("\nSERIAL             :").append(Build.SERIAL);
        }
        Log.i(TAG, sb.toString());
        return sb.toString();
    }

    private static final char[] HEX_CHAR = {

            '0', '1', '2', '3', '4', '5', '6', '7',

            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'

    };

    /** 获取签名的MD5摘要 */

    public String[] signatureDigest(Context ctx, String pkgName) {
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = ctx.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
        } catch (Exception e){
            e.printStackTrace();
        }


        int length = pkgInfo.signatures.length;

        String[] digests = new String[length];

        for (int i = 0; i < length; ++i) {

            Signature sign = pkgInfo.signatures[i];

            try {

                MessageDigest md5 = MessageDigest.getInstance("MD5");

                byte[] digest = md5.digest(sign.toByteArray()); // get digest with md5 algorithm

                digests[i] = toHexString(digest);

            } catch (NoSuchAlgorithmException e) {

                e.printStackTrace();

                digests[i] = null;

            }

        }

        return digests;

    }

    /** 将字节数组转化为对应的十六进制字符串 */

    private String toHexString(byte[] rawByteArray) {

        char[] chars = new char[rawByteArray.length * 2];

        for (int i = 0; i < rawByteArray.length; ++i) {

            byte b = rawByteArray[i];

            chars[i*2] = HEX_CHAR[(b >>> 4 & 0x0F)];

            chars[i*2+1] = HEX_CHAR[(b & 0x0F)];

        }

        return new String(chars);

    }
}
