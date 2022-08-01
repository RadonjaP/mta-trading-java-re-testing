package utils;

import com.rprelevic.re.api.IRuleId;

public enum TestRuleId implements IRuleId {

    SIGNAL_IS_NOT_PULLBACK(100),
    HAS_NO_CFRM_SIGNAL(101),
    HAS_NO_2ND_CFRM_SIGNAL(102),
    BASELINE_NOT_MATCH_TREND(103),
    NO_CONTINUATION_TRADE(104),
    VOLUME_NOT_EXISTS(105),
    PRICE_TO_BASELINE_NOT_LTHAN_ATR(106),
    BASELINE_NOT_CROSSED(107),
    CFRM_NOT_OPPOSITE_SIGNAL(119),
    END_OF_FLOW(199),
    BASELINE_NOT_CROSSED_EXIT(120),

    // Confirmation Indicator Testing

    ACTIVE_NOT_TRADE_EXISTS(108),
    ACTIVE_TRADE_TO_CHALK_NOT_EXISTS(114),
    STOP_LOSS_NOT_HIT(109),
    TAKE_PROFIT_NOT_HIT(110),
    TRAILING_STOP_NOT_HIT(118),
    NO_EXIT_SIGNAL(113);

    private final int id;

    TestRuleId(int executionId) {
        this.id = executionId;
    }

    @Override
    public int value() {
        return this.id;
    }
}