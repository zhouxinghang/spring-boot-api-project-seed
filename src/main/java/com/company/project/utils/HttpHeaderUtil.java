package com.company.project.utils;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.RequestBuilder;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
public class HttpHeaderUtil {
    public static final String HTML_ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";

    public static void addHtmlHead(RequestBuilder builder){
        builder.addHeader(HttpHeaders.ACCEPT, HTML_ACCEPT);
    }

    public static void addCacheHead(RequestBuilder builder){
        builder.addHeader(HttpHeaders.CACHE_CONTROL, "max-age=0");
    }

    public static void addUpgradeHead(RequestBuilder builder){
        builder.addHeader("Upgrade-Insecure-Requests", "1");
    }

    public static void addPostOnlyHead(RequestBuilder builder){
        builder.addHeader(HttpHeaders.CONTENT_TYPE,"application/x-www-form-urlencoded; charset=UTF-8");
    }

    public static void addContentTypeHead(RequestBuilder builder){
        builder.addHeader(HttpHeaders.CONTENT_TYPE,"application/x-www-form-urlencoded");

    }

    public static void addAjaxJsonHead(RequestBuilder builder){
        builder.addHeader(HttpHeaders.ACCEPT,"application/json, text/javascript, */*; q=0.01");
        builder.addHeader("X-Requested-With","XMLHttpRequest");
    }

    public static void addHost(RequestBuilder builder, String host){
        builder.addHeader(HttpHeaders.HOST, host);
    }

    public static void addOrigin(RequestBuilder builder, String origin){
        builder.addHeader("Origin", origin);
    }
}
