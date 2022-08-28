package test;

import org.junit.jupiter.api.Test;
import com.rprelevic.re.core.RuleEngineBuilder;
import com.rprelevic.re.model.ExecutionRs;
import utils.TestRuleRegistry;
import utils.TestUtility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.rprelevic.re.core.ConditionsAndRules.*;
import static com.rprelevic.re.model.RuleId.*;
import static utils.TestRuleId.*;

class RuleEngineBaselinePplnTest {

    @Test
    public void test_Full() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS)
                .add(ACTIVE_TRADE_EXISTS, STOP_LOSS_HIT, BASELINE_CROSSED)
                .add(STOP_LOSS_HIT, BASIC_CLOSE_TRADE_L, TRAILING_STOP_HIT)
                .add(TRAILING_STOP_HIT, BASIC_CLOSE_TRADE_W, BASELINE_CROSSED_EXIT)
                .add(BASELINE_CROSSED_EXIT, COMPLEX_CLOSE_TRADE, CFRM_OPPOSITE_SIGNAL)
                .add(CFRM_OPPOSITE_SIGNAL, COMPLEX_CLOSE_TRADE, MOVE_TRAILING_STOP)
                .addOnlyTrue(COMPLEX_CLOSE_TRADE, BASELINE_CROSSED)
                .addOnlyTrue(BASIC_CLOSE_TRADE_W, BASELINE_CROSSED)
                .addOnlyTrue(BASIC_CLOSE_TRADE_L, BASELINE_CROSSED)
                .add(BASELINE_CROSSED, CFRM_MATCHES_BASELINE_SIGNAL_FALSE, HAS_CFRM_SIGNAL)
                .addOnlyTrue(CFRM_MATCHES_BASELINE_SIGNAL_FALSE, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND)
                .addOnlyTrue(BASELINE_MATCH_TREND, OPEN_TRADE)
                .build()
                .run();

        TestUtility.printLog(executionResult);

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

//        assertEquals(3, executionResult.getLog().size());
//        assertEquals(COND_HAVE_NO_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
//        assertEquals(COND_BASELINE_IS_CROSSED.getInfo(), executionResult.getLog().get(1));
//        assertEquals(ACTION_TERMINATE.getInfo(), executionResult.getLog().get(2));
    }

    @Test
    public void testTradeMgmt_whenActiveTradeDoesNotExists_thenSeekEntry() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS_FALSE)
                .add(ACTIVE_TRADE_EXISTS_FALSE, STOP_LOSS_HIT, BASELINE_CROSSED)
                .add(STOP_LOSS_HIT, BASIC_CLOSE_TRADE_L, TRAILING_STOP_HIT)
                .add(TRAILING_STOP_HIT, BASIC_CLOSE_TRADE_W, BASELINE_CROSSED_EXIT)
                .add(BASELINE_CROSSED_EXIT, COMPLEX_CLOSE_TRADE, CFRM_OPPOSITE_SIGNAL)
                .add(CFRM_OPPOSITE_SIGNAL, COMPLEX_CLOSE_TRADE, MOVE_TRAILING_STOP)
                .addOnlyTrue(COMPLEX_CLOSE_TRADE, BASELINE_CROSSED)
                .addOnlyTrue(BASIC_CLOSE_TRADE_W, BASELINE_CROSSED)
                .addOnlyTrue(BASIC_CLOSE_TRADE_L, BASELINE_CROSSED)
                .add(BASELINE_CROSSED, END_OF_FLOW, END_OF_FLOW)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(3, executionResult.getLog().size());
        assertEquals(COND_HAVE_NO_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_BASELINE_IS_CROSSED.getInfo(), executionResult.getLog().get(1));
        assertEquals(ACTION_TERMINATE.getInfo(), executionResult.getLog().get(2));
    }

    @Test
    public void testTradeMgmt_whenActiveTradeExists_andPriceHitSL_thenCloseTradeLoss() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS)
                .add(ACTIVE_TRADE_EXISTS, STOP_LOSS_HIT, BASELINE_CROSSED_FALSE)
                .add(STOP_LOSS_HIT, BASIC_CLOSE_TRADE_L, TRAILING_STOP_HIT)
                .add(TRAILING_STOP_HIT, BASIC_CLOSE_TRADE_W, BASELINE_CROSSED_EXIT)
                .add(BASELINE_CROSSED_EXIT, COMPLEX_CLOSE_TRADE, CFRM_OPPOSITE_SIGNAL)
                .add(CFRM_OPPOSITE_SIGNAL, COMPLEX_CLOSE_TRADE, MOVE_TRAILING_STOP)
                .addOnlyTrue(COMPLEX_CLOSE_TRADE, BASELINE_CROSSED_FALSE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_W, BASELINE_CROSSED_FALSE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_L, BASELINE_CROSSED_FALSE)
                .add(BASELINE_CROSSED_FALSE, END_OF_FLOW, END_OF_FLOW)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(5, executionResult.getLog().size());
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_PRICE_HIT_SL.getInfo(), executionResult.getLog().get(1));
        assertEquals(ACTION_CLOSE_TRADE_L.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(3));
        assertEquals(ACTION_TERMINATE.getInfo(), executionResult.getLog().get(4));
    }

    @Test
    public void testTradeMgmt_whenActiveTradeExists_baselineCrossed_thenCloseTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS)
                .add(ACTIVE_TRADE_EXISTS, STOP_LOSS_HIT_FALSE, BASELINE_CROSSED_FALSE)
                .add(STOP_LOSS_HIT_FALSE, BASIC_CLOSE_TRADE_L, TRAILING_STOP_HIT_FALSE)
                .add(TRAILING_STOP_HIT_FALSE, BASIC_CLOSE_TRADE_W, BASELINE_CROSSED_EXIT)
                .add(BASELINE_CROSSED_EXIT, COMPLEX_CLOSE_TRADE, CFRM_OPPOSITE_SIGNAL)
                .add(CFRM_OPPOSITE_SIGNAL, COMPLEX_CLOSE_TRADE, MOVE_TRAILING_STOP)
                .addOnlyTrue(COMPLEX_CLOSE_TRADE, BASELINE_CROSSED_FALSE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_W, BASELINE_CROSSED_FALSE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_L, BASELINE_CROSSED_FALSE)
                .add(BASELINE_CROSSED_FALSE, END_OF_FLOW, END_OF_FLOW)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(7, executionResult.getLog().size());
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_PRICE_DID_NOT_HIT_SL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_PRICE_DID_NOT_HIT_TS.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_IS_CROSSED.getInfo(), executionResult.getLog().get(3));
        assertEquals(ACTION_CLOSE_TRADE.getInfo(), executionResult.getLog().get(4));
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(5));
        assertEquals(ACTION_TERMINATE.getInfo(), executionResult.getLog().get(6));
    }

    @Test
    public void testTradeMgmt_whenActiveTradeExists_andCfrmSignalExists_thenCloseTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS)
                .add(ACTIVE_TRADE_EXISTS, STOP_LOSS_HIT_FALSE, BASELINE_CROSSED_FALSE)
                .add(STOP_LOSS_HIT_FALSE, BASIC_CLOSE_TRADE_L, TRAILING_STOP_HIT_FALSE)
                .add(TRAILING_STOP_HIT_FALSE, BASIC_CLOSE_TRADE_W, BASELINE_CROSSED_EXIT_FALSE)
                .add(BASELINE_CROSSED_EXIT_FALSE, COMPLEX_CLOSE_TRADE, CFRM_OPPOSITE_SIGNAL)
                .add(CFRM_OPPOSITE_SIGNAL, COMPLEX_CLOSE_TRADE, MOVE_TRAILING_STOP)
                .addOnlyTrue(COMPLEX_CLOSE_TRADE, BASELINE_CROSSED_FALSE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_W, BASELINE_CROSSED_FALSE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_L, BASELINE_CROSSED_FALSE)
                .add(BASELINE_CROSSED_FALSE, END_OF_FLOW, END_OF_FLOW)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(8, executionResult.getLog().size());
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_PRICE_DID_NOT_HIT_SL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_PRICE_DID_NOT_HIT_TS.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(3));
        assertEquals(COND_CFRM_OPPOSITE_SIGNAL.getInfo(), executionResult.getLog().get(4));
        assertEquals(ACTION_CLOSE_TRADE.getInfo(), executionResult.getLog().get(5));
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(6));
        assertEquals(ACTION_TERMINATE.getInfo(), executionResult.getLog().get(7));
    }

    @Test
    public void testTradeMgmt_whenActiveTradeExists_andNoPrecon_thenMoveTSAndNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS)
                .add(ACTIVE_TRADE_EXISTS, STOP_LOSS_HIT_FALSE, BASELINE_CROSSED_FALSE)
                .add(STOP_LOSS_HIT_FALSE, BASIC_CLOSE_TRADE_L, TRAILING_STOP_HIT_FALSE)
                .add(TRAILING_STOP_HIT_FALSE, BASIC_CLOSE_TRADE_W, BASELINE_CROSSED_EXIT_FALSE)
                .add(BASELINE_CROSSED_EXIT_FALSE, COMPLEX_CLOSE_TRADE, CFRM_OPPOSITE_SIGNAL_FALSE)
                .add(CFRM_OPPOSITE_SIGNAL_FALSE, COMPLEX_CLOSE_TRADE, MOVE_TRAILING_STOP)
                .addOnlyTrue(COMPLEX_CLOSE_TRADE, BASELINE_CROSSED_FALSE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_W, BASELINE_CROSSED_FALSE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_L, BASELINE_CROSSED_FALSE)
                .add(BASELINE_CROSSED_FALSE, END_OF_FLOW, END_OF_FLOW)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(6, executionResult.getLog().size());
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_PRICE_DID_NOT_HIT_SL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_PRICE_DID_NOT_HIT_TS.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(3));
        assertEquals(COND_CFRM_NOT_OPPOSITE_SIGNAL.getInfo(), executionResult.getLog().get(4));
        assertEquals(ACTION_MOVE_TRAILING_STOP.getInfo(), executionResult.getLog().get(5));
    }

    @Test
    public void testEntrySignal_whenBaselineCrossed_andNoCfrm_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_CROSSED)
                .add(BASELINE_CROSSED, CFRM_MATCHES_BASELINE_SIGNAL_FALSE, HAS_CFRM_SIGNAL)
                .addOnlyTrue(CFRM_MATCHES_BASELINE_SIGNAL_FALSE, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND)
                .addOnlyTrue(BASELINE_MATCH_TREND, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(2, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_CFRM_NOT_MATCHES_BL_SIGNAL.getInfo(), executionResult.getLog().get(1));
    }

    @Test
    public void testEntrySignal_whenBaselineNotCrossed_andNoCfrm_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_CROSSED_FALSE)
                .add(BASELINE_CROSSED_FALSE, CFRM_MATCHES_BASELINE_SIGNAL_FALSE, HAS_CFRM_SIGNAL_FALSE)
                .addOnlyTrue(CFRM_MATCHES_BASELINE_SIGNAL_FALSE, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL_FALSE, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND)
                .addOnlyTrue(BASELINE_MATCH_TREND, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(2, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_NO_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
    }

    @Test
    public void testEntrySignal_whenBaselineNotCrossed_andCfrm_andNo2ndCfrm_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_CROSSED_FALSE)
                .add(BASELINE_CROSSED_FALSE, CFRM_MATCHES_BASELINE_SIGNAL_FALSE, HAS_CFRM_SIGNAL)
                .addOnlyTrue(CFRM_MATCHES_BASELINE_SIGNAL_FALSE, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL_FALSE)
                .addOnlyTrue(HAS_2ND_CFRM_SIGNAL_FALSE, BASELINE_MATCH_TREND)
                .addOnlyTrue(BASELINE_MATCH_TREND, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(3, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAS_NO_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(2));
    }

    @Test
    public void testEntrySignal_whenBaselineNotCrossed_andCfrmAnd2ndCfrm_andNoTrendMatch_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_CROSSED_FALSE)
                .add(BASELINE_CROSSED_FALSE, CFRM_MATCHES_BASELINE_SIGNAL_FALSE, HAS_CFRM_SIGNAL)
                .addOnlyTrue(CFRM_MATCHES_BASELINE_SIGNAL_FALSE, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND_FALSE)
                .addOnlyTrue(BASELINE_MATCH_TREND_FALSE, OPEN_TRADE)
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
    public void testEntrySignal_whenBaselineNotCrossed_andCfrmAnd2ndCfrmAndTrendMatch_thenOpenTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_CROSSED_FALSE)
                .add(BASELINE_CROSSED_FALSE, CFRM_MATCHES_BASELINE_SIGNAL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(CFRM_MATCHES_BASELINE_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND)
                .addOnlyTrue(BASELINE_MATCH_TREND, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(5, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAS_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_MATCH_TREND.getInfo(), executionResult.getLog().get(3));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getLog().get(4));
    }

}