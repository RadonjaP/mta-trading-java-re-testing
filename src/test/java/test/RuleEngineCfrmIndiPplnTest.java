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

class RuleEngineCfrmIndiPplnTest {

    @Test
    void whenNoActiveTradeAndCfrmSignal_thenOpenTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS_FALSE)
                .add(ACTIVE_TRADE_EXISTS_FALSE, STOP_LOSS_HIT, HAS_CFRM_SIGNAL)
                .add(STOP_LOSS_HIT, BASIC_CLOSE_TRADE_L, TAKE_PROFIT_HIT)
                .add(TAKE_PROFIT_HIT, BASIC_CLOSE_TRADE_W, HAS_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_W, HAS_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_L, HAS_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, ACTIVE_TRADE_TO_CHALK_EXISTS_FALSE)
                .add(ACTIVE_TRADE_TO_CHALK_EXISTS_FALSE, BASIC_CLOSE_TRADE_CHALK, OPEN_TRADE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(4, executionResult.getLog().size());
        assertEquals(COND_HAVE_NO_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAVE_NO_ACTIVE_TRADE_CHALK.getInfo(), executionResult.getLog().get(2));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getLog().get(3));
    }

    @Test
    void whenNoActiveTradeAndNoCfrmSignal_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS_FALSE)
                .add(ACTIVE_TRADE_EXISTS_FALSE, STOP_LOSS_HIT, HAS_CFRM_SIGNAL_FALSE)
                .add(STOP_LOSS_HIT, BASIC_CLOSE_TRADE_L, TAKE_PROFIT_HIT)
                .add(TAKE_PROFIT_HIT, BASIC_CLOSE_TRADE_W, HAS_CFRM_SIGNAL_FALSE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_W, HAS_CFRM_SIGNAL_FALSE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_L, HAS_CFRM_SIGNAL_FALSE)
                .addOnlyTrue(HAS_CFRM_SIGNAL_FALSE, ACTIVE_TRADE_TO_CHALK_EXISTS_FALSE)
                .add(ACTIVE_TRADE_TO_CHALK_EXISTS_FALSE, BASIC_CLOSE_TRADE_CHALK, OPEN_TRADE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(2, executionResult.getLog().size());
        assertEquals(COND_HAVE_NO_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_NO_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
    }

    @Test
    void whenActiveTrade_andSLTPNotHit_andCfrmSignal_thenOpenTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS)
                .add(ACTIVE_TRADE_EXISTS, STOP_LOSS_HIT_FALSE, HAS_CFRM_SIGNAL)
                .add(STOP_LOSS_HIT_FALSE, BASIC_CLOSE_TRADE_L, TAKE_PROFIT_HIT_FALSE)
                .add(TAKE_PROFIT_HIT_FALSE, BASIC_CLOSE_TRADE_W, HAS_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_W, HAS_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_L, HAS_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, ACTIVE_TRADE_TO_CHALK_EXISTS_FALSE)
                .add(ACTIVE_TRADE_TO_CHALK_EXISTS_FALSE, BASIC_CLOSE_TRADE_CHALK, OPEN_TRADE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(6, executionResult.getLog().size());
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_PRICE_DID_NOT_HIT_SL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_PRICE_DID_NOT_HIT_TP.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(3));
        assertEquals(COND_HAVE_NO_ACTIVE_TRADE_CHALK.getInfo(), executionResult.getLog().get(4));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getLog().get(5));
    }

    @Test
    void whenActiveTrade_andSLHit_thenCloseTradeLose() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS)
                .add(ACTIVE_TRADE_EXISTS, STOP_LOSS_HIT, HAS_CFRM_SIGNAL_FALSE)
                .add(STOP_LOSS_HIT, BASIC_CLOSE_TRADE_L, TAKE_PROFIT_HIT)
                .add(TAKE_PROFIT_HIT, BASIC_CLOSE_TRADE_W, HAS_CFRM_SIGNAL_FALSE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_W, HAS_CFRM_SIGNAL_FALSE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_L, HAS_CFRM_SIGNAL_FALSE)
                .addOnlyTrue(HAS_CFRM_SIGNAL_FALSE, ACTIVE_TRADE_TO_CHALK_EXISTS_FALSE)
                .add(ACTIVE_TRADE_TO_CHALK_EXISTS_FALSE, BASIC_CLOSE_TRADE_CHALK, OPEN_TRADE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(4, executionResult.getLog().size());
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_PRICE_HIT_SL.getInfo(), executionResult.getLog().get(1));
        assertEquals(ACTION_CLOSE_TRADE_L.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_HAS_NO_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(3));
    }

    @Test
    void whenActiveTrade_andSLNotHit_andTPHit_thenCloseTradeWin() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS)
                .add(ACTIVE_TRADE_EXISTS, STOP_LOSS_HIT_FALSE, HAS_CFRM_SIGNAL_FALSE)
                .add(STOP_LOSS_HIT_FALSE, BASIC_CLOSE_TRADE_L, TAKE_PROFIT_HIT)
                .add(TAKE_PROFIT_HIT, BASIC_CLOSE_TRADE_W, HAS_CFRM_SIGNAL_FALSE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_W, HAS_CFRM_SIGNAL_FALSE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_L, HAS_CFRM_SIGNAL_FALSE)
                .addOnlyTrue(HAS_CFRM_SIGNAL_FALSE, ACTIVE_TRADE_TO_CHALK_EXISTS_FALSE)
                .add(ACTIVE_TRADE_TO_CHALK_EXISTS_FALSE, BASIC_CLOSE_TRADE_CHALK, OPEN_TRADE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(5, executionResult.getLog().size());
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_PRICE_DID_NOT_HIT_SL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_PRICE_HIT_TP.getInfo(), executionResult.getLog().get(2));
        assertEquals(ACTION_CLOSE_TRADE_W.getInfo(), executionResult.getLog().get(3));
        assertEquals(COND_HAS_NO_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(4));
    }

    @Test
    void whenHasActiveTradeChalk_AndCfrmSignal_andNoChalkTrade_thenCreateNewTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS_FALSE)
                .add(ACTIVE_TRADE_EXISTS_FALSE, STOP_LOSS_HIT_FALSE, HAS_CFRM_SIGNAL)
                .add(STOP_LOSS_HIT_FALSE, BASIC_CLOSE_TRADE_L, TAKE_PROFIT_HIT_FALSE)
                .add(TAKE_PROFIT_HIT_FALSE, BASIC_CLOSE_TRADE_W, HAS_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_W, HAS_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_L, HAS_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, ACTIVE_TRADE_TO_CHALK_EXISTS_FALSE)
                .add(ACTIVE_TRADE_TO_CHALK_EXISTS_FALSE, BASIC_CLOSE_TRADE_CHALK, OPEN_TRADE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(4, executionResult.getLog().size());
        assertEquals(COND_HAVE_NO_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAVE_NO_ACTIVE_TRADE_CHALK.getInfo(), executionResult.getLog().get(2));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getLog().get(3));
    }

}