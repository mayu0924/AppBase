package com.appheader.base.common.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.orhanobut.logger.LogLevel;

/**
 * Description: 通用的Log管理工具类
 * 开发阶段LOGLEVEL = 6
 * 发布阶段LOGLEVEL = -1
 */

public class Logger {

    public static String mTag = "MVPBestPractice";
    private static int LOGLEVEL = 6;
    private static int VERBOSE = 1;
    private static int DEBUG = 2;
    private static int INFO = 3;
    private static int WARN = 4;
    private static int ERROR = 5;

    static {
        com.orhanobut.logger.Logger
                .init(mTag)                       // default PRETTYLOGGER or use just init()
                .setMethodCount(3)                // default 2
                .hideThreadInfo()                 // default shown
                .setLogLevel(LogLevel.FULL);      // default LogLevel.FULL
    }

    public static void setDevelopMode(boolean flag) {
        if (flag) {
            LOGLEVEL = 6;
            com.orhanobut.logger.Logger.init().setLogLevel(LogLevel.FULL);
        } else {
            LOGLEVEL = -1;
            com.orhanobut.logger.Logger.init().setLogLevel(LogLevel.NONE);
        }
    }

    public static void v(@NonNull String tag, String msg) {
        if (LOGLEVEL > VERBOSE && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//            Log.v(tag, msg);
            com.orhanobut.logger.Logger.t(tag).v(msg);
        }
    }

    public static void d(@NonNull String tag, String msg) {
        if (LOGLEVEL > DEBUG && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//            Log.d(tag, msg);
            com.orhanobut.logger.Logger.t(tag).d(msg);
        }
    }

    public static void i(@NonNull String tag, String msg) {
        if (LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//            Log.i(tag, msg);
            com.orhanobut.logger.Logger.t(tag).i(msg);
        }
    }

    public static void w(@NonNull String tag, String msg) {
        if (LOGLEVEL > WARN && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//            Log.w(tag, msg);
            com.orhanobut.logger.Logger.t(tag).w(msg);
        }
    }

    public static void e(@NonNull String tag, String msg) {
        if (LOGLEVEL > ERROR && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//            Log.e(tag, msg);
            com.orhanobut.logger.Logger.t(tag).e(msg);
        }
    }

    public static void e(@NonNull String tag, Exception e) {
        tag = checkTag(tag);
        if (LOGLEVEL > ERROR) {
//            Log.e(tag, e==null ? "未知错误" : e.getMessage());
            com.orhanobut.logger.Logger.t(tag).e(e == null ? "未知错误" : e.getMessage());
        }
    }

    public static void v(String msg) {
        if (LOGLEVEL > VERBOSE && !TextUtils.isEmpty(msg)) {
//            Log.v(mTag, msg);
            com.orhanobut.logger.Logger.v(msg);
        }
    }

    public static void d(String msg) {
        if (LOGLEVEL > DEBUG && !TextUtils.isEmpty(msg)) {
//            Log.d(mTag, msg);
            com.orhanobut.logger.Logger.d(msg);
        }
    }

    public static void i(String msg) {
        if (LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
//            Log.i(mTag, msg);
            com.orhanobut.logger.Logger.i(msg);
        }
    }

    public static void w(String msg) {
        if (LOGLEVEL > WARN && !TextUtils.isEmpty(msg)) {
//            Log.w(mTag, msg);
            com.orhanobut.logger.Logger.v(msg);
        }
    }

    public static void e(String msg) {
        if (LOGLEVEL > ERROR && !TextUtils.isEmpty(msg)) {
//            Log.e(mTag, msg);
            com.orhanobut.logger.Logger.e(msg);
        }
    }

    public static void e(Exception e) {
        if (LOGLEVEL > ERROR) {
//            Log.e(mTag, e==null ? "未知错误" : e.getMessage());
            com.orhanobut.logger.Logger.e(e == null ? "未知错误" : e.getMessage());
        }
    }

    public static void wtf(@NonNull String tag, String msg) {
        if (LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//            Log.i(tag, msg);
            com.orhanobut.logger.Logger.t(tag).wtf(msg);
        }
    }

    public static void json(@NonNull String tag, String msg) {
        if (LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//            Log.i(tag, msg);
            com.orhanobut.logger.Logger.t(tag).json(msg);
        }
    }

    public static void xml(@NonNull String tag, String msg) {
        if (LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
            tag = checkTag(tag);
//            Log.i(tag, msg);
            com.orhanobut.logger.Logger.t(tag).xml(msg);
        }
    }

    public static void wtf(String msg) {
        if (LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
//            Log.i(tag, msg);
            com.orhanobut.logger.Logger.wtf(msg);
        }
    }

    public static void json(String msg) {
        if (LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
//            Log.i(tag, msg);
            com.orhanobut.logger.Logger.json(msg);
        }
    }

    public static void xml(String msg) {
        if (LOGLEVEL > INFO && !TextUtils.isEmpty(msg)) {
//            Log.i(tag, msg);
            com.orhanobut.logger.Logger.xml(msg);
        }
    }

    private static String checkTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            tag = mTag;
        }
        return tag;
    }
}