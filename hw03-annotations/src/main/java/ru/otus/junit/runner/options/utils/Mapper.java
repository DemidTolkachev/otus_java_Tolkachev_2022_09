package ru.otus.junit.runner.options.utils;

import ru.otus.junit.runner.ResultOfRunning;
import ru.otus.junit.runner.TestClass;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class Mapper {

    private Mapper() {
    }

    public static BiFunction<List<ResultOfRunning>, Predicate<TestClass.Result>, Long> toCountResults =
            (results, predicate) -> results.stream().map(ResultOfRunning::getResults).flatMap(List::stream)
                    .filter(predicate).count();

}
