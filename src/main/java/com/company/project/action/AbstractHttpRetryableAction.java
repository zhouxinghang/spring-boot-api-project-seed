package com.company.project.action;

import com.google.common.collect.ImmutableSet;

import com.company.project.context.Context;
import com.company.project.enums.common.Errors;
import com.company.project.exception.SystemException;
import com.company.project.model.common.Channel;
import com.company.project.model.common.HttpResponseAdapt;
import com.company.project.utils.DesensitizeUtil;
import com.company.project.utils.HttpHeaderUtil;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Set;

import javax.net.ssl.SSLHandshakeException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 *
 * http 重试抽象类
 * 处理重试相关逻辑
 */
@Slf4j
public abstract class AbstractHttpRetryableAction extends AbstractMonitoredAction {

    @Override
    public boolean doInRetry(Context context) throws Exception {
        HttpUriRequest request = product(context);

        if (request == null){
            return true;
        }

        log.debug("http请求: url {}, header {} ",request.getURI(), request.getAllHeaders());

        HttpResponse httpResponse = null;
        boolean result = true;
        try {
            HttpContext httpContext = initHttpContext(context);
            HttpResponseAdapt httpResponseAdapt;
            httpResponse = context.httpClient.execute(request, httpContext);
            httpResponseAdapt = transHttpResponseAdapt(httpResponse);
            log.info("{}-{},http action request:{} ,response:{}",
                     context.bizTypeEnum,context.key, getStringExpress((HttpRequestBase) request), httpResponseAdapt.toSimplenessString(getDesensitizeResponse()));
            int httpCode = httpResponseAdapt.getHttpCode();
            context.httpCode = httpCode;

            if (httpCode >= HttpStatus.SC_OK && httpCode < HttpStatus.SC_MULTIPLE_CHOICES) {
                result = consumer(context, httpResponseAdapt);
            } else if (httpCode >= HttpStatus.SC_MULTIPLE_CHOICES && httpCode < HttpStatus.SC_BAD_REQUEST) {
                result = handle30XResponse(httpResponseAdapt, context);
            } else if (httpCode >= HttpStatus.SC_BAD_REQUEST && httpCode < HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                handle40XResponse();
            } else {
                handleTheRestResponse();
            }
            handleRefer(context,request);
        } catch (ConnectTimeoutException e) {
            if (e.getMessage().contains(context.proxy.toHostString())) {
                //代理机器连接超时
                log.error("{}-{},Proxy.timeout:{}", context.bizTypeEnum,context.key, context.proxy.toHostString());
            }
            throw e;
        } catch (HttpHostConnectException e) {
            if (e.getMessage().contains(context.proxy.toHostString())) {
                //代理机器refused
                log.error("{}-{},Proxy.refused:{}", context.bizTypeEnum,context.key, context.proxy.toHostString());
            }
            throw e;
        } catch (SSLHandshakeException e) {
            if (e.getMessage().contains(context.proxy.toHostString())) {
                log.error("{}-{},Proxy.SSLHandshakeException:{}", context.bizTypeEnum,context.key, context.proxy.toHostString());
            }
            throw e;
        } catch (NoHttpResponseException e) {
            if (e.getMessage().contains(context.proxy.toHostString())) {
                log.error("{}-{},Proxy.NoHttpResponseException:{}", context.bizTypeEnum,context.key, context.proxy.toHostString());
            }
            throw e;
        } finally{
            if (httpResponse != null) {
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }
        }
        return result;
    }

    private HttpResponseAdapt transHttpResponseAdapt(HttpResponse httpResponse) throws IOException {

        HttpResponseAdapt httpResponseAdapt = new HttpResponseAdapt();
        ContentType contentType = ContentType.get(httpResponse.getEntity());

        if (contentType != null) {
            if (StringUtils.contains(contentType.getMimeType(), "image")) {
                httpResponseAdapt.setBody(EntityUtils.toByteArray(httpResponse.getEntity()));
            } else {
                httpResponseAdapt.setContext(EntityUtils.toString(httpResponse.getEntity()));
            }
        }

        if (httpResponse.getStatusLine().getStatusCode() >= HttpStatus.SC_MULTIPLE_CHOICES
            && httpResponse.getStatusLine().getStatusCode() < HttpStatus.SC_BAD_REQUEST) {
            Header locationHeader = httpResponse.getFirstHeader("location");
            if (locationHeader != null) {
                httpResponseAdapt.setRedirectUrl(locationHeader.getValue());
            }
        }
        httpResponseAdapt.setHttpCode(httpResponse.getStatusLine().getStatusCode());

        return httpResponseAdapt;
    }

    /**
     * 组装 http 请求参数
     * @param context  执行上下文
     * @return  请求信息
     * @throws IOException 处理报文异常
     */
    public abstract HttpUriRequest product(Context context) throws IOException;

    /**
     * 处理http接口返回内容
     * @param context 上下文信息
     * @param response http封装响应报文
     * @return  消费是否成功
     * @throws IOException 处理报文异常
     */
    public abstract boolean consumer(Context context, HttpResponseAdapt response) throws Exception;

    protected HttpContext initHttpContext(Context context) {
        Channel channel = new Channel(context.proxy, context.target);
        HttpContext httpContext = new BasicHttpContext();
        httpContext.setAttribute("Channel", channel);

        return httpContext;
    }

    private void  handleRefer(Context context,HttpUriRequest request){
        Header acceptHead =  request.getFirstHeader(HttpHeaders.ACCEPT);
        if (acceptHead != null && StringUtils.equals(acceptHead.getValue(), HttpHeaderUtil.HTML_ACCEPT)) {
            context.refer = request.getURI().toString();
        }
    }

    protected boolean handle30XResponse(HttpResponseAdapt responseAdapt, Context context) throws Exception {
        throw new SystemException(Errors.HTTP_30X, "30X http code");
    }

    protected void handle40XResponse() throws IOException{
        throw new SystemException(Errors.HTTP_40X, "40X http code");
    }

    protected void handleTheRestResponse() throws IOException{
        throw new SystemException(Errors.HTTP_UNKNOWN, "unknown http code");
    }

    protected Set<String> getDesensitizeParameter(){
        return SetUtils.emptySet();
    }

    protected Set<String> getDesensitizeResponse(){
        return SetUtils.emptySet();
    }

    private String getStringExpress(HttpRequestBase httpRequestBase) {
        String result = StringUtils.trimToEmpty(httpRequestBase.getURI().getPath()) + "," + StringUtils.trimToEmpty(httpRequestBase.getURI().getQuery());
        if (httpRequestBase instanceof HttpPost && ((HttpPost) httpRequestBase).getEntity() instanceof UrlEncodedFormEntity) {
            String temp = "";
            try {
                if (!getDesensitizeParameter().isEmpty()) {
                    String[] pairs = org.springframework.util.StringUtils.tokenizeToStringArray(EntityUtils.toString(((HttpPost) httpRequestBase).getEntity(), "utf8"), "&");
                    for (String pair : pairs) {
                        int idx = pair.indexOf('=');
                        if (idx == -1) {
                            temp += pair;
                        } else {
                            String name = URLDecoder.decode(pair.substring(0, idx), "utf8");
                            String value = URLDecoder.decode(pair.substring(idx + 1), "utf8");
                            if (getDesensitizeParameter().contains(name)) {
                                value = DesensitizeUtil.desensitize(value);
                            }
                            temp += (name + "=" + value);
                        }
                        temp += "&";
                    }
                } else {
                    temp = EntityUtils.toString(((HttpPost) httpRequestBase).getEntity());
                    temp = URLDecoder.decode(temp, "UTF8");
                }
            } catch (IOException e) {
                log.error("getStringExpress error", e);
            }
            result = result + temp;
        }
        return result.replaceAll("[\r\n]", "").replaceAll("password=.*&", "password=XXXXXXX&");
    }


}
