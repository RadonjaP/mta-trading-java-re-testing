package com.rprelevic.re.core;

import com.rprelevic.re.api.IRuleEngine;
import com.rprelevic.re.api.IRuleId;
import com.rprelevic.re.model.ExecutionRs;
import com.rprelevic.re.model.RuleRs;

public class RuleEngine implements IRuleEngine {

    private final RuleRegistry ruleRegistry;

    private final int[][] graphCfg;

    public RuleEngine(RuleRegistry ruleRegistry, int[][] graphCfg) {
        this.ruleRegistry = ruleRegistry;
        this.graphCfg = graphCfg;
    }

    @Override
    public ExecutionRs run() {
        ExecutionRs response = new ExecutionRs();
        response.setExecSuccess(false);

        int startingRule = graphCfg[IRuleId.start()][0];
        move(startingRule, response);

        response.setExecSuccess(true);
        return response;
    }

    private void move(final int rule, final ExecutionRs response) {
        RuleRs result = ruleRegistry.getRule(rule).execute(); // Get rule from registry and execute
        response.addMessage(result);
        if (graphCfg[rule] == null) // Rule is terminal. Leaf of graph
            return;

        int nextRule;
        if (result.getResult())
            nextRule = graphCfg[rule][RuleEngineBuilder.CHILD_TRUE_ORDER];
        else
            nextRule = graphCfg[rule][RuleEngineBuilder.CHILD_FALSE_ORDER];
        if (nextRule != 0) // Rule has child path
            move(nextRule, response);
    }
}