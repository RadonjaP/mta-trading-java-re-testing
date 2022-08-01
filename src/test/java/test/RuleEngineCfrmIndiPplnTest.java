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
                .start(ACTIVE_NOT_TRADE_EXISTS)
                .add(ACTIVE_NOT_TRADE_EXISTS, STOP_LOSS_HIT, HAS_CFRM_SIGNAL)
                .add(STOP_LOSS_HIT, BASIC_CLOSE_TRADE_WL, TAKE_PROFIT_HIT)
                .add(TAKE_PROFIT_HIT, BASIC_CLOSE_TRADE_WL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_WL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, ACTIVE_TRADE_TO_CHALK_NOT_EXISTS)
                .add(ACTIVE_TRADE_TO_CHALK_NOT_EXISTS, EXIT_SIGNAL, BASIC_OPEN_TRADE)
                .add(EXIT_SIGNAL, BASIC_CLOSE_TRADE_CHALK, BASIC_OPEN_TRADE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, BASIC_OPEN_TRADE)
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
                .start(ACTIVE_NOT_TRADE_EXISTS)
                .add(ACTIVE_NOT_TRADE_EXISTS, STOP_LOSS_HIT, HAS_NO_CFRM_SIGNAL)
                .add(STOP_LOSS_HIT, BASIC_CLOSE_TRADE_WL, TAKE_PROFIT_HIT)
                .add(TAKE_PROFIT_HIT, BASIC_CLOSE_TRADE_WL, HAS_NO_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_WL, HAS_NO_CFRM_SIGNAL)
                .addOnlyTrue(HAS_NO_CFRM_SIGNAL, ACTIVE_TRADE_TO_CHALK_NOT_EXISTS)
                .add(ACTIVE_TRADE_TO_CHALK_NOT_EXISTS, EXIT_SIGNAL, BASIC_OPEN_TRADE)
                .add(EXIT_SIGNAL, BASIC_CLOSE_TRADE_CHALK, BASIC_OPEN_TRADE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, BASIC_OPEN_TRADE)
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
                .add(ACTIVE_TRADE_EXISTS, STOP_LOSS_NOT_HIT, HAS_CFRM_SIGNAL)
                .add(STOP_LOSS_NOT_HIT, BASIC_CLOSE_TRADE_WL, TAKE_PROFIT_NOT_HIT)
                .add(TAKE_PROFIT_NOT_HIT, BASIC_CLOSE_TRADE_WL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_WL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, ACTIVE_TRADE_TO_CHALK_NOT_EXISTS)
                .add(ACTIVE_TRADE_TO_CHALK_NOT_EXISTS, EXIT_SIGNAL, BASIC_OPEN_TRADE)
                .add(EXIT_SIGNAL, BASIC_CLOSE_TRADE_CHALK, BASIC_OPEN_TRADE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, BASIC_OPEN_TRADE)
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
                .add(ACTIVE_TRADE_EXISTS, STOP_LOSS_HIT, HAS_NO_CFRM_SIGNAL)
                .add(STOP_LOSS_HIT, BASIC_CLOSE_TRADE_WL, TAKE_PROFIT_NOT_HIT)
                .add(TAKE_PROFIT_NOT_HIT, BASIC_CLOSE_TRADE_WL, HAS_NO_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_WL, HAS_NO_CFRM_SIGNAL)
                .addOnlyTrue(HAS_NO_CFRM_SIGNAL, ACTIVE_TRADE_TO_CHALK_NOT_EXISTS)
                .add(ACTIVE_TRADE_TO_CHALK_NOT_EXISTS, EXIT_SIGNAL, BASIC_OPEN_TRADE)
                .add(EXIT_SIGNAL, BASIC_CLOSE_TRADE_CHALK, BASIC_OPEN_TRADE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, BASIC_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(4, executionResult.getLog().size());
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_PRICE_HIT_SL.getInfo(), executionResult.getLog().get(1));
        assertEquals(ACTION_CLOSE_TRADE_WL.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_HAS_NO_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(3));
    }

    @Test
    void whenActiveTrade_andSLNotHit_andTPHit_thenCloseTradeWin() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_TRADE_EXISTS)
                .add(ACTIVE_TRADE_EXISTS, STOP_LOSS_NOT_HIT, HAS_NO_CFRM_SIGNAL)
                .add(STOP_LOSS_NOT_HIT, BASIC_CLOSE_TRADE_WL, TAKE_PROFIT_HIT)
                .add(TAKE_PROFIT_HIT, BASIC_CLOSE_TRADE_WL, HAS_NO_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_WL, HAS_NO_CFRM_SIGNAL)
                .addOnlyTrue(HAS_NO_CFRM_SIGNAL, ACTIVE_TRADE_TO_CHALK_NOT_EXISTS)
                .add(ACTIVE_TRADE_TO_CHALK_NOT_EXISTS, EXIT_SIGNAL, BASIC_OPEN_TRADE)
                .add(EXIT_SIGNAL, BASIC_CLOSE_TRADE_CHALK, BASIC_OPEN_TRADE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, BASIC_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(5, executionResult.getLog().size());
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_PRICE_DID_NOT_HIT_SL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_PRICE_HIT_TP.getInfo(), executionResult.getLog().get(2));
        assertEquals(ACTION_CLOSE_TRADE_WL.getInfo(), executionResult.getLog().get(3));
        assertEquals(COND_HAS_NO_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(4));
    }

    @Test
    void whenActiveChalkTrade_andConfirmSignal_andExitSignal_thenCloseChalk_andOpenNewTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_NOT_TRADE_EXISTS)
                .add(ACTIVE_NOT_TRADE_EXISTS, STOP_LOSS_NOT_HIT, HAS_CFRM_SIGNAL)
                .add(STOP_LOSS_NOT_HIT, BASIC_CLOSE_TRADE_WL, TAKE_PROFIT_HIT)
                .add(TAKE_PROFIT_HIT, BASIC_CLOSE_TRADE_WL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_WL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, ACTIVE_TRADE_TO_CHALK_EXISTS)
                .add(ACTIVE_TRADE_TO_CHALK_EXISTS, EXIT_SIGNAL, BASIC_OPEN_TRADE)
                .add(EXIT_SIGNAL, BASIC_CLOSE_TRADE_CHALK, BASIC_OPEN_TRADE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, BASIC_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(6, executionResult.getLog().size());
        assertEquals(COND_HAVE_NO_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_GOT_EXIT_SIGNAL.getInfo(), executionResult.getLog().get(3));
        assertEquals(ACTION_CLOSE_TRADE_CHALK.getInfo(), executionResult.getLog().get(4));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getLog().get(5));
    }


    @Test
    void whenHasActiveTradeChalk_AndCfrmSignal_AndNoExitSignal_thenNoChalkTrade_thenCreateNewTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_NOT_TRADE_EXISTS)
                .add(ACTIVE_NOT_TRADE_EXISTS, STOP_LOSS_NOT_HIT, HAS_CFRM_SIGNAL)
                .add(STOP_LOSS_NOT_HIT, BASIC_CLOSE_TRADE_WL, TAKE_PROFIT_HIT)
                .add(TAKE_PROFIT_HIT, BASIC_CLOSE_TRADE_WL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_WL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, ACTIVE_TRADE_TO_CHALK_EXISTS)
                .add(ACTIVE_TRADE_TO_CHALK_EXISTS, NO_EXIT_SIGNAL, BASIC_OPEN_TRADE)
                .add(NO_EXIT_SIGNAL, BASIC_CLOSE_TRADE_CHALK, BASIC_OPEN_TRADE)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, BASIC_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(5, executionResult.getLog().size());
        assertEquals(COND_HAVE_NO_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAVE_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_DID_NOT_GET_EXIT_SIGNAL.getInfo(), executionResult.getLog().get(3));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getLog().get(4));
    }

}