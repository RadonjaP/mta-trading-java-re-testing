package re.impl;

import re.Rule;

/**
 * Only for test purposes
 */
public class ConditionsAndRules {

    /**
     * Conditions
     */
    public static final Rule COND_HAVE_ACTIVE_TRADE = new SimpleCondition(true, "Have Active Trade W/L?");
    public static final Rule COND_HAVE_NO_ACTIVE_TRADE = new SimpleCondition(false, "Have Active Trade W/L?");
    public static final Rule COND_PRICE_HIT_SL = new SimpleCondition(true, "Price Hit SL?");
    public static final Rule COND_PRICE_HIT_TP = new SimpleCondition(true, "Price Hit TP?");
    public static final Rule COND_PRICE_DID_NOT_HIT_SL = new SimpleCondition(false, "Price Hit SL?");
    public static final Rule COND_PRICE_DID_NOT_HIT_TP = new SimpleCondition(false, "Price Hit TP?");
    public static final Rule COND_HAS_CFRM_SIGNAL = new SimpleCondition(true, "Signal from Main Indicator?");
    public static final Rule COND_HAS_NO_CFRM_SIGNAL = new SimpleCondition(false, "Signal from Main Indicator?");
    public static final Rule COND_HAVE_ACTIVE_TRADE_CHALK = new SimpleCondition(true, "Have Active Trade (chalk)?");
    public static final Rule COND_HAVE_NO_ACTIVE_TRADE_CHALK = new SimpleCondition(false, "Have Active Trade (chalk)?");
    public static final Rule COND_GOT_EXIT_SIGNAL = new SimpleCondition(true, "Exit signal exists?");
    public static final Rule COND_DID_NOT_GET_EXIT_SIGNAL = new SimpleCondition(false, "Exit signal exists?");

    public static final Rule COND_MOCK_TRUE = new SimpleCondition(true, "Mock Condition?");
    public static final Rule COND_MOCK_FALSE = new SimpleCondition(false, "Mock Condition?");

    /**
     * Actions
     */
    public static final Rule ACTION_MANAGE_TRADE = new SimpleAction("Manage Trade");
    public static final Rule ACTION_CLOSE_TRADE_WL = new SimpleAction("Basic Close Trade (win/lose)");
    public static final Rule ACTION_CLOSE_TRADE_CHALK = new SimpleAction("Basic Close Trade (chalk)");
    public static final Rule ACTION_PRE_OPEN_TRADE_STEP = new SimpleAction("Basic Pre-Open Trade Step");
    public static final Rule ACTION_OPEN_TRADE = new SimpleAction("Open New Trade");

    public static final Rule ACTION_MOCK_SIMPLE = new SimpleAction("Simple Print Action");
    
}
