package re;

import java.util.ArrayList;
import java.util.List;

import static re.OperationType.MANDATORY;
import static re.OperationType.NON_TERMINAL;
import static re.RuleOperator.AND;
import static re.RuleOperator.OR;
import static re.RuleType.ACTION;
import static re.RuleType.CONDITION;

public class RuleEngineBuilder {

    private final List<RuleWrapper> rules = new ArrayList<RuleWrapper>();

    private RuleWrapper lastRuleInChain;

    public RuleEngineBuilder when(Rule r) {
        RuleWrapper ruleWrapper = new RuleWrapper(r, CONDITION, lastRuleInChain, null, MANDATORY);
        if (lastRuleInChain == null)
            rules.add(ruleWrapper);
        lastRuleInChain = ruleWrapper;
        return this;
    }

    public RuleEngineBuilder optional(Rule r) {
        RuleWrapper ruleWrapper = new RuleWrapper(r, CONDITION, lastRuleInChain, null, NON_TERMINAL);
        if (lastRuleInChain == null)
            rules.add(ruleWrapper);
        else
            lastRuleInChain.nextRule(ruleWrapper);
        lastRuleInChain = ruleWrapper;
        return this;
    }

    public RuleEngineBuilder and(Rule r) {
        RuleWrapper ruleWrapper = new RuleWrapper(r, CONDITION, lastRuleInChain, AND, NON_TERMINAL);
        lastRuleInChain.nextRule(ruleWrapper);
        lastRuleInChain = ruleWrapper;
        return this;
    }

    public RuleEngineBuilder or(Rule r) {
        RuleWrapper ruleWrapper = new RuleWrapper(r, CONDITION, lastRuleInChain, OR, NON_TERMINAL);
        lastRuleInChain.nextRule(ruleWrapper);
        lastRuleInChain = ruleWrapper;
        return this;
    }

    public RuleEngineBuilder then(Rule a) {
        RuleWrapper ruleWrapper = new RuleWrapper(a, ACTION, lastRuleInChain, RuleOperator.ACTION, NON_TERMINAL);
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

    public RuleEngine build() {
        return new RuleEngine(rules);
    }

    static class RuleWrapper {

        private final RuleWrapper previousRule;
        private RuleWrapper nextRule;
        private final Rule rule;
        private final RuleType type;
        private final RuleOperator operator;
        private final OperationType operationType;

        RuleWrapper(Rule rule, RuleType type, RuleWrapper previousRule,
                    RuleOperator operator, OperationType operationType) {
            this.previousRule = previousRule;
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
