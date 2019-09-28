package com.company.project.action;

import com.google.common.base.Charsets;

import com.company.project.context.Context;
import com.company.project.utils.HttpRequestUtil;
import com.company.project.utils.RequestBuilderHelper;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import java.io.IOException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 *
 * http 重试抽象逻辑类 主要负责构建公共参数
 */
@Slf4j
public abstract class PcHttpRetryableAction extends AbstractHttpRetryableAction {

    @Override
    public HttpUriRequest product(Context context) throws IOException {

        RequestBuilder builder = build(context);
        setCharset(builder);
        Map<String, String> params = buildParameters(context);
        RequestBuilderHelper.addParameters(params, builder);
        RequestBuilderHelper.addCommonsHeader(context, builder);
        //各个action自定义头信息
        addCustomHeader(builder, context);
        HttpUriRequest request = builder.build();

        log.debug("参数信息 {}",builder.getParameters());

        return request;
    }



    private void setCharset(RequestBuilder builder) {
        if(HttpRequestUtil.isPostRequest(builder)){
            builder.setCharset(Charsets.UTF_8);
        }
    }

    /**
     * 构建http请求接口
     * @param context 接口执行上下文
     * @return RequestBuilder
     */
    protected abstract RequestBuilder build(Context context);

    /**
     * 增加定制化header信息
     * @param builder ParameterBuilder
     * @param context
     */
    protected abstract void addCustomHeader(RequestBuilder builder, Context context);

    /**
     * 构建http接口请求参数
     * @param context 接口执行上下文
     * @return ParameterBuilder
     */
    protected abstract Map<String, String> buildParameters(Context context);
}
