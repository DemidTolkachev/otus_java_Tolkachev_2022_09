package ru.otus.junit.runner;

import ru.otus.junit.After;
import ru.otus.junit.Before;
import ru.otus.junit.Test;
import ru.otus.junit.runner.options.out.Output;
import ru.otus.junit.runner.options.out.OutputToConsole;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestRunner implements Runner {
    public static void run(Class<?> clazz) {
        List<ResultOfRunning> runResults = Stream.of(clazz).map(TestRunner::invokeTestClass).collect(Collectors.toList());
        Output output = new OutputToConsole();
        output.printTestTrace(runResults);
        output.printReport(runResults);
    }

    private static ResultOfRunning invokeTestClass(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> testMethods = Arrays.stream(methods).filter(method -> method.isAnnotationPresent(Test.class)).collect(Collectors.toList());
        List<TestClass.Result> results = new ArrayList<TestClass.Result>();
        List<TestClass.Result> beforeResults = null;
        List<TestClass.Result> testResults = null;
        for (Method testMethod : testMethods) {
            var testClass = TestClass.of(clazz);
            boolean runTests = true;

            beforeResults = testClass.invokeMethods(Before.class, methods);
            for (TestClass.Result result : beforeResults) {
                if (result.getType().equals(TestClass.Result.Type.ERROR)) {
                    runTests = false;
                    break;
                }
            }
            if (runTests == true) {
                testResults = testClass.invokeMethod(new Method[]{testMethod});
                for (TestClass.Result result : testResults) {
                    results.add(result);
                }
            }
            testClass.invokeMethods(After.class, methods);
        }

        return new ResultOfRunning(clazz, results);
    }
}
