package test;

import org.junit.jupiter.api.Test;
import com.rprelevic.re.model.ExecutionRs;
import com.rprelevic.re.core.RuleEngineBuilder;
import utils.TestRuleRegistry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.rprelevic.re.core.ConditionsAndRules.*;
import static com.rprelevic.re.model.RuleId.*;
import static utils.TestRuleId.*;

class RuleEngineFullFXPplnTest {

    @Test
    void testEntrySignal_whenBaselineCrossed_andPullback_thenAddPendingTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_CROSSED)
                .add(BASELINE_CROSSED, SIGNAL_IS_PULLBACK, HAS_CFRM_SIGNAL)
                .addOnlyTrue(SIGNAL_IS_PULLBACK, ADD_PENDING_TRADE)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .add(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND, ADD_PENDING_TRADE) // ADD_PENDING_TRADE for "One Candle Rule"
                .addOnlyTrue(BASELINE_MATCH_TREND, CONTINUATION_TRADE)
                .add(CONTINUATION_TRADE, OPEN_TRADE, VOLUME_EXISTS)
                .addOnlyTrue(VOLUME_EXISTS, PRICE_TO_BASELINE_LTHAN_ATR)
                .addOnlyTrue(PRICE_TO_BASELINE_LTHAN_ATR, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(3, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_SIGNAL_IS_PULLBACK.getInfo(), executionResult.getLog().get(1));
        assertEquals(ACTION_ADD_PENDING_TRADE.getInfo(), executionResult.getLog().get(2));
    }

    @Test
    void testEntrySignal_whenBaselineCrossed_andNotPullback_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_CROSSED)
                .add(BASELINE_CROSSED, SIGNAL_IS_NOT_PULLBACK, HAS_CFRM_SIGNAL)
                .addOnlyTrue(SIGNAL_IS_NOT_PULLBACK, ADD_PENDING_TRADE)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .add(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND, ADD_PENDING_TRADE)
                .addOnlyTrue(BASELINE_MATCH_TREND, CONTINUATION_TRADE)
                .add(CONTINUATION_TRADE, OPEN_TRADE, VOLUME_EXISTS)
                .addOnlyTrue(VOLUME_EXISTS, PRICE_TO_BASELINE_LTHAN_ATR)
                .addOnlyTrue(PRICE_TO_BASELINE_LTHAN_ATR, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(2, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_SIGNAL_IS_NOT_PULLBACK.getInfo(), executionResult.getLog().get(1));
    }

    @Test
    void test_whenBaselineNotCrossed_andNoSignal_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_NOT_CROSSED)
                .add(BASELINE_NOT_CROSSED, SIGNAL_IS_NOT_PULLBACK, HAS_NO_CFRM_SIGNAL)
                .addOnlyTrue(SIGNAL_IS_NOT_PULLBACK, ADD_PENDING_TRADE)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .add(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND, ADD_PENDING_TRADE)
                .addOnlyTrue(BASELINE_MATCH_TREND, CONTINUATION_TRADE)
                .add(CONTINUATION_TRADE, OPEN_TRADE, VOLUME_EXISTS)
                .addOnlyTrue(VOLUME_EXISTS, PRICE_TO_BASELINE_LTHAN_ATR)
                .addOnlyTrue(PRICE_TO_BASELINE_LTHAN_ATR, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(2, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_NO_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
    }

    @Test
    void test_whenBaselineNotCrossedAndSignal_andNoConfirmSignal_thenPendingTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_NOT_CROSSED)
                .add(BASELINE_NOT_CROSSED, SIGNAL_IS_NOT_PULLBACK, HAS_CFRM_SIGNAL)
                .addOnlyTrue(SIGNAL_IS_NOT_PULLBACK, ADD_PENDING_TRADE)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_NO_2ND_CFRM_SIGNAL)
                .add(HAS_NO_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND, ADD_PENDING_TRADE)
                .addOnlyTrue(BASELINE_MATCH_TREND, CONTINUATION_TRADE)
                .add(CONTINUATION_TRADE, OPEN_TRADE, VOLUME_EXISTS)
                .addOnlyTrue(VOLUME_EXISTS, PRICE_TO_BASELINE_LTHAN_ATR)
                .addOnlyTrue(PRICE_TO_BASELINE_LTHAN_ATR, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(4, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAS_NO_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(2));
        assertEquals(ACTION_ADD_PENDING_TRADE.getInfo(), executionResult.getLog().get(3));
    }

    @Test
    void test_whenBaselineNotCrossed_andSignals_baselineTrendNoMatch_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_NOT_CROSSED)
                .add(BASELINE_NOT_CROSSED, SIGNAL_IS_NOT_PULLBACK, HAS_CFRM_SIGNAL)
                .addOnlyTrue(SIGNAL_IS_NOT_PULLBACK, ADD_PENDING_TRADE)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .add(HAS_2ND_CFRM_SIGNAL, BASELINE_NOT_MATCH_TREND, ADD_PENDING_TRADE)
                .addOnlyTrue(BASELINE_NOT_MATCH_TREND, CONTINUATION_TRADE)
                .add(CONTINUATION_TRADE, OPEN_TRADE, VOLUME_EXISTS)
                .addOnlyTrue(VOLUME_EXISTS, PRICE_TO_BASELINE_LTHAN_ATR)
                .addOnlyTrue(PRICE_TO_BASELINE_LTHAN_ATR, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(4, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAS_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_DOES_NOT_MATCH_TREND.getInfo(), executionResult.getLog().get(3));
    }

    @Test
    void test_whenBaselineNotCrossed_andBLMatchTrade_thenOpenTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_NOT_CROSSED)
                .add(BASELINE_NOT_CROSSED, SIGNAL_IS_NOT_PULLBACK, HAS_CFRM_SIGNAL)
                .addOnlyTrue(SIGNAL_IS_NOT_PULLBACK, ADD_PENDING_TRADE)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .add(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND, ADD_PENDING_TRADE)
                .addOnlyTrue(BASELINE_MATCH_TREND, CONTINUATION_TRADE)
                .add(CONTINUATION_TRADE, OPEN_TRADE, VOLUME_EXISTS)
                .addOnlyTrue(VOLUME_EXISTS, PRICE_TO_BASELINE_LTHAN_ATR)
                .addOnlyTrue(PRICE_TO_BASELINE_LTHAN_ATR, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(6, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAS_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_MATCH_TREND.getInfo(), executionResult.getLog().get(3));
        assertEquals(COND_IS_CONTINUATION_TRADE.getInfo(), executionResult.getLog().get(4));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getLog().get(5));
    }

    @Test
    void test_whenBaselineNotCrossed_andNotContinuationTrade_andNoVolume_noTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_NOT_CROSSED)
                .add(BASELINE_NOT_CROSSED, SIGNAL_IS_NOT_PULLBACK, HAS_CFRM_SIGNAL)
                .addOnlyTrue(SIGNAL_IS_NOT_PULLBACK, ADD_PENDING_TRADE)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .add(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND, ADD_PENDING_TRADE)
                .addOnlyTrue(BASELINE_MATCH_TREND, NO_CONTINUATION_TRADE)
                .add(NO_CONTINUATION_TRADE, OPEN_TRADE, VOLUME_NOT_EXISTS)
                .addOnlyTrue(VOLUME_NOT_EXISTS, PRICE_TO_BASELINE_LTHAN_ATR)
                .addOnlyTrue(PRICE_TO_BASELINE_LTHAN_ATR, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(6, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAS_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_MATCH_TREND.getInfo(), executionResult.getLog().get(3));
        assertEquals(COND_IS_NOT_CONTINUATION_TRADE.getInfo(), executionResult.getLog().get(4));
        assertEquals(COND_VOLUME_DOES_NOT_EXIST.getInfo(), executionResult.getLog().get(5));
    }

    @Test
    void test_whenBaselineNotCrossed_andVolume_andPriceDistanceGreater_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_NOT_CROSSED)
                .add(BASELINE_NOT_CROSSED, SIGNAL_IS_NOT_PULLBACK, HAS_CFRM_SIGNAL)
                .addOnlyTrue(SIGNAL_IS_NOT_PULLBACK, ADD_PENDING_TRADE)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .add(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND, ADD_PENDING_TRADE)
                .addOnlyTrue(BASELINE_MATCH_TREND, NO_CONTINUATION_TRADE)
                .add(NO_CONTINUATION_TRADE, OPEN_TRADE, VOLUME_EXISTS)
                .addOnlyTrue(VOLUME_EXISTS, PRICE_TO_BASELINE_NOT_LTHAN_ATR)
                .addOnlyTrue(PRICE_TO_BASELINE_NOT_LTHAN_ATR, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(7, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAS_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_MATCH_TREND.getInfo(), executionResult.getLog().get(3));
        assertEquals(COND_IS_NOT_CONTINUATION_TRADE.getInfo(), executionResult.getLog().get(4));
        assertEquals(COND_VOLUME_EXISTS.getInfo(), executionResult.getLog().get(5));
        assertEquals(COND_PRICE_TO_BASELINE_NOT_LTHEN_ATR.getInfo(), executionResult.getLog().get(6));
    }

    @Test
    void test_whenBaselineNotCrossed_andVolume_andPriceDistanceLower_thenOpenTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_NOT_CROSSED)
                .add(BASELINE_NOT_CROSSED, SIGNAL_IS_NOT_PULLBACK, HAS_CFRM_SIGNAL)
                .addOnlyTrue(SIGNAL_IS_NOT_PULLBACK, ADD_PENDING_TRADE)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .add(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND, ADD_PENDING_TRADE)
                .addOnlyTrue(BASELINE_MATCH_TREND, NO_CONTINUATION_TRADE)
                .add(NO_CONTINUATION_TRADE, OPEN_TRADE, VOLUME_EXISTS)
                .addOnlyTrue(VOLUME_EXISTS, PRICE_TO_BASELINE_LTHAN_ATR)
                .addOnlyTrue(PRICE_TO_BASELINE_LTHAN_ATR, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(8, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAS_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_MATCH_TREND.getInfo(), executionResult.getLog().get(3));
        assertEquals(COND_IS_NOT_CONTINUATION_TRADE.getInfo(), executionResult.getLog().get(4));
        assertEquals(COND_VOLUME_EXISTS.getInfo(), executionResult.getLog().get(5));
        assertEquals(COND_PRICE_TO_BASELINE_LTHEN_ATR.getInfo(), executionResult.getLog().get(6));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getLog().get(7));
    }


}