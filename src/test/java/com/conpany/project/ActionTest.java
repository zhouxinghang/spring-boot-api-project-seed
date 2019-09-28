package com.conpany.project;

import com.company.project.context.Context;
import com.company.project.enums.BizTypeEnum;
import com.company.project.service.impl.ActionService;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
@Slf4j
public class ActionTest extends Tester {
    @Autowired
    private ActionService actionService;

    @Test
    public void testQueryBaidu() {
        log.info("begin====>");
        try {
            Context context = new Context();
            context.bizTypeEnum = BizTypeEnum.QUERY_BAIDU;
            context.httpClient = HttpClientBuilder.create().build();
            context.target = new HttpHost("www.baidu.com",80);
            actionService.queryBaidu(context);
        } catch (Exception e) {
            log.error("error",e);
        }
    }
}
