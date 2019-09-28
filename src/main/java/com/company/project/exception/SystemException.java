package com.company.project.exception;

import com.company.project.enums.common.Errors;

import java.io.IOException;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 *
 * 执行流程中的系统异常
 */
public class SystemException extends IOException {

    private Errors err;

    public SystemException(String msg) {
        this(Errors.UNKNOWN_EXCEPTION, msg);
    }

    public SystemException(Errors err, String message, Object... args) {
        super(err.name() + ":" + String.format(message.replace("\n", ""), args));
        this.err = err;
    }

    public Errors getErrors() {
        return err;
    }
}
