package re.impl;

import re.Rule;
import re.RuleRs;

public class SimpleCondition implements Rule {

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
