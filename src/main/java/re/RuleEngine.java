package re;

import jdk.swing.interop.SwingInterOpUtils;

import java.util.List;

import static re.OperationType.MANDATORY;
import static re.OperationType.TERMINAL;
import static re.RuleOperator.OR;
import static re.RuleType.CONDITION;

public class RuleEngine {

    private final List<RuleEngineBuilder.RuleWrapper> rules;

    public RuleEngine(List<RuleEngineBuilder.RuleWrapper> rules) {
        this.rules = rules;
    }

    public ExecutionRs run() {
        boolean precon = false;
        ExecutionRs executionRs = new ExecutionRs();
        for (RuleEngineBuilder.RuleWrapper rootWrapper : rules) {
            RuleEngineBuilder.RuleWrapper r = rootWrapper;
            precon = r.rule().execute().getResult();
            //System.out.println(r.rule().getInfo());
            while (r.nextRule() != null) { // Loop through child rules
                r = r.nextRule();
                if (CONDITION.equals(r.type())) { // Handle boolean conditions
                    if (RuleOperator.AND.equals(r.operator())) {
                        precon = precon & r.rule().execute().getResult();
                    } else if (OR.equals(r.operator())) {
                        precon = precon || r.rule().execute().getResult();
                    } else {
                        precon = r.rule().execute().getResult(); // New set of conditions about to happen
                    }
                    //System.out.println(r.rule().getInfo());
                } else {
                    if (precon) {
                        RuleRs rs = r.rule().execute(); // If precondition is true, execute action
                        precon = rs.getResult();
                        executionRs.addMessage(rs);
                        if (TERMINAL.equals(r.operationType())) { // If operation is terminal, finish pipeline with it
                            executionRs.setExecSuccess(true);
                            return executionRs;
                        }
                    } else {
                        // If this piece of pipeline is mandatory, whole pipeline breaks with it
                        if (MANDATORY.equals(rootWrapper.operationType())) {
                            executionRs.setExecSuccess(false);
                            return executionRs;
                        }
                        break; // If preconditions are false, there is no need to move on with rule segment
                    }
                }
            }
        }
        executionRs.setExecSuccess(precon);
        return executionRs;
    }
}
