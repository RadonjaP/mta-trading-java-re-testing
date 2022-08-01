package com.rprelevic.re.api;

import com.rprelevic.re.model.RuleRs;

public interface IRule {

    RuleRs execute();

    String getInfo();

}