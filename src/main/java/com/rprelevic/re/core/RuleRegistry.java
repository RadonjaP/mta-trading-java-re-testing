package com.rprelevic.re.core;

import com.rprelevic.re.api.IRule;

import java.util.*;

import static com.rprelevic.re.core.ConditionsAndRules.*;
import static com.rprelevic.re.model.RuleId.*;

public class RuleRegistry {

    protected static final Map<Integer, IRule> RULES = new HashMap<>();

    static {
        // Conditions
        RULES.put(SIGNAL_IS_PULLBACK.value(), COND_SIGNAL_IS_PULLBACK);
        RULES.put(HAS_CFRM_SIGNAL.value(), COND_HAS_CFRM_SIGNAL);
        RULES.put(HAS_2ND_CFRM_SIGNAL.value(), COND_HAS_2ND_CFRM_SIGNAL);
        RULES.put(BASELINE_MATCH_TREND.value(), COND_BASELINE_MATCH_TREND);
        RULES.put(CONTINUATION_TRADE.value(), COND_IS_CONTINUATION_TRADE);
        RULES.put(VOLUME_EXISTS.value(), COND_VOLUME_EXISTS);
        RULES.put(PRICE_TO_BASELINE_LTHAN_ATR.value(), COND_PRICE_TO_BASELINE_LTHEN_ATR);
        RULES.put(BASELINE_CROSSED.value(), COND_BASELINE_IS_CROSSED);
        RULES.put(ACTIVE_TRADE_EXISTS.value(), COND_HAVE_ACTIVE_TRADE);
        RULES.put(ACTIVE_TRADE_TO_CHALK_EXISTS.value(), COND_HAVE_ACTIVE_TRADE_CHALK);
        RULES.put(STOP_LOSS_HIT.value(), COND_PRICE_HIT_SL);
        RULES.put(TAKE_PROFIT_HIT.value(), COND_PRICE_HIT_TP);
        RULES.put(TRAILING_STOP_HIT.value(), COND_PRICE_HIT_TS);
        RULES.put(EXIT_SIGNAL.value(), COND_GOT_EXIT_SIGNAL);
        RULES.put(CFRM_OPPOSITE_SIGNAL.value(), COND_CFRM_OPPOSITE_SIGNAL);
        RULES.put(BASELINE_CROSSED_EXIT.value(), COND_BASELINE_IS_CROSSED);
        RULES.put(CFRM_MATCHES_BASELINE_SIGNAL.value(), COND_CFRM_MATCHES_BL_SIGNAL);

        // Actions
        RULES.put(OPEN_TRADE.value(), ACTION_OPEN_TRADE);
        RULES.put(ADD_PENDING_TRADE.value(), ACTION_ADD_PENDING_TRADE);
        RULES.put(BASIC_OPEN_TRADE.value(), ACTION_OPEN_TRADE);
        RULES.put(BASIC_CLOSE_TRADE_W.value(), ACTION_CLOSE_TRADE_W);
        RULES.put(BASIC_CLOSE_TRADE_L.value(), ACTION_CLOSE_TRADE_L);
        RULES.put(BASIC_CLOSE_TRADE_CHALK.value(), ACTION_CLOSE_TRADE_CHALK);
        RULES.put(COMPLEX_CLOSE_TRADE.value(), ACTION_CLOSE_TRADE);
        RULES.put(MOVE_TRAILING_STOP.value(), ACTION_MOVE_TRAILING_STOP);
    }

    public IRule getRule(int rule) {
        return RULES.get(rule);
    }
}