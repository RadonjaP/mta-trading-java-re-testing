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
    public static final Rule COND_HAS_2ND_CFRM_SIGNAL = new SimpleCondition(true, "Has 2nd confirmation signal");
    public static final Rule COND_HAS_NO_2ND_CFRM_SIGNAL = new SimpleCondition(false, "Has 2nd confirmation signal");
    public static final Rule COND_BASELINE_MATCH_TREND = new SimpleCondition(true, "Baseline Mathes Trend?");
    public static final Rule COND_BASELINE_DOES_NOT_MATCH_TREND = new SimpleCondition(false, "Baseline Mathes Trend?");
    public static final Rule COND_IS_CONTINUATION_TRADE = new SimpleCondition(true, "Is Continuation Trade?");
    public static final Rule COND_IS_NOT_CONTINUATION_TRADE = new SimpleCondition(false, "Is Continuation Trade?");
    public static final Rule COND_VOLUME_EXISTS = new SimpleCondition(true, "Volume Exists?");
    public static final Rule COND_VOLUME_DOES_NOT_EXISTS = new SimpleCondition(false, "Volume Exists?");
    public static final Rule COND_PRICE_TO_BASELINE_LTHEN_ATR = new SimpleCondition(true, "Price Distance to Baseline is lower than ATR?");
    public static final Rule COND_PRICE_TO_BASELINE_NOT_LTHEN_ATR = new SimpleCondition(false, "Price Distance to Baseline is lower than ATR?");
    public static final Rule COND_BASELINE_IS_CROSSED = new SimpleCondition(true, "Baseline is Crossed?");
    public static final Rule COND_BASELINE_IS_NOT_CROSSED = new SimpleCondition(false, "Baseline is Crossed?");
    public static final Rule COND_SIGNAL_IS_PULLBACK = new SimpleCondition(true, "Signal is Poolback?");
    public static final Rule COND_SIGNAL_IS_NOT_PULLBACK = new SimpleCondition(false, "Signal is Poolback?");

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
    public static final Rule ACTION_ADD_PENDING_TRADE = new SimpleAction("Add Pending Trade");
    
    public static final Rule ACTION_MOCK_SIMPLE = new SimpleAction("Simple Print Action");
    public static final Rule ACTION_TERMINATE = new SimpleAction("Simple Terminate Action");


}
