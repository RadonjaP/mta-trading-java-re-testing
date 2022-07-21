import org.junit.jupiter.api.Test;
import re.ExecutionRs;
import re.RuleEngineBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static re.impl.ConditionsAndRules.*;
import static re.impl.ConditionsAndRules.ACTION_OPEN_TRADE;

class PplnPlaygroundEnvTest {

    @Test
    void test_fullPipeline() {
        ExecutionRs executionResult = new RuleEngineBuilder()
                .optional(COND_BASELINE_IS_CROSSED)
                    .and(COND_SIGNAL_IS_PULLBACK)
                    //.then(ACTION_ADD_PENDING_TRADE)
                    .terminate(ACTION_ADD_PENDING_TRADE)
                .optional(COND_HAS_CFRM_SIGNAL)
                    .and(COND_HAS_2ND_CFRM_SIGNAL) // 2nd Indicator Confirms
                    .and(COND_BASELINE_MATCH_TREND)
                    .optional(COND_IS_CONTINUATION_TRADE)
                        .then(ACTION_OPEN_TRADE)
//                    .and(COND_IS_NOT_CONTINUATION_TRADE)
                    .and(COND_VOLUME_EXISTS)
                    //.and(COND_PRICE_TO_BASELINE_NOT_GTHEN_ATR)
                    .then(ACTION_OPEN_TRADE)

                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(1, executionResult.getMessages().size());
        assertEquals(ACTION_ADD_PENDING_TRADE.getInfo(), executionResult.getMessages().get(0));
    }

    @Test
    void test_fullPipeline_2() {
        ExecutionRs executionResult = new RuleEngineBuilder()
                .optional(COND_BASELINE_IS_CROSSED)
                    .and(COND_SIGNAL_IS_NOT_PULLBACK)
                        .terminate(ACTION_MOCK_SIMPLE)
                .link()
                .when(COND_HAS_CFRM_SIGNAL)
                .link()
                .optional(COND_HAS_2ND_CFRM_SIGNAL)
                    //.terminate(ACTION_ADD_PENDING_TRADE)// 2nd Indicator Confirms
                .link()
                .optional(COND_BASELINE_MATCH_TREND)
                .and(COND_IS_NOT_CONTINUATION_TRADE)
                    .terminate(ACTION_PRE_OPEN_TRADE_STEP)
                .link()
                .when(COND_VOLUME_EXISTS)
                .and(COND_PRICE_TO_BASELINE_LTHEN_ATR)
                    .terminate(ACTION_OPEN_TRADE)
                .build()
                .run();

        //assertTrue(executionResult.isExecSuccess(), "Successful execution.");
        assertEquals(1, executionResult.getMessages().size());
        //assertEquals(ACTION_ADD_PENDING_TRADE.getInfo(), executionResult.getMessages().get(0));
    }

}
