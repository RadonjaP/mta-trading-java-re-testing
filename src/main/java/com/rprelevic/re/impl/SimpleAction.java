package com.rprelevic.re.impl;

import com.rprelevic.re.api.IRule;
import com.rprelevic.re.model.RuleRs;

public class SimpleAction implements IRule {

    private String info;

    public SimpleAction(String info) {
        this.info = info;
    }

    public RuleRs execute() {
        System.out.println(info);
        return new RuleRs(info, true);
    }

    public String getInfo() {
        return info;
    }

}