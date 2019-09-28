package com.company.project.action;

import com.google.common.collect.Maps;

import com.company.project.context.Context;
import com.company.project.exception.ExecuteInterruptException;
import com.company.project.utils.TimeUtils;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 * 重试抽象类
 */
@Slf4j
public abstract class AbstractRetryableAction implements Action {

    protected final int MAX_RETRY_ATTEMPTS = 3;
    private final int DEFAULT_SLEEP_MILLIS = 300;

    @Override
    public boolean execute(Context context) throws Exception {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(getRetryPolicy());
        retryTemplate.setBackOffPolicy(getBackoffPolicy());
        //执行前沉睡 模仿用户行为
        TimeUtils.sleepTime(sleepBeforeExecute(context));
        RetryCallback<Boolean, Exception> retryCallback = retryContext -> {

            try {
                onStart(context);
                boolean result = doInRetry(context);
                if (async()) {
                    return result;
                }
                onComplete(context);
                return result;
            } catch (ExecuteInterruptException e) {
                onFailed(context, e);
                throw e;
            } catch (Exception e) {
                boolean ignore = onError(context, e);
                if (ignore) {
                    log.info("Ignore error on exception", e);
                    return true;
                }
                throw e;
            } finally {
                release(context);
            }
        };
        return retryTemplate.execute(retryCallback, retryContext -> doRecover(context, retryContext.getLastThrowable()));

    }

    protected RetryPolicy getRetryPolicy() {
        Map<Class<? extends Throwable>, Boolean> map = Maps.newHashMap();
        map.put(ExecuteInterruptException.class, false);
        map.put(Exception.class, true);
        return new SimpleRetryPolicy(getMaxRetryAttempts(), map, true);
    }

    protected BackOffPolicy getBackoffPolicy() {
        FixedBackOffPolicy policy = new FixedBackOffPolicy();
        policy.setBackOffPeriod(50);
        return policy;
    }

    protected int getMaxRetryAttempts() {
        return MAX_RETRY_ATTEMPTS;
    }

    protected boolean doRecover(Context context, Throwable e) throws Exception {
        if (e instanceof Exception) {
            throw ((Exception) e);
        } else {
            throw new RuntimeException(e);
        }
    }

    protected int sleepBeforeExecute(Context context) {
        return DEFAULT_SLEEP_MILLIS;
    }

    /**
     * 重试操作
     * @param context 执行上下文
     * @return
     * @throws Exception 重试执行异常
     */
    public abstract boolean doInRetry(Context context)  throws Exception;

    /**
     * 系统异常处理
     * @param context 执行上下文
     * @param e
     * @return
     */
    public abstract boolean onError(Context context, Exception e);

    /**
     * 释放操作
     * @param context 执行上下文
     */
    public abstract void release(Context context);

    /**
     * 重试前操作
     * @param context 执行上下文
     */
    @Override
    public abstract void onStart(Context context);

    /**
     * 中断异常处理
     * @param context 执行上下文
     * @param e
     */
    @Override
    public abstract void onFailed(Context context, Exception e);

    /**
     * 请求完成执行操作
     * @param context 执行上下文
     */
    @Override
    public abstract void onComplete(Context context);


}
