package com.company.project.context;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.company.project.enums.BizTypeEnum;
import com.company.project.utils.TimeUtils;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
public class Context {
    public final String key;
    public Map<String, Object> attach = Maps.newConcurrentMap();
    public BizTypeEnum bizTypeEnum;
    public HttpClient httpClient;
    public int httpCode;

    /**
     * 请求refer
     */
    public String refer;
    /**
     * 请求url
     */
    public String url;
    /**
     * 代理信息
     * new HttpHost("103.37.167.12", 8866);
     */
    public HttpHost proxy;
    /**
     * cdn
     * new HttpHost("122.13.74.253", 443);
     */
    public HttpHost target;
    /**
     * 操作链中忽略的操作
     */
    public List<Class> ignoreActions = Lists.newArrayList();

    private static AtomicLong atomicLong = new AtomicLong(1);


    public Context() {
        key = initKey();
    }

    private String initKey() {
        String date = TimeUtils.today();
        long sq = (atomicLong.getAndIncrement() << 48) >>> 48;
        String x = Strings.padStart(String.valueOf(sq), 6, '0');
        return  date + x;
    }


    public <T> T getAttach(String key) {
        Object object = attach.get(key);
        if (object == null) {
            throw new IllegalStateException("Not found key = " + key);
        }
        return (T) object;
    }

    public <T> T getAttachOrDefault(String key, T value) {
        Object object = attach.get(key);
        if (object == null) {
            return value;
        }
        return (T) object;
    }
}
