package com.rprelevic.re.model;

import com.rprelevic.re.api.IRuleId;

public enum RuleId implements IRuleId {

    /*
        Full Pipeline
     */
    SIGNAL_IS_PULLBACK(1),
    HAS_CFRM_SIGNAL(2),
    HAS_2ND_CFRM_SIGNAL(3),
    BASELINE_MATCH_TREND(4),
    CONTINUATION_TRADE(5),
    VOLUME_EXISTS(6),
    PRICE_TO_BASELINE_LTHAN_ATR(7),
    ADD_PENDING_TRADE(8),
    OPEN_TRADE(9),
    BASELINE_CROSSED(10),
    ACTIVE_TRADE_EXISTS(11),
    STOP_LOSS_HIT(12),
    TAKE_PROFIT_HIT(13),
    ACTIVE_TRADE_TO_CHALK_EXISTS(18),
    BASIC_CLOSE_TRADE_WL(14),
    BASIC_OPEN_TRADE(15),
    EXIT_SIGNAL(16),
    BASIC_CLOSE_TRADE_CHALK(17),
    COMPLEX_CLOSE_TRADE(19),
    BASELINE_CROSSED_EXIT(20),
    CFRM_OPPOSITE_SIGNAL(21),
    MOVE_TRAILING_STOP(22),
    TRAILING_STOP_HIT(23);

    private final int id;

    RuleId(int executionId) {
        this.id = executionId;
    }

    @Override
    public int value() {
        return this.id;
    }

}