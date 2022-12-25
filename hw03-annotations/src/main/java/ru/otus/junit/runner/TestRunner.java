package ru.otus.junit.runner;

import ru.otus.junit.After;
import ru.otus.junit.Before;
import ru.otus.junit.Test;
import ru.otus.junit.runner.options.out.Output;
import ru.otus.junit.runner.options.out.OutputToConsole;

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
        var testClass = TestClass.of(clazz);

        testClass.invokeMethods(Before.class);
        final var results = testClass.invokeMethods(Test.class);
        testClass.invokeMethods(After.class);

        return new ResultOfRunning(clazz, results);
    }
}
