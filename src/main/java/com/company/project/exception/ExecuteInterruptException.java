package com.company.project.exception;

import com.company.project.enums.common.Errors;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
public class ExecuteInterruptException extends RuntimeException {
    public final Errors err;

    public ExecuteInterruptException(String message) {
        this(Errors.SYSTEM_ERROR, message);
    }

    public ExecuteInterruptException(Errors err){
        super(err.msg);
        this.err = err;
    }

    public ExecuteInterruptException(Errors err, String message, Object... args) {
        super(err.name() + ":" + String.format(message.replace("\n", ""), args));
        this.err = err;
    }

    public ExecuteInterruptException(String message, Throwable cause) {
        super(message, cause);
        err = Errors.SYSTEM_ERROR;
    }
}
