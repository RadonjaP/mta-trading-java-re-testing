package re;

import java.util.ArrayList;
import java.util.List;

import static re.OperationType.MANDATORY;
import static re.RuleOperator.OR;
import static re.RuleType.CONDITION;

public class ExecutionRs {

    private final List<String> messages = new ArrayList<String>();

    private boolean execSuccess;

    public void addMessage(RuleRs ruleRs) {
        messages.add(ruleRs.getInfo());
    }

    public List<String> getMessages() {
        return messages;
    }

    public boolean isExecSuccess() {
        return execSuccess;
    }

    public void setExecSuccess(boolean execSuccess) {
        this.execSuccess = execSuccess;
    }
}
