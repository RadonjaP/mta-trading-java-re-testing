package com.rprelevic.re.core;

import com.rprelevic.re.api.IRule;
import com.rprelevic.re.impl.SimpleAction;
import com.rprelevic.re.impl.SimpleCondition;

/**
 * Only for test purposes
 */
public final class ConditionsAndRules {

    /**
     * Conditions
     */
    public static final IRule COND_HAVE_ACTIVE_TRADE = new SimpleCondition(true, "Have Active Trade W/L?");
    public static final IRule COND_HAVE_NO_ACTIVE_TRADE = new SimpleCondition(false, "Has no Active Trade W/L?");
    public static final IRule COND_PRICE_HIT_SL = new SimpleCondition(true, "Price Hit SL?");
    public static final IRule COND_PRICE_HIT_TP = new SimpleCondition(true, "Price Hit TP?");
    public static final IRule COND_PRICE_HIT_TS = new SimpleCondition(true, "Price Hit Trailing Stop?");
    public static final IRule COND_PRICE_DID_NOT_HIT_SL = new SimpleCondition(false, "Price Did Not Hit SL?");
    public static final IRule COND_PRICE_DID_NOT_HIT_TP = new SimpleCondition(false, "Price Did not Hit TP?");
    public static final IRule COND_PRICE_DID_NOT_HIT_TS = new SimpleCondition(false, "Price Did Not Hit Trailing Stop?");
    public static final IRule COND_HAS_CFRM_SIGNAL = new SimpleCondition(true, "Signal from Main Indicator?");
    public static final IRule COND_HAS_NO_CFRM_SIGNAL = new SimpleCondition(false, "No Signal from Main Indicator?");
    public static final IRule COND_HAVE_NO_ACTIVE_TRADE_CHALK = new SimpleCondition(false, "Has no Active Trade (chalk)?");
    public static final IRule COND_HAVE_ACTIVE_TRADE_CHALK = new SimpleCondition(true, "Has Active Trade (chalk)?");
    public static final IRule COND_GOT_EXIT_SIGNAL = new SimpleCondition(true, "Exit signal exists?");
    public static final IRule COND_DID_NOT_GET_EXIT_SIGNAL = new SimpleCondition(false, "Exit signal does not exist?");
    public static final IRule COND_HAS_2ND_CFRM_SIGNAL = new SimpleCondition(true, "Has 2nd confirmation signal");
    public static final IRule COND_HAS_NO_2ND_CFRM_SIGNAL = new SimpleCondition(false, "Has no 2nd confirmation signal");
    public static final IRule COND_BASELINE_MATCH_TREND = new SimpleCondition(true, "Baseline Matches Trend?");
    public static final IRule COND_BASELINE_DOES_NOT_MATCH_TREND = new SimpleCondition(false, "Baseline does not Match Trend?");
    public static final IRule COND_IS_CONTINUATION_TRADE = new SimpleCondition(true, "Is Continuation Trade?");
    public static final IRule COND_IS_NOT_CONTINUATION_TRADE = new SimpleCondition(false, "Is Not Continuation Trade?");
    public static final IRule COND_VOLUME_EXISTS = new SimpleCondition(true, "Volume Exists?");
    public static final IRule COND_VOLUME_DOES_NOT_EXIST = new SimpleCondition(false, "Volume Does not Exists?");
    public static final IRule COND_PRICE_TO_BASELINE_LTHEN_ATR = new SimpleCondition(true, "Price Distance to Baseline is lower than ATR?");
    public static final IRule COND_PRICE_TO_BASELINE_NOT_LTHEN_ATR = new SimpleCondition(false, "Price Distance to Baseline is greater than ATR?");
    public static final IRule COND_BASELINE_IS_CROSSED = new SimpleCondition(true, "Baseline is Crossed?");
    public static final IRule COND_BASELINE_IS_NOT_CROSSED = new SimpleCondition(false, "Baseline is Not Crossed?");
    public static final IRule COND_SIGNAL_IS_PULLBACK = new SimpleCondition(true, "Signal is Poolback?");
    public static final IRule COND_SIGNAL_IS_NOT_PULLBACK = new SimpleCondition(false, "Signal is Not Poolback?");
    public static final IRule COND_CFRM_OPPOSITE_SIGNAL = new SimpleCondition(true, "Confirmation Indicator gave opposite signal?");
    public static final IRule COND_CFRM_NOT_OPPOSITE_SIGNAL = new SimpleCondition(false, "Confirmation Indicator did not give opposite signal?");
    public static final IRule COND_CFRM_MATCHES_BL_SIGNAL = new SimpleCondition(true, "Confirmation Indicator matches baseline signal?");
    public static final IRule COND_CFRM_NOT_MATCHES_BL_SIGNAL = new SimpleCondition(false, "Confirmation Indicator does not match baseline signal?");

    /**
     * Actions
     */
    public static final IRule ACTION_CLOSE_TRADE_W = new SimpleAction("Basic Close Trade Win");
    public static final IRule ACTION_CLOSE_TRADE_L = new SimpleAction("Basic Close Trade Lose");
    public static final IRule ACTION_CLOSE_TRADE_CHALK = new SimpleAction("Basic Close Trade (chalk)");
    public static final IRule ACTION_CLOSE_TRADE = new SimpleAction("Close Trade");
    public static final IRule ACTION_OPEN_TRADE = new SimpleAction("Open New Trade");
    public static final IRule ACTION_ADD_PENDING_TRADE = new SimpleAction("Add Pending Trade");
    public static final IRule ACTION_MOVE_TRAILING_STOP = new SimpleAction("Move Trailing Stop");
    public static final IRule ACTION_MOVE_TO_BREAK_EVEN = new SimpleAction("Move To Break Even");
    public static final IRule ACTION_TERMINATE = new SimpleAction("Simple Terminate Action");

}