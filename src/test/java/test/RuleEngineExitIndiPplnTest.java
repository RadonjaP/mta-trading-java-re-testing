package test;

import org.junit.jupiter.api.Test;
import com.rprelevic.re.core.RuleEngineBuilder;
import com.rprelevic.re.model.ExecutionRs;
import utils.TestRuleRegistry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.rprelevic.re.core.ConditionsAndRules.*;
import static com.rprelevic.re.model.RuleId.*;
import static utils.TestRuleId.*;

class RuleEngineExitIndiPplnTest {

    @Test
    public void testTradeMgmt_whenActiveTradeDoesNotExists_thenSeekEntry() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_NOT_TRADE_EXISTS)
                .add(ACTIVE_NOT_TRADE_EXISTS, TRAILING_STOP_HIT, BASELINE_CROSSED)
                .add(TRAILING_STOP_HIT, COMPLEX_CLOSE_TRADE, BASELINE_CROSSED_EXIT)
                .addOnlyTrue(COMPLEX_CLOSE_TRADE, BASELINE_CROSSED)
                .add(BASELINE_CROSSED_EXIT, COMPLEX_CLOSE_TRADE, CFRM_OPPOSITE_SIGNAL)
                .add(CFRM_OPPOSITE_SIGNAL, COMPLEX_CLOSE_TRADE, EXIT_SIGNAL)
                .add(EXIT_SIGNAL, COMPLEX_CLOSE_TRADE, MOVE_TRAILING_STOP)
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
    public void testTradeMgmt_whenActiveTradeExists_andPriceHitTS_thenCloseTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS)
                .add(ACTIVE_TRADE_EXISTS, TRAILING_STOP_HIT, BASELINE_NOT_CROSSED)
                .add(TRAILING_STOP_HIT, COMPLEX_CLOSE_TRADE, BASELINE_CROSSED_EXIT)
                .addOnlyTrue(COMPLEX_CLOSE_TRADE, BASELINE_NOT_CROSSED)
                .add(BASELINE_CROSSED_EXIT, COMPLEX_CLOSE_TRADE, CFRM_OPPOSITE_SIGNAL)
                .add(CFRM_OPPOSITE_SIGNAL, COMPLEX_CLOSE_TRADE, EXIT_SIGNAL)
                .add(EXIT_SIGNAL, COMPLEX_CLOSE_TRADE, MOVE_TRAILING_STOP)
                .add(BASELINE_NOT_CROSSED, END_OF_FLOW, END_OF_FLOW)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(5, executionResult.getLog().size());
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_PRICE_HIT_TS.getInfo(), executionResult.getLog().get(1));
        assertEquals(ACTION_CLOSE_TRADE.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(3));
        assertEquals(ACTION_TERMINATE.getInfo(), executionResult.getLog().get(4));
    }

    @Test
    public void testTradeMgmt_whenActiveTradeExists_baselineCrossed_thenCloseTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS)
                .add(ACTIVE_TRADE_EXISTS, TRAILING_STOP_NOT_HIT, BASELINE_CROSSED_EXIT)
                .add(TRAILING_STOP_NOT_HIT, COMPLEX_CLOSE_TRADE, BASELINE_CROSSED_EXIT)
                .addOnlyTrue(COMPLEX_CLOSE_TRADE, BASELINE_NOT_CROSSED)
                .add(BASELINE_CROSSED_EXIT, COMPLEX_CLOSE_TRADE, CFRM_OPPOSITE_SIGNAL)
                .add(CFRM_OPPOSITE_SIGNAL, COMPLEX_CLOSE_TRADE, EXIT_SIGNAL)
                .add(EXIT_SIGNAL, COMPLEX_CLOSE_TRADE, MOVE_TRAILING_STOP)
                .add(BASELINE_NOT_CROSSED, END_OF_FLOW, END_OF_FLOW)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(6, executionResult.getLog().size());
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_PRICE_DID_NOT_HIT_TS.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_BASELINE_IS_CROSSED.getInfo(), executionResult.getLog().get(2));
        assertEquals(ACTION_CLOSE_TRADE.getInfo(), executionResult.getLog().get(3));
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(4));
        assertEquals(ACTION_TERMINATE.getInfo(), executionResult.getLog().get(5));
    }

    @Test
    public void testTradeMgmt_whenActiveTradeExists_andCfrmSignalExists_thenCloseTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS)
                .add(ACTIVE_TRADE_EXISTS, TRAILING_STOP_NOT_HIT, BASELINE_NOT_CROSSED)
                .add(TRAILING_STOP_NOT_HIT, COMPLEX_CLOSE_TRADE, BASELINE_NOT_CROSSED_EXIT)
                .addOnlyTrue(COMPLEX_CLOSE_TRADE, BASELINE_NOT_CROSSED)
                .add(BASELINE_NOT_CROSSED_EXIT, COMPLEX_CLOSE_TRADE, CFRM_OPPOSITE_SIGNAL)
                .add(CFRM_OPPOSITE_SIGNAL, COMPLEX_CLOSE_TRADE, EXIT_SIGNAL)
                .add(EXIT_SIGNAL, COMPLEX_CLOSE_TRADE, MOVE_TRAILING_STOP)
                .add(BASELINE_NOT_CROSSED, END_OF_FLOW, END_OF_FLOW)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(7, executionResult.getLog().size());
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_PRICE_DID_NOT_HIT_TS.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_CFRM_OPPOSITE_SIGNAL.getInfo(), executionResult.getLog().get(3));
        assertEquals(ACTION_CLOSE_TRADE.getInfo(), executionResult.getLog().get(4));
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(5));
        assertEquals(ACTION_TERMINATE.getInfo(), executionResult.getLog().get(6));
    }

    @Test
    public void testTradeMgmt_whenActiveTradeExists_andExitSignalExists_thenCloseTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS)
                .add(ACTIVE_TRADE_EXISTS, TRAILING_STOP_NOT_HIT, BASELINE_NOT_CROSSED)
                .add(TRAILING_STOP_NOT_HIT, COMPLEX_CLOSE_TRADE, BASELINE_NOT_CROSSED_EXIT)
                .addOnlyTrue(COMPLEX_CLOSE_TRADE, BASELINE_NOT_CROSSED)
                .add(BASELINE_NOT_CROSSED_EXIT, COMPLEX_CLOSE_TRADE, CFRM_NOT_OPPOSITE_SIGNAL)
                .add(CFRM_NOT_OPPOSITE_SIGNAL, COMPLEX_CLOSE_TRADE, EXIT_SIGNAL)
                .add(EXIT_SIGNAL, COMPLEX_CLOSE_TRADE, MOVE_TRAILING_STOP)
                .add(BASELINE_NOT_CROSSED, END_OF_FLOW, END_OF_FLOW)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(8, executionResult.getLog().size());
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_PRICE_DID_NOT_HIT_TS.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_CFRM_NOT_OPPOSITE_SIGNAL.getInfo(), executionResult.getLog().get(3));
        assertEquals(COND_GOT_EXIT_SIGNAL.getInfo(), executionResult.getLog().get(4));
        assertEquals(ACTION_CLOSE_TRADE.getInfo(), executionResult.getLog().get(5));
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(6));
        assertEquals(ACTION_TERMINATE.getInfo(), executionResult.getLog().get(7));
    }

    @Test
    public void testTradeMgmt_whenActiveTradeExists_andNoPrecon_thenMoveTSAndNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS)
                .add(ACTIVE_TRADE_EXISTS, TRAILING_STOP_NOT_HIT, BASELINE_NOT_CROSSED)
                .add(TRAILING_STOP_NOT_HIT, COMPLEX_CLOSE_TRADE, BASELINE_NOT_CROSSED_EXIT)
                .addOnlyTrue(COMPLEX_CLOSE_TRADE, BASELINE_NOT_CROSSED)
                .add(BASELINE_NOT_CROSSED_EXIT, COMPLEX_CLOSE_TRADE, CFRM_NOT_OPPOSITE_SIGNAL)
                .add(CFRM_NOT_OPPOSITE_SIGNAL, COMPLEX_CLOSE_TRADE, NO_EXIT_SIGNAL)
                .add(NO_EXIT_SIGNAL, COMPLEX_CLOSE_TRADE, MOVE_TRAILING_STOP)
                .add(BASELINE_NOT_CROSSED, END_OF_FLOW, END_OF_FLOW)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(6, executionResult.getLog().size());
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_PRICE_DID_NOT_HIT_TS.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_CFRM_NOT_OPPOSITE_SIGNAL.getInfo(), executionResult.getLog().get(3));
        assertEquals(COND_DID_NOT_GET_EXIT_SIGNAL.getInfo(), executionResult.getLog().get(4));
        assertEquals(ACTION_MOVE_TRAILING_STOP.getInfo(), executionResult.getLog().get(5));
    }

    @Test
    public void testEntrySignal_whenBaselineCrossed_andNoCfrm_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_CROSSED)
                .add(BASELINE_CROSSED, HAS_NO_CFRM_SIGNAL, HAS_NO_CFRM_SIGNAL)
                .addOnlyTrue(HAS_NO_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND)
                .addOnlyTrue(BASELINE_MATCH_TREND, BASIC_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(2, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_NO_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
    }

    @Test
    public void testEntrySignal_whenBaselineNotCrossed_andNoCfrm_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_NOT_CROSSED)
                .add(BASELINE_NOT_CROSSED, HAS_NO_CFRM_SIGNAL, HAS_NO_CFRM_SIGNAL)
                .addOnlyTrue(HAS_NO_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND)
                .addOnlyTrue(BASELINE_MATCH_TREND, BASIC_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(2, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_NOT_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_NO_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
    }

    @Test
    public void testEntrySignal_whenBaselineCrossed_andCfrm_andNo2ndCfrm_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_CROSSED)
                .add(BASELINE_CROSSED, HAS_CFRM_SIGNAL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_NO_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_NO_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND)
                .addOnlyTrue(BASELINE_MATCH_TREND, BASIC_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(3, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAS_NO_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(2));
    }

    @Test
    public void testEntrySignal_whenBaselineCrossed_andCfrmAnd2ndCfrm_andNoTrendMatch_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_CROSSED)
                .add(BASELINE_CROSSED, HAS_CFRM_SIGNAL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_2ND_CFRM_SIGNAL, BASELINE_NOT_MATCH_TREND)
                .addOnlyTrue(BASELINE_NOT_MATCH_TREND, BASIC_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(4, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAS_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_DOES_NOT_MATCH_TREND.getInfo(), executionResult.getLog().get(3));
    }

    @Test
    public void testEntrySignal_whenBaselineCrossed_andCfrmAnd2ndCfrmAndTrendMatch_thenOpenTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_CROSSED)
                .add(BASELINE_CROSSED, HAS_CFRM_SIGNAL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND)
                .addOnlyTrue(BASELINE_MATCH_TREND, BASIC_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(5, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAS_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_MATCH_TREND.getInfo(), executionResult.getLog().get(3));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getLog().get(4));
    }

}