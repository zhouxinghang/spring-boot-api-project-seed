package com.company.project.service.impl;

import com.company.project.context.Context;
import com.company.project.enums.BizTypeEnum;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
@Slf4j
@Service
public class QueryBaiduService {


    public void query() {

    }

    private Context initContext() {
        Context context = new Context();
        context.bizTypeEnum = BizTypeEnum.QUERY_BAIDU;
        return context;
    }
}
