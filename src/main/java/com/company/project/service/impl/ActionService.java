package com.company.project.service.impl;

import com.google.common.collect.Lists;

import com.company.project.action.Action;
import com.company.project.action.impl.QueryBaiduAction;
import com.company.project.context.Context;
import com.company.project.model.common.ActionChainBuilder;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
@Service
public class ActionService {

    private void actionChainExecution(List<Action> actions, Context context) throws Exception {
        for (Action action : actions) {
            boolean continueFlag = context.ignoreActions.stream().anyMatch(ignoreAction -> action.getClass().equals(ignoreAction));
            if (!continueFlag) {
                action.execute(context);
            }
        }
    }

    public void queryBaidu(Context context) throws Exception {
        List<Action> actionChain = ActionChainBuilder.build()
            .addAction(new QueryBaiduAction())
            .getActionChain();
        actionChainExecution(actionChain, context);
    }
}
