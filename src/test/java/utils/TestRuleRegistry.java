package utils;

import com.rprelevic.re.core.RuleRegistry;

import static com.rprelevic.re.core.ConditionsAndRules.*;
import static utils.TestRuleId.*;

public class TestRuleRegistry extends RuleRegistry {

    static {
        // Conditions
        RULES.put(SIGNAL_IS_NOT_PULLBACK.value(), COND_SIGNAL_IS_NOT_PULLBACK);
        RULES.put(HAS_NO_CFRM_SIGNAL.value(), COND_HAS_NO_CFRM_SIGNAL);
        RULES.put(HAS_NO_2ND_CFRM_SIGNAL.value(), COND_HAS_NO_2ND_CFRM_SIGNAL);
        RULES.put(BASELINE_NOT_MATCH_TREND.value(), COND_BASELINE_DOES_NOT_MATCH_TREND);
        RULES.put(NO_CONTINUATION_TRADE.value(), COND_IS_NOT_CONTINUATION_TRADE);
        RULES.put(VOLUME_NOT_EXISTS.value(), COND_VOLUME_DOES_NOT_EXIST);
        RULES.put(PRICE_TO_BASELINE_NOT_LTHAN_ATR.value(), COND_PRICE_TO_BASELINE_NOT_LTHEN_ATR);
        RULES.put(BASELINE_NOT_CROSSED.value(), COND_BASELINE_IS_NOT_CROSSED);
        RULES.put(CFRM_NOT_OPPOSITE_SIGNAL.value(), COND_CFRM_NOT_OPPOSITE_SIGNAL);
        RULES.put(BASELINE_NOT_CROSSED_EXIT.value(), COND_BASELINE_IS_NOT_CROSSED);
        RULES.put(TRAILING_STOP_NOT_HIT.value(), COND_PRICE_DID_NOT_HIT_TS);

        RULES.put(ACTIVE_NOT_TRADE_EXISTS.value(), COND_HAVE_NO_ACTIVE_TRADE);
        RULES.put(STOP_LOSS_NOT_HIT.value(), COND_PRICE_DID_NOT_HIT_SL);
        RULES.put(TAKE_PROFIT_NOT_HIT.value(), COND_PRICE_DID_NOT_HIT_TP);
        RULES.put(NO_EXIT_SIGNAL.value(), COND_DID_NOT_GET_EXIT_SIGNAL);
        RULES.put(ACTIVE_TRADE_TO_CHALK_NOT_EXISTS.value(), COND_HAVE_NO_ACTIVE_TRADE_CHALK);
        RULES.put(END_OF_FLOW.value(), ACTION_TERMINATE);
    }

}