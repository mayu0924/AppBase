package com.appheader.base.common.network;

import com.appheader.base.application.GlobalVars;
import com.appheader.base.common.user.CurrentUserManager;
import com.appheader.base.common.utils.LogUtil;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 文件上传util，直接上传不带更新进度的功能
 */
public class UploadUtil {

    private static UploadUtil uploadUtil;

    private UploadUtil() {

    }

    /**
     * @return
     */
    public static UploadUtil getInstance() {
        if (null == uploadUtil) {
            uploadUtil = new UploadUtil();
        }
        return uploadUtil;
    }

    public interface OnProgressUpdateListener {
        public void onUpdate(int progress);
    }

    public interface ResponseCallback {
        public void onResponse(String responseData);
    }

    public String uploadFile(String filePath, String requestURL) throws Exception {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        return uploadFile(fis, GlobalVars.getAppServerUrl() + requestURL);
    }

    public String uploadFile(InputStream inputStream, String requestURL) throws Exception {
        StringBuilder paramStr = new StringBuilder();
        String sessionId = CurrentUserManager.getCurrentUser().getSession_id();
        URL url = new URL(requestURL + paramStr.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(1000 * 10);
        conn.setReadTimeout(60 * 1000 * 2);
        conn.addRequestProperty("session_id", sessionId);
        conn.addRequestProperty("platform", "android");
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");

        DataOutputStream ds = new DataOutputStream(conn.getOutputStream());

        int bufferSize = 2048;
        byte[] buffer = new byte[bufferSize];

//		long sendTotalLength = 0;

        int length = -1;

        while ((length = inputStream.read(buffer)) != -1) {
            ds.write(buffer, 0, length);
//			sendTotalLength += length;
//			listener.onUpdate(Float.valueOf(((float) sendTotalLength / totalLength) * 100).intValue());
        }

        inputStream.close();
        ds.flush();

        // get response
        InputStream responseIs = conn.getInputStream();
        int ch;
        StringBuffer b = new StringBuffer();
        while ((ch = responseIs.read()) != -1) {
            b.append((char) ch);
        }

        LogUtil.info("UploadUtil", "upload complete! response: " + b.toString());
        ds.close();
        return b.toString();
    }
}
