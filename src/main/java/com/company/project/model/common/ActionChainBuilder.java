package com.company.project.model.common;

import com.google.common.collect.Lists;

import com.company.project.action.Action;

import java.util.Collections;
import java.util.List;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
public class ActionChainBuilder {
    private List<Action> actionChain = Lists.newArrayList();

    public ActionChainBuilder(){

    }

    public static ActionChainBuilder build(){
        return new ActionChainBuilder();
    }

    public ActionChainBuilder addAction(Action action){
        this.actionChain.add(action);
        return this;
    }

    public List<Action> getActionChain(){
        return Collections.unmodifiableList(this.actionChain);
    }
}
