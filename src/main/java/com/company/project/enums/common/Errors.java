package com.company.project.enums.common;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
public enum  Errors {
    /**
     * 成功
     */
    SUCCESS("成功"),
    SYSTEM_ERROR("系统错误"),
    REQUEST_TIMEOUT("请求超时"),
    SYSTEM_BUSY("系统繁忙"),


    /**
     * 请求接口http状态码
     */
    HTTP_30X("30X错误"),
    HTTP_40X("40X错误"),
    HTTP_UNKNOWN("不属于[200-500)状态码"),

    UNKNOWN_EXCEPTION("未知异常"),

    ;

    public final String msg;

    Errors(String msg) {
        this.msg = msg;
    }
}
