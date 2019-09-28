package com.company.project.action.impl;

import com.google.common.collect.Maps;

import com.company.project.action.PcHttpRetryableAction;
import com.company.project.context.Context;
import com.company.project.model.common.HttpResponseAdapt;

import org.apache.http.client.methods.RequestBuilder;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
@Slf4j
public class QueryBaiduAction extends PcHttpRetryableAction {

    @Override
    protected RequestBuilder build(Context context) {
        return RequestBuilder.get("http://www.baidu.com");
    }

    @Override
    protected void addCustomHeader(RequestBuilder builder, Context context) {

    }

    @Override
    protected Map<String, String> buildParameters(Context context) {
        Map<String, String> params = Maps.newHashMap();
        params.put("key", "test");
        return params;
    }

    @Override
    public boolean consumer(Context context, HttpResponseAdapt response) throws Exception {
        log.info("{} action,response:{}", response.getContext());
        return false;
    }

    @Override
    public void onComplete(Context context) {

    }
}