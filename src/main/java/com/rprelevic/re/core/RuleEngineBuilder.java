package com.rprelevic.re.core;

import com.rprelevic.re.api.IRuleId;

public class RuleEngineBuilder {

    public static final int CHILD_TRUE_ORDER = 0;
    public static final int CHILD_FALSE_ORDER = 1;

    private static final int MAX_LONG_PATH = 200;

    private final int[][] cfg = new int[MAX_LONG_PATH][];

    private final RuleRegistry ruleRegistry;

    public RuleEngineBuilder() {
        this.ruleRegistry = new RuleRegistry();
    }

    public RuleEngineBuilder(RuleRegistry ruleRegistry) {
        this.ruleRegistry = ruleRegistry;
    }

    public RuleEngineBuilder start(IRuleId firstNode) {
        int[] child = new int[2];
        child[CHILD_TRUE_ORDER] = firstNode.value(); // first step to be ran
        cfg[IRuleId.start()] = child;

        return this;
    }

    public RuleEngineBuilder add(IRuleId id, IRuleId trueId, IRuleId falseId) {
        int[] child = new int[2];
        child[CHILD_TRUE_ORDER] = trueId.value(); // Next step if true
        child[CHILD_FALSE_ORDER] = falseId.value(); // Next step if false
        cfg[id.value()] = child;

        return this;
    }

    public RuleEngineBuilder addOnlyTrue(IRuleId id, IRuleId trueId) {
        int[] child = new int[2];
        child[CHILD_TRUE_ORDER] = trueId.value(); // Next step if true
        cfg[id.value()] = child;

        return this;
    }

    public RuleEngineBuilder addOnlyFalse(IRuleId id, IRuleId falseId) {
        int[] child = new int[2];
        child[CHILD_FALSE_ORDER] = falseId.value(); // Next step if false
        cfg[id.value()] = child;

        return this;
    }

    public RuleEngine build() {
        return new RuleEngine(ruleRegistry, cfg);
    }

}