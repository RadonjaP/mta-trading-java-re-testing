package re;

import java.util.ArrayList;
import java.util.List;

import static re.OperationType.*;
import static re.RuleOperator.AND;
import static re.RuleOperator.OR;
import static re.RuleType.ACTION;
import static re.RuleType.CONDITION;

public class RuleEngineBuilder {

    private final List<RuleWrapper> rules = new ArrayList<RuleWrapper>();

    private RuleWrapper lastRuleInChain;

    public RuleEngineBuilder when(Rule r) {
        RuleWrapper ruleWrapper = new RuleWrapper(r, CONDITION, null, MANDATORY);
        if (lastRuleInChain == null)
            rules.add(ruleWrapper);
        lastRuleInChain = ruleWrapper;
        return this;
    }

    public RuleEngineBuilder optional(Rule r) {
        RuleWrapper ruleWrapper = new RuleWrapper(r, CONDITION, null, NON_TERMINAL);
        if (lastRuleInChain == null)
            rules.add(ruleWrapper);
        else
            lastRuleInChain.nextRule(ruleWrapper);
        lastRuleInChain = ruleWrapper;
        return this;
    }
    public RuleEngineBuilder unrestricted(Rule a) {
        optional(new SimpleTruePrecondition());
        RuleWrapper ruleWrapper = new RuleWrapper(a, ACTION, RuleOperator.ACTION, NON_TERMINAL);
        if (lastRuleInChain == null) {
            lastRuleInChain = ruleWrapper;
        } else {
            lastRuleInChain.nextRule(ruleWrapper);
        }
        lastRuleInChain = ruleWrapper;
        return this;
    }


    public RuleEngineBuilder and(Rule r) {
        RuleWrapper ruleWrapper = new RuleWrapper(r, CONDITION, AND, NON_TERMINAL);
        lastRuleInChain.nextRule(ruleWrapper);
        lastRuleInChain = ruleWrapper;
        return this;
    }

    public RuleEngineBuilder or(Rule r) {
        RuleWrapper ruleWrapper = new RuleWrapper(r, CONDITION, OR, NON_TERMINAL);
        lastRuleInChain.nextRule(ruleWrapper);
        lastRuleInChain = ruleWrapper;
        return this;
    }

    public RuleEngineBuilder then(Rule a) {
        RuleWrapper ruleWrapper = new RuleWrapper(a, ACTION, RuleOperator.ACTION, NON_TERMINAL);
        if (lastRuleInChain == null) {
            lastRuleInChain = ruleWrapper;
        } else {
            lastRuleInChain.nextRule(ruleWrapper);
        }
        lastRuleInChain = ruleWrapper;
        return this;
    }

    public RuleEngineBuilder link() {
        lastRuleInChain.nextRule(null); // Terminal operation has no next rule
        lastRuleInChain = null;
        return this;
    }

    public RuleEngineBuilder terminate(Rule a) {
        RuleWrapper ruleWrapper = new RuleWrapper(a, ACTION, RuleOperator.ACTION, TERMINAL);
        if (lastRuleInChain == null) {
            lastRuleInChain = ruleWrapper;
        } else {
            lastRuleInChain.nextRule(ruleWrapper);
        }
        lastRuleInChain = ruleWrapper;
        return this;
    }

    public RuleEngine build() {
        return new RuleEngine(rules);
    }

    static class SimpleTruePrecondition implements Rule {
        public RuleRs execute() { return new RuleRs("Default Precondition for execute()", true); }
        public String getInfo() { return "Default Precondition"; }
    }

    static class TerminatePostCondition implements Rule {
        public RuleRs execute() { return new RuleRs("Terminating execution", false); }
        public String getInfo() { return "Default Precondition"; }
    }

    static class RuleWrapper {

        private RuleWrapper nextRule;
        private final Rule rule;
        private final RuleType type;
        private final RuleOperator operator;
        private final OperationType operationType;

        RuleWrapper(Rule rule, RuleType type, RuleOperator operator, OperationType operationType) {
            this.rule = rule;
            this.type = type;
            this.operator = operator;
            this.operationType = operationType;
        }

        Rule rule() {return this.rule;}

        RuleType type() {return this.type;}

        RuleOperator operator() {return this.operator;}

        OperationType operationType() {return this.operationType;}

        void nextRule(RuleWrapper nextRule) {this.nextRule = nextRule;}

        RuleWrapper nextRule() {return this.nextRule;}

    }
}
