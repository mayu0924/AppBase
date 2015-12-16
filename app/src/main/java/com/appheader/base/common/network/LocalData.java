package com.appheader.base.common.network;

import com.appheader.base.common.utils.FileUtil;
import com.appheader.base.common.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mayu on 15/8/3,上午11:59.
 */
public class LocalData {

    public static boolean isSumilate = false;

    /**Api_simulate-------------------------------------------------------------------------*/

    

    /**Api_simulate-------------------------------------------------------------------------*/

    /**-------------------------------------------------------------------------------------*/
    public static JSONObject readFromSimulate(String TAG){
        String resultJsonStr = null;
        try {
            if (TAG != null) {
                String content = FileUtil.readTextFromAssets("simulate_data/" + TAG + ".json");
                if (content == null)
                    resultJsonStr = makeErrorJsonResult("模拟数据错误：未找到常量名称对应的模拟数据文件（" + TAG + ".json）。");
                else {
                    resultJsonStr = content;
                    Thread.sleep(1000);
                }
            } else {
                resultJsonStr = makeErrorJsonResult("模拟数据错误：未找到url对应的Field，请检查是否在UrlConstants类中定义了常量。");
            }
        } catch (Exception e) {
            LogUtil.error("LocaData", e);
        }
        try {
            return new JSONObject(resultJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    /**
     * 拼装错误信息的json
     *
     * @param errMsg
     * @return
     * @throws JSONException
     */
    private static String makeErrorJsonResult(String errMsg) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("success", "false");
        json.put("data", new JSONObject());
        json.put("errorMsg", errMsg);
        json.put("errCode", "");
        return json.toString();
    }
}
