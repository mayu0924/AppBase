package com.appheader.base.common.network.entity;

/**
 * Volley配置
 * @author mayu
 */
public class HTTPConfig {
    // http parameters
    public static final int HTTP_MEMORY_CACHE_SIZE = 2 * 1024 * 1024; // 2MB
    public static final int HTTP_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
    public static final String HTTP_DISK_CACHE_DIR_NAME = "AppHeader";
    public static final String USER_AGENT = "www.appheader.cn";
}