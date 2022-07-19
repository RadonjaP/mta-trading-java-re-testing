package re.impl;

import re.Rule;

public class BasicCondition implements Rule {

    private final boolean value;

    public BasicCondition(boolean value) {
        this.value = value;
    }

    public boolean execute() {
        return value;
    }
}
