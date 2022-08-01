package utils;

import com.rprelevic.re.model.ExecutionRs;

public class TestUtility {

    public static void printLog(ExecutionRs executionRs) {
        System.out.println("-----------------------------------");
        for (String log : executionRs.getLog()) {
            System.out.println(log);
        }
    }

}