package com.company.project.utils;

import com.company.project.context.Context;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.RequestBuilder;

import java.util.Map;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
public class RequestBuilderHelper {
    public static void addParameters(Map<String, String> params, RequestBuilder builder){
        if (MapUtils.isNotEmpty(params)){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
        }
    }

    public static void addCommonsHeader(Context context, RequestBuilder builder) {
        if (StringUtils.isNotBlank(context.refer)) {
            builder.addHeader(HttpHeaders.REFERER, context.refer);
        }
        builder.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
        builder.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        builder.addHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,en;q=0.8");
        builder.addHeader(HttpHeaders.CONNECTION, "keep-alive");
    }
}
