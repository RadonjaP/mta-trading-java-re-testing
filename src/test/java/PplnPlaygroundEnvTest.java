import org.junit.jupiter.api.Test;
import re.ExecutionRs;
import re.RuleEngineBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static re.impl.ConditionsAndRules.*;
import static re.impl.ConditionsAndRules.ACTION_OPEN_TRADE;

public class PplnPlaygroundEnvTest {

    @Test
    void whenHasActiveTrade__AndSLHit_AndCfrmSignal_thenCloseTradeWL_AndOpenNewTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder()
                .optional(COND_HAVE_ACTIVE_TRADE)
                .then(ACTION_MANAGE_TRADE)
                    .optional(COND_PRICE_DID_NOT_HIT_SL)
                    .or(COND_PRICE_DID_NOT_HIT_TP)
                    .unrestricted(ACTION_CLOSE_TRADE_WL)
                .link()
                .when(COND_HAS_NO_CFRM_SIGNAL)
                .unrestricted(ACTION_PRE_OPEN_TRADE_STEP)
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

}
