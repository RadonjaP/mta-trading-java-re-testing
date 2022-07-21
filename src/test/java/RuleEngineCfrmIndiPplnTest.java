import org.junit.jupiter.api.Test;
import re.ExecutionRs;
import re.RuleEngineBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static re.impl.ConditionsAndRules.*;
import static re.impl.ConditionsAndRules.ACTION_MOCK_SIMPLE;
import static re.impl.ConditionsAndRules.COND_HAVE_NO_ACTIVE_TRADE;

class RuleEngineCfrmIndiPplnTest {

    @Test
    void test_simpleWhenThen() {
        ExecutionRs executionResult = new RuleEngineBuilder()
                .when(COND_MOCK_TRUE)
                .then(ACTION_MOCK_SIMPLE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(1, executionResult.getMessages().size());
        assertEquals(ACTION_MOCK_SIMPLE.getInfo(), executionResult.getMessages().get(0));
    }

    @Test
    void whenNoActiveTradeAndCfrmSignal_thenOpenTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder()
                .optional(COND_HAVE_NO_ACTIVE_TRADE)
                    .then(ACTION_MANAGE_TRADE)
                    .optional(COND_PRICE_DID_NOT_HIT_SL)
                        .or(COND_PRICE_DID_NOT_HIT_TP)
                        .then(ACTION_CLOSE_TRADE_WL)
                .link()
                .when(COND_HAS_CFRM_SIGNAL)
                .then(ACTION_PRE_OPEN_TRADE_STEP)
                .link()
                .optional(COND_HAVE_NO_ACTIVE_TRADE_CHALK)
                    .and(COND_DID_NOT_GET_EXIT_SIGNAL)
                    .then(ACTION_CLOSE_TRADE_CHALK)
                .link()
                .unrestricted(ACTION_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(2, executionResult.getMessages().size());
        assertEquals(ACTION_PRE_OPEN_TRADE_STEP.getInfo(), executionResult.getMessages().get(0));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getMessages().get(1));
    }

    @Test
    void whenHasActiveTrade__AndSLTPNotHit_AndCfrmSignal_thenCloseTrade_AndOpenNewTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder()
                .optional(COND_HAVE_ACTIVE_TRADE)
                    .then(ACTION_MANAGE_TRADE)
                    .optional(COND_PRICE_DID_NOT_HIT_SL)
                        .or(COND_PRICE_DID_NOT_HIT_TP)
                        .then(ACTION_CLOSE_TRADE_WL)
                .link()
                .when(COND_HAS_CFRM_SIGNAL)
                .then(ACTION_PRE_OPEN_TRADE_STEP)
                .link()
                .optional(COND_HAVE_NO_ACTIVE_TRADE_CHALK)
                    .and(COND_DID_NOT_GET_EXIT_SIGNAL)
                    .then(ACTION_CLOSE_TRADE_CHALK)
                .link()
                .unrestricted(ACTION_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(3, executionResult.getMessages().size());
        assertEquals(ACTION_MANAGE_TRADE.getInfo(), executionResult.getMessages().get(0));
        assertEquals(ACTION_PRE_OPEN_TRADE_STEP.getInfo(), executionResult.getMessages().get(1));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getMessages().get(2));
    }

    @Test
    void whenHasActiveTrade__AndSLHit_AndCfrmSignal_thenCloseTradeWL_AndOpenNewTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder()
                .optional(COND_HAVE_ACTIVE_TRADE)
                    .then(ACTION_MANAGE_TRADE)
                    .optional(COND_PRICE_HIT_SL)
                        .or(COND_PRICE_DID_NOT_HIT_TP)
                        .then(ACTION_CLOSE_TRADE_WL)
                .link()
                .when(COND_HAS_CFRM_SIGNAL)
                .then(ACTION_PRE_OPEN_TRADE_STEP)
                .link()
                .optional(COND_HAVE_NO_ACTIVE_TRADE_CHALK)
                    .and(COND_DID_NOT_GET_EXIT_SIGNAL)
                    .then(ACTION_CLOSE_TRADE_CHALK)
                .link()
                .unrestricted(ACTION_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(4, executionResult.getMessages().size());
        assertEquals(ACTION_MANAGE_TRADE.getInfo(), executionResult.getMessages().get(0));
        assertEquals(ACTION_CLOSE_TRADE_WL.getInfo(), executionResult.getMessages().get(1));
        assertEquals(ACTION_PRE_OPEN_TRADE_STEP.getInfo(), executionResult.getMessages().get(2));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getMessages().get(3));
    }

    @Test
    void whenHasActiveTrade__AndTPHit_AndCfrmSignal_thenCloseTradeWL_AndOpenNewTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder()
                .optional(COND_HAVE_ACTIVE_TRADE)
                .then(ACTION_MANAGE_TRADE)
                    .optional(COND_PRICE_DID_NOT_HIT_SL)
                        .or(COND_PRICE_HIT_TP)
                        .then(ACTION_CLOSE_TRADE_WL)
                .link()
                .when(COND_HAS_CFRM_SIGNAL)
                .then(ACTION_PRE_OPEN_TRADE_STEP)
                .link()
                .optional(COND_HAVE_NO_ACTIVE_TRADE_CHALK)
                    .and(COND_DID_NOT_GET_EXIT_SIGNAL)
                    .then(ACTION_CLOSE_TRADE_CHALK)
                .link()
                .unrestricted(ACTION_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(4, executionResult.getMessages().size());
        assertEquals(ACTION_MANAGE_TRADE.getInfo(), executionResult.getMessages().get(0));
        assertEquals(ACTION_CLOSE_TRADE_WL.getInfo(), executionResult.getMessages().get(1));
        assertEquals(ACTION_PRE_OPEN_TRADE_STEP.getInfo(), executionResult.getMessages().get(2));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getMessages().get(3));
    }

    @Test
    void whenHasNoActiveTrade_AndNoCfrmSignal_thenStopPplnExec() {
        ExecutionRs executionResult = new RuleEngineBuilder()
                .optional(COND_HAVE_NO_ACTIVE_TRADE)
                .then(ACTION_MANAGE_TRADE)
                    .optional(COND_PRICE_DID_NOT_HIT_SL)
                        .or(COND_PRICE_HIT_TP)
                        .then(ACTION_CLOSE_TRADE_WL)
                .link()
                .when(COND_HAS_NO_CFRM_SIGNAL)
                .then(ACTION_PRE_OPEN_TRADE_STEP)
                .link()
                .optional(COND_HAVE_NO_ACTIVE_TRADE_CHALK)
                    .and(COND_DID_NOT_GET_EXIT_SIGNAL)
                    .then(ACTION_CLOSE_TRADE_CHALK)
                .link()
                .unrestricted(ACTION_OPEN_TRADE)
                .build()
                .run();

        assertFalse(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(0, executionResult.getMessages().size());
    }

    @Test
    void whenHasActiveTrade_AndNoCfrmSignal_thenStopPplnExec() {
        ExecutionRs executionResult = new RuleEngineBuilder()
                .optional(COND_HAVE_ACTIVE_TRADE)
                    .then(ACTION_MANAGE_TRADE)
                    .optional(COND_PRICE_DID_NOT_HIT_SL)
                        .or(COND_PRICE_DID_NOT_HIT_TP)
                        .then(ACTION_CLOSE_TRADE_WL)
                .link()
                .when(COND_HAS_NO_CFRM_SIGNAL)
                .then(ACTION_PRE_OPEN_TRADE_STEP)
                .link()
                .optional(COND_HAVE_NO_ACTIVE_TRADE_CHALK)
                    .and(COND_DID_NOT_GET_EXIT_SIGNAL)
                    .then(ACTION_CLOSE_TRADE_CHALK)
                .link()
                .unrestricted(ACTION_OPEN_TRADE)
                .build()
                .run();

        assertFalse(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(1, executionResult.getMessages().size());
        assertEquals(ACTION_MANAGE_TRADE.getInfo(), executionResult.getMessages().get(0));
    }

    @Test
    void whenHasNoActiveTrade_AndCfrmSignal_thenCreateNewTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder()
                .optional(COND_HAVE_NO_ACTIVE_TRADE)
                .then(ACTION_MANAGE_TRADE)
                    .optional(COND_PRICE_DID_NOT_HIT_SL)
                        .or(COND_PRICE_DID_NOT_HIT_TP)
                        .then(ACTION_CLOSE_TRADE_WL)
                .link()
                .when(COND_HAS_CFRM_SIGNAL)
                .then(ACTION_PRE_OPEN_TRADE_STEP)
                .link()
                .optional(COND_HAVE_NO_ACTIVE_TRADE_CHALK)
                    .and(COND_DID_NOT_GET_EXIT_SIGNAL)
                    .then(ACTION_CLOSE_TRADE_CHALK)
                .link()
                .unrestricted(ACTION_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(2, executionResult.getMessages().size());
        assertEquals(ACTION_PRE_OPEN_TRADE_STEP.getInfo(), executionResult.getMessages().get(0));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getMessages().get(1));
    }

    @Test
    void whenHasOnlyActiveTradeChalk_AndCfrmSignal_AndExitSignal_thenCloseChalkTrade_thenCreateNewTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder()
                .optional(COND_HAVE_NO_ACTIVE_TRADE)
                .then(ACTION_MANAGE_TRADE)
                    .optional(COND_PRICE_DID_NOT_HIT_SL)
                        .or(COND_PRICE_DID_NOT_HIT_TP)
                        .then(ACTION_CLOSE_TRADE_WL)
                .link()
                .when(COND_HAS_CFRM_SIGNAL)
                .then(ACTION_PRE_OPEN_TRADE_STEP)
                .link()
                .optional(COND_HAVE_ACTIVE_TRADE_CHALK)
                    .and(COND_GOT_EXIT_SIGNAL)
                    .then(ACTION_CLOSE_TRADE_CHALK)
                .link()
                .unrestricted(ACTION_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(3, executionResult.getMessages().size());
        assertEquals(ACTION_PRE_OPEN_TRADE_STEP.getInfo(), executionResult.getMessages().get(0));
        assertEquals(ACTION_CLOSE_TRADE_CHALK.getInfo(), executionResult.getMessages().get(1));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getMessages().get(2));
    }

    @Test
    void whenHasNoActiveTradeChalk_AndCfrmSignal_AndExitSignal_thenNoChalkTrade_thenCreateNewTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder()
                .optional(COND_HAVE_NO_ACTIVE_TRADE)
                .then(ACTION_MANAGE_TRADE)
                    .optional(COND_PRICE_DID_NOT_HIT_SL)
                        .or(COND_PRICE_DID_NOT_HIT_TP)
                        .then(ACTION_CLOSE_TRADE_WL)
                .link()
                .when(COND_HAS_CFRM_SIGNAL)
                .then(ACTION_PRE_OPEN_TRADE_STEP)
                .link()
                .optional(COND_HAVE_NO_ACTIVE_TRADE_CHALK)
                    .and(COND_GOT_EXIT_SIGNAL)
                    .then(ACTION_CLOSE_TRADE_CHALK)
                .link()
                .unrestricted(ACTION_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(2, executionResult.getMessages().size());
        assertEquals(ACTION_PRE_OPEN_TRADE_STEP.getInfo(), executionResult.getMessages().get(0));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getMessages().get(1));
    }

    @Test
    void whenHasActiveTradeChalk_AndCfrmSignal_AndNoExitSignal_thenNoChalkTrade_thenCreateNewTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder()
                .optional(COND_HAVE_NO_ACTIVE_TRADE)
                .then(ACTION_MANAGE_TRADE)
                    .optional(COND_PRICE_DID_NOT_HIT_SL)
                        .or(COND_PRICE_DID_NOT_HIT_TP)
                        .then(ACTION_CLOSE_TRADE_WL)
                .link()
                .when(COND_HAS_CFRM_SIGNAL)
                .then(ACTION_PRE_OPEN_TRADE_STEP)
                .link()
                .optional(COND_HAVE_NO_ACTIVE_TRADE_CHALK)
                    .and(COND_GOT_EXIT_SIGNAL)
                    .then(ACTION_CLOSE_TRADE_CHALK)
                .link()
                .unrestricted(ACTION_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(2, executionResult.getMessages().size());
        assertEquals(ACTION_PRE_OPEN_TRADE_STEP.getInfo(), executionResult.getMessages().get(0));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getMessages().get(1));
    }

}
