package re;

import java.util.List;

import static re.OperationType.MANDATORY;
import static re.RuleOperator.OR;
import static re.RuleType.CONDITION;

public class RuleEngine {

    private final List<RuleEngineBuilder.RuleWrapper> rules;

    public RuleEngine(List<RuleEngineBuilder.RuleWrapper> rules) {
        this.rules = rules;
    }

    public boolean run() {
        boolean precon = false;
        for (RuleEngineBuilder.RuleWrapper rootWrapper : rules) {
            RuleEngineBuilder.RuleWrapper r = rootWrapper;
            precon = r.rule().execute();
            while (r.nextRule() != null) { // Loop through child rules
                r = r.nextRule();
                if (CONDITION.equals(r.type())) { // Handle boolean conditions
                    if (RuleOperator.AND.equals(r.operator())) {
                        precon = precon & r.rule().execute();
                    } else if (OR.equals(r.operator())) {
                        precon = precon || r.rule().execute();
                    } else {
                        precon = r.rule().execute(); // New set of conditions about to happen
                    }
                } else {
                    if (precon) {
                        r.rule().execute(); // If precondition is true, execute action
                    } else {
                        if (MANDATORY.equals(rootWrapper.operationType()))
                            return false; // If this piece of pipeline is mandatory, whole pipeline breaks with it
                        break; // If preconditions are false, there is no need to move on with rule segment
                    }
                }
            }
        }

        return precon;
    }
}
