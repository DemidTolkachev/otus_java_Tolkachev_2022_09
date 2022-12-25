package ru.otus.junit.runner.options.out;

import ru.otus.junit.runner.ResultOfRunning;

import java.util.List;

public interface Output {

    void printTestTrace(List<ResultOfRunning> results);

    void printReport(List<ResultOfRunning> results);
}
