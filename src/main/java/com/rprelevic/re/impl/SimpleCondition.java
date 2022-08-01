package com.rprelevic.re.impl;

import com.rprelevic.re.api.IRule;
import com.rprelevic.re.model.RuleRs;

public class SimpleCondition implements IRule {

    private final boolean value;

    private final String info;

    public SimpleCondition(boolean value, String info) {
        this.value = value;
        this.info = info;
    }

    public RuleRs execute() {
        return new RuleRs(info, value);
    }

    public String getInfo() {
        return info;
    }
}