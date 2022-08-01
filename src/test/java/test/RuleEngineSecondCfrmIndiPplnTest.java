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

class RuleEngineSecondCfrmIndiPplnTest {

    @Test
    void whenNoActiveTradeAndCfrmSignalAnd2ndCfrmSignal_thenOpenTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_NOT_TRADE_EXISTS)
                .add(ACTIVE_NOT_TRADE_EXISTS, STOP_LOSS_HIT, HAS_CFRM_SIGNAL)
                .add(STOP_LOSS_HIT, BASIC_CLOSE_TRADE_WL, TAKE_PROFIT_HIT)
                .add(TAKE_PROFIT_HIT, BASIC_CLOSE_TRADE_WL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_WL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, ACTIVE_TRADE_TO_CHALK_NOT_EXISTS)
                .add(ACTIVE_TRADE_TO_CHALK_NOT_EXISTS, EXIT_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .add(EXIT_SIGNAL, BASIC_CLOSE_TRADE_CHALK, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_2ND_CFRM_SIGNAL, BASIC_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(5, executionResult.getLog().size());
        assertEquals(COND_HAVE_NO_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAVE_NO_ACTIVE_TRADE_CHALK.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_HAS_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(3));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getLog().get(4));
    }

    @Test
    void whenNoActiveTrade_andCfrmSignal_andNo2ndCfrmSignal_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(ACTIVE_NOT_TRADE_EXISTS)
                .add(ACTIVE_NOT_TRADE_EXISTS, STOP_LOSS_HIT, HAS_CFRM_SIGNAL)
                .add(STOP_LOSS_HIT, BASIC_CLOSE_TRADE_WL, TAKE_PROFIT_HIT)
                .add(TAKE_PROFIT_HIT, BASIC_CLOSE_TRADE_WL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_WL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, ACTIVE_TRADE_TO_CHALK_NOT_EXISTS)
                .add(ACTIVE_TRADE_TO_CHALK_NOT_EXISTS, EXIT_SIGNAL, HAS_NO_2ND_CFRM_SIGNAL)
                .add(EXIT_SIGNAL, BASIC_CLOSE_TRADE_CHALK, HAS_NO_2ND_CFRM_SIGNAL)
                .addOnlyTrue(BASIC_CLOSE_TRADE_CHALK, HAS_NO_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_NO_2ND_CFRM_SIGNAL, BASIC_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(4, executionResult.getLog().size());
        assertEquals(COND_HAVE_NO_ACTIVE_TRADE.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAVE_NO_ACTIVE_TRADE_CHALK.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_HAS_NO_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(3));
    }

}