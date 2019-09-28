package com.company.project.model.common;

import org.apache.http.HttpHost;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
public class Channel {
    public final HttpHost proxy;
    public final HttpHost target;

    public Channel(HttpHost proxy, HttpHost target) {
        this.proxy = proxy;
        this.target = target;
    }
}
