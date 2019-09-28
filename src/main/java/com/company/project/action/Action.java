package com.company.project.action;

import com.company.project.context.Context;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 * Action 执行接口
 */
public interface Action extends Executable {

    /**
     * 请求开始前执行操作
     * @param context
     */
    void onStart(Context context);

    /**
     * 请求失败执行操作
     * @param context
     * @param e
     */
    void onFailed(Context context, Exception e);

    /**
     * 请求完成执行操作
     * @param context
     */
    void onComplete(Context context);

    /**
     * 请求是否异步
     * @return
     */
    default boolean async() {
        return false;
    }
}
