package com.company.project.action;

import com.company.project.context.Context;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 * 执行接口
 */
public interface Executable {
    /**
     * 方法执行逻辑
     * @param context 执行上下文
     * @return  是否成功
     * @throws Exception 执行业务异常
     */
    boolean execute(Context context) throws Exception;
}
