package re.impl;

import re.Rule;

public class PrintAction implements Rule {

    private final String value;

    public PrintAction(String value) {
        this.value = value;
    }

    public boolean execute() {
        System.out.println(value);
        return true;
    }

}
