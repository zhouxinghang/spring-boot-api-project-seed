package com.company.project.action;

import com.google.common.base.Stopwatch;

import com.company.project.context.Context;
import com.company.project.exception.ExecuteInterruptException;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 * 监控重试抽象类
 */
@Slf4j
public abstract class AbstractMonitoredAction extends AbstractRetryableAction {
    private Stopwatch stopwatch;

    @Override
    public boolean onError(Context context, Exception e) {
        log.warn("{}, Action [{},{}] execute error. Message: {}", context.bizTypeEnum, context.key, getClass().getSimpleName(), e.getMessage(), e);
        return false;
    }

    @Override
    public void onStart(Context context) {
        log.info("{}, Action [{},{}] execute start",context.bizTypeEnum, context.key, getClass().getSimpleName());
        stopwatch = Stopwatch.createStarted();

    }

    @Override
    public void onFailed(Context context, Exception e) {
        if (e instanceof ExecuteInterruptException) {
            log.warn("{}, Action [{},{}] execute error. Message: {}", context.bizTypeEnum, context.key, getClass().getSimpleName(), e.getMessage(), e);
        } else {
            log.error("{}, Action [{},{}] execute error. Message: {}", context.bizTypeEnum, context.key, getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    @Override
    public void release(Context context) {
        log.info("{}, Action [{},{}] execute complete cost: {}millis",context.bizTypeEnum, context.key, getClass().getSimpleName(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
}
