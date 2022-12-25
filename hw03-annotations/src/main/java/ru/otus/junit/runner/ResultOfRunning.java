package ru.otus.junit.runner;

import ru.otus.junit.runner.TestClass.Result;

import java.util.List;

public class ResultOfRunning {
    private final Class<?> clazz;
    private final List<Result> results;

    public Class<?> getClazz() {
        return this.clazz;
    }

    public List<Result> getResults() {
        return this.results;
    }

    public ResultOfRunning(final Class<?> clazz, final List<Result> results) {
        this.clazz = clazz;
        this.results = results;
    }
}
