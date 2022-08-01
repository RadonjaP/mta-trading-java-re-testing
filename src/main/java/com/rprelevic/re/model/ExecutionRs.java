package com.rprelevic.re.model;

import java.util.ArrayList;
import java.util.List;

public class ExecutionRs {

    private final List<String> log = new ArrayList<>();

    private boolean execSuccess;

    public void addMessage(RuleRs ruleRs) {
        log.add(ruleRs.getInfo());
    }

    public List<String> getLog() {
        return log;
    }

    public boolean isExecSuccess() {
        return execSuccess;
    }

    public void setExecSuccess(boolean execSuccess) {
        this.execSuccess = execSuccess;
    }
}