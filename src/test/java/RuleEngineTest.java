import org.junit.Assert;
import org.junit.Test;
import re.RuleEngineBuilder;
import re.impl.BasicCondition;
import re.impl.PrintAction;

public class RuleEngineTest {

    @Test
    public void test_simpleWhenThen() {
        boolean executionRes = new RuleEngineBuilder()
                .when(new BasicCondition(1 == 1))
                .then(new PrintAction("Hello World I am Rule Engine. [test_simpleWhenThen]"))
                .build()
                .run();

        Assert.assertTrue(executionRes);
    }

    @Test
    public void test_complexWhenThen() {
        boolean executionRes = new RuleEngineBuilder()
                .when(new BasicCondition(1 == 1))
                .and(new BasicCondition(2 == 2))
                .or(new BasicCondition(1 == 2))
                .then(new PrintAction("Hello World I am Rule Engine. [test_complexWhenThen]"))
                .build()
                .run();

        Assert.assertTrue(executionRes);
    }

    @Test
    public void test_complexWhenThen_orAnd_expectFalseAndNoMessage() {
        boolean executionRes = new RuleEngineBuilder()
                .when(new BasicCondition(1 == 3))
                .and(new BasicCondition(2 == 2))
                .or(new BasicCondition(1 == 2))
                .then(new PrintAction("Hello World I am Rule Engine. [test_complexWhenThen_orAnd_expectFalseAndNoMessage]"))
                .build()
                .run();

        Assert.assertFalse(executionRes);
    }

    @Test
    public void test_complexWhenThen_orAnd_expectTrue() {
        boolean executionRes = new RuleEngineBuilder()
                .when(new BasicCondition(1 == 3))
                .and(new BasicCondition(1 == 1))
                .or(new BasicCondition(1 == 1))
                .then(new PrintAction("Hello World I am Rule Engine. [test_complexWhenThen_orAnd_expectTrue]"))
                .build()
                .run();

        Assert.assertTrue(executionRes);
    }

    @Test
    public void test_complexTwoLevelsWhenThen_orAnd_expectFalseAndNoMessages() {
        boolean executionRes = new RuleEngineBuilder()
                .when(new BasicCondition(1 == 3))
                .and(new BasicCondition(1 == 1))
                .or(new BasicCondition(1 == 1))
                .and(new BasicCondition(5 < 3))
                .then(new PrintAction("Hello World I am Rule Engine. [test_complexTwoLevelsWhenThen_orAnd_expectFalseAndNoMessages]"))
                .build()
                .run();

        Assert.assertFalse(executionRes);
    }

    @Test
    public void test_multiActionExecution_expectAllActionsExecuted() {
        boolean executionRes = new RuleEngineBuilder()
                .when(new BasicCondition(true))
                .then(new PrintAction("Action 1"))
                .then(new PrintAction("Action 2"))
                .when(new BasicCondition(1 == 1))
                .then(new PrintAction("Action 3"))
                .build()
                .run();

        Assert.assertTrue(executionRes);
    }

    @Test
    public void test_multiActionExecution_breakBeforeSecondAction() {
        boolean executionRes = new RuleEngineBuilder()
                .when(new BasicCondition(true))
                .then(new PrintAction("Action 1"))
                .link()
                .when(new BasicCondition(2 == 1))
                .then(new PrintAction("Action 2"))
                .build()
                .run();

        Assert.assertFalse(executionRes);
    }

    @Test
    public void test_multiActionExecution_fullTree() {
        boolean executionRes = new RuleEngineBuilder()
                .when(new BasicCondition(true))
                    .then(new PrintAction("BasicManageTradeAction"))
                    .when(new BasicCondition(true))
                        .or(new BasicCondition(false))
                        .then(new PrintAction("BasicCloseTradeAction"))
                .link()
                .when(new BasicCondition(true))
                .then(new PrintAction("BasicOpenTradeAction"))
                .when(new BasicCondition(true))
                    .and(new BasicCondition(true))
                    .then(new PrintAction("BasicCloseTradeAction"))
                .link()
                .when(new BasicCondition(true))
                .then(new PrintAction("CreateTradeAction"))
                .build()
                .run();

        Assert.assertTrue(executionRes);
    }

    @Test
    public void test_multiActionExecution_fullTree_NoActiveTrade_expectThreeMessages() {
        boolean executionRes = new RuleEngineBuilder()
                .optional(new BasicCondition(false)) // Does active trade exist?
                .then(new PrintAction("BasicManageTradeAction"))
                    .optional(new BasicCondition(false)) // SL hit?
                        .or(new BasicCondition(false)) // TP hit?
                        .then(new PrintAction("BasicCloseTradeAction1"))
                .link()
                .when(new BasicCondition(true)) // Confirmation signal?
                .and(new BasicCondition(true)) // 2nd Confirmation signal?
                .and(new BasicCondition(true)) // Baseline trend signal?
                .and(new BasicCondition(true)) // Volume signal?
                .then(new PrintAction("BasicOpenTradeAction"))
                .link()
                .optional(new BasicCondition(true)) // Does active trade exist?
                    .and(new BasicCondition(true)) // There is exit signal?
                    .then(new PrintAction("BasicCloseTradeAction2"))
                .link()
                .when(new BasicCondition(true)) // TODO: Unnecessary, this serves only to make sure CreateTradeAction is called
                .then(new PrintAction("CreateTradeAction"))
                .build()
                .run();

        //Assert.assertTrue(executionRes);
    }

}
