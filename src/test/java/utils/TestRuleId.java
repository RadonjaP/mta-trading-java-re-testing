package utils;

import com.rprelevic.re.api.IRuleId;

public enum TestRuleId implements IRuleId {

    SIGNAL_IS_PULLBACK_FALSE(100),
    HAS_CFRM_SIGNAL_FALSE(101),
    HAS_2ND_CFRM_SIGNAL_FALSE(102),
    BASELINE_MATCH_TREND_FALSE(103),
    CONTINUATION_TRADE_FALSE(104),
    VOLUME_EXISTS_FALSE(105),
    PRICE_TO_BASELINE_LTHAN_ATR_FALSE(106),
    BASELINE_CROSSED_FALSE(107),
    CFRM_OPPOSITE_SIGNAL_FALSE(119),
    END_OF_FLOW(199),
    BASELINE_CROSSED_EXIT_FALSE(120),

    // Confirmation Indicator Testing

    ACTIVE_TRADE_EXISTS_FALSE(108),
    ACTIVE_TRADE_TO_CHALK_EXISTS_FALSE(114),
    STOP_LOSS_HIT_FALSE(109),
    TAKE_PROFIT_HIT_FALSE(110),
    TRAILING_STOP_HIT_FALSE(118),
    EXIT_SIGNAL_FALSE(113),
    CFRM_MATCHES_BASELINE_SIGNAL_FALSE(125);

    private final int id;

    TestRuleId(int executionId) {
        this.id = executionId;
    }

    @Override
    public int value() {
        return this.id;
    }
}