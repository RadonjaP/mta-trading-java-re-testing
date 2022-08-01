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

class RuleEngineVolumeIndiPplnTest {

    @Test
    public void testEntrySignal_whenBaselineCrossed_andNoCfrm_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_CROSSED)
                .add(BASELINE_CROSSED, HAS_NO_CFRM_SIGNAL, HAS_NO_CFRM_SIGNAL)
                .addOnlyTrue(HAS_NO_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND)
                .addOnlyTrue(BASELINE_MATCH_TREND, VOLUME_EXISTS)
                .addOnlyTrue(VOLUME_EXISTS, BASIC_OPEN_TRADE)
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
                .addOnlyTrue(BASELINE_MATCH_TREND, VOLUME_EXISTS)
                .addOnlyTrue(VOLUME_EXISTS, BASIC_OPEN_TRADE)
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
                .addOnlyTrue(BASELINE_MATCH_TREND, VOLUME_EXISTS)
                .addOnlyTrue(VOLUME_EXISTS, BASIC_OPEN_TRADE)
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
                .addOnlyTrue(BASELINE_NOT_MATCH_TREND, VOLUME_EXISTS)
                .addOnlyTrue(VOLUME_EXISTS, BASIC_OPEN_TRADE)
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
    public void testEntrySignal_whenBaselineCrossed_andCfrmAnd2ndCfrmAndTrendMatch_andNoVolume_thenNoTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_CROSSED)
                .add(BASELINE_CROSSED, HAS_CFRM_SIGNAL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND)
                .addOnlyTrue(BASELINE_MATCH_TREND, VOLUME_NOT_EXISTS)
                .addOnlyTrue(VOLUME_NOT_EXISTS, BASIC_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(5, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAS_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_MATCH_TREND.getInfo(), executionResult.getLog().get(3));
        assertEquals(COND_VOLUME_DOES_NOT_EXIST.getInfo(), executionResult.getLog().get(4));
    }

    @Test
    public void testEntrySignal_whenBaselineCrossed_andCfrmAnd2ndCfrmAndTrendMatchAndVolume_thenOpenTrade() {
        ExecutionRs executionResult = new RuleEngineBuilder(new TestRuleRegistry())
                .start(BASELINE_CROSSED)
                .add(BASELINE_CROSSED, HAS_CFRM_SIGNAL, HAS_CFRM_SIGNAL)
                .addOnlyTrue(HAS_CFRM_SIGNAL, HAS_2ND_CFRM_SIGNAL)
                .addOnlyTrue(HAS_2ND_CFRM_SIGNAL, BASELINE_MATCH_TREND)
                .addOnlyTrue(BASELINE_MATCH_TREND, VOLUME_EXISTS)
                .addOnlyTrue(VOLUME_EXISTS, BASIC_OPEN_TRADE)
                .build()
                .run();

        assertTrue(executionResult.isExecSuccess(), "Successful execution.");

        assertEquals(6, executionResult.getLog().size());
        assertEquals(COND_BASELINE_IS_CROSSED.getInfo(), executionResult.getLog().get(0));
        assertEquals(COND_HAS_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(1));
        assertEquals(COND_HAS_2ND_CFRM_SIGNAL.getInfo(), executionResult.getLog().get(2));
        assertEquals(COND_BASELINE_MATCH_TREND.getInfo(), executionResult.getLog().get(3));
        assertEquals(COND_VOLUME_EXISTS.getInfo(), executionResult.getLog().get(4));
        assertEquals(ACTION_OPEN_TRADE.getInfo(), executionResult.getLog().get(5));
    }

}