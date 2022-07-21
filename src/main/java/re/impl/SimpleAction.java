package re.impl;

import re.Rule;
import re.RuleRs;

public class SimpleAction implements Rule {

    private String info;

    public SimpleAction(String info) {
        this.info = info;
    }

    public RuleRs execute() {
        System.out.println(info);
        return new RuleRs(info, true);
    }

    public String getInfo() {
        return info;
    }

}
