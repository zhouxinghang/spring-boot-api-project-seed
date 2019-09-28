package com.company.project.enums;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
public enum  BizTypeEnum {
    /**
     * 测试业务类型
     */
    TEST("test"),
    LOGIN("login"),
    QUERY_BAIDU("queryBaidu"),

    ;

    private String name;

    BizTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
